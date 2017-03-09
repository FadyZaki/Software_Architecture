/*---------------------------------------------------------------------------*\
  $Id$
  ---------------------------------------------------------------------------
  This software is released under a BSD-style license:

  Copyright (c) 2004-2007 Brian M. Clapper. All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are
  met:

  1.  Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.

  2.  The end-user documentation included with the redistribution, if any,
      must include the following acknowlegement:

        "This product includes software developed by Brian M. Clapper
        (bmc@clapper.org, http://www.clapper.org/bmc/). That software is
        copyright (c) 2004-2007 Brian M. Clapper."

      Alternately, this acknowlegement may appear in the software itself,
      if wherever such third-party acknowlegements normally appear.

  3.  Neither the names "clapper.org", "clapper.org Java Utility Library",
      nor any of the names of the project contributors may be used to
      endorse or promote products derived from this software without prior
      written permission. For written permission, please contact
      bmc@clapper.org.

  4.  Products derived from this software may not be called "clapper.org
      Java Utility Library", nor may "clapper.org" appear in their names
      without prior written permission of Brian M. Clapper.

  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
  NO EVENT SHALL BRIAN M. CLAPPER BE LIABLE FOR ANY DIRECT, INDIRECT,
  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\*---------------------------------------------------------------------------*/

package com.architecture.specification.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import com.architecture.specification.util.classfilter.JavassistClassFilter;
import com.architecture.specification.util.file.AndFileFilter;
import com.architecture.specification.util.file.FileFilterMatchType;
import com.architecture.specification.util.file.FileOnlyFilter;
import com.architecture.specification.util.file.RecursiveFileFinder;
import com.architecture.specification.util.file.RegexFileFilter;
import com.architecture.specification.util.log.Logger;

import org.objectweb.asm.ClassReader;

/**
 * <p>
 * A <tt>ClassFinder</tt> object is used to find classes. By default, an
 * instantiated <tt>ClassFinder</tt> won't find any classes; you have to add the
 * classpath (via a call to {@link #addClassPath}), add jar files, add zip
 * files, and/or add directories to the <tt>ClassFinder</tt> so it knows where
 * to look. Adding a jar file to a <tt>ClassFinder</tt> causes the
 * <tt>ClassFinder</tt> to look at the jar's manifest for a "Class-Path" entry;
 * if the <tt>ClassFinder</tt> finds such an entry, it adds the contents to the
 * search path, as well. After the <tt>ClassFinder</tt> has been "primed" with
 * things to search, you call its {@link #findClasses findClasses()} method to
 * have it search for the classes, optionally passing a {@link JavassistClassFilter} that
 * can be used to filter out classes you're not interested in.
 * </p>
 *
 * <p>
 * This package also contains a rich set of {@link JavassistClassFilter} implementations,
 * including:
 * </p>
 *
 * <ul>
 * <li>A {@link RegexClassFilter} for filtering class names on a regular
 * expression
 * <li>Filters for testing various class attributes (such as whether a class is
 * an interface, or a subclass of a known class, etc.
 * <li>Filters that can combine other filters in logical operations
 * </ul>
 *
 * <p>
 * The following example illustrates how you might use a <tt>ClassFinder</tt> to
 * locate all non-abstract classes that implement the <tt>ClassFilter</tt>
 * interface, searching the classpath as well as anything specified on the
 * command line.
 * </p>
 *
 * <blockquote>
 * 
 * <pre>
 * import org.clapper.util.classutil.*;
 *
 * public class Test {
 * 	public static void main(String[] args) throws Throwable {
 * 		ClassFinder finder = new ClassFinder();
 * 		for (String arg : args)
 * 			finder.add(new File(arg));
 *
 * 		ClassFilter filter = new AndClassFilter
 * 		// Must not be an interface
 * 		(new NotClassFilter(new InterfaceOnlyClassFilter()),
 *
 * 		// Must implement the ClassFilter interface
 * 				new SubclassClassFilter(ClassFilter.class),
 *
 * 		// Must not be abstract
 * 				new NotClassFilter(new AbstractClassFilter()));
 *
 * 		Collection&lt;ClassInfo&gt; foundClasses = new ArrayList&lt;ClassInfo&gt;();
 * 		finder.findClasses(foundClasses, filter);
 *
 * 		for (ClassInfo classInfo : foundClasses)
 * 			System.out.println("Found " + classInfo.getClassName());
 * 	}
 * }
 * </pre>
 * 
 * </blockquote>
 *
 * <p>
 * This class, and the {@link ClassInfo} class, rely on the ASM byte-code
 * manipulation library. If that library is not available, this package will not
 * work. See <a href="http://asm.objectweb.org"><i>asm.objectweb.org</i></a> for
 * details on ASM.
 * </p>
 *
 * <p>
 * <b>WARNING: This class is not thread-safe.</b>
 * </p>
 *
 * @version <tt>$Revision$</tt>
 *
 * @author Copyright &copy; 2006 Brian M. Clapper
 */
public class ClassFinder {
	/*----------------------------------------------------------------------*\
	                        Private Data Items
	\*----------------------------------------------------------------------*/

	/**
	 * Places to search.
	 */
	private LinkedHashMap<String, File> placesToSearch = new LinkedHashMap<String, File>();

	/**
	 * Found classes. Cleared after every call to findClasses()
	 */
	private Collection<String> foundClasses = new ArrayList<String>();

	/**
	 * For logging
	 */
	private static final Logger log = new Logger(ClassFinder.class);

	/*----------------------------------------------------------------------*\
	                            Constructor
	\*----------------------------------------------------------------------*/

	/**
	 * Create a new <tt>ClassFinder</tt> that will search for classes using the
	 * default class loader.
	 */
	public ClassFinder() {
		// Nothing to do
	}

	/*----------------------------------------------------------------------*\
	                          Public Methods
	\*----------------------------------------------------------------------*/

	/**
	 * Add the contents of the system classpath for classes.
	 */
	public void addClassPath() {
		String path = null;

		try {
			path = System.getProperty("java.class.path");
		}

		catch (Exception ex) {
			path = "";
			log.error("Unable to get class path", ex);
		}

		StringTokenizer tok = new StringTokenizer(path, File.pathSeparator);

		while (tok.hasMoreTokens())
			add(new File(tok.nextToken()));
	}

	/**
	 * Add a jar file, zip file or directory to the list of places to search for
	 * classes.
	 *
	 * @param file
	 *            the jar file, zip file or directory
	 *
	 * @return <tt>true</tt> if the file was suitable for adding; <tt>false</tt>
	 *         if it was not a jar file, zip file, or directory.
	 */
	public boolean add(File file) {
		boolean added = false;

		if (ClassUtil.fileCanContainClasses(file)) {
			String absPath = file.getAbsolutePath();
			if (placesToSearch.get(absPath) == null) {
				placesToSearch.put(absPath, file);
				if (isJar(absPath))
					loadJarClassPathEntries(file);
			}

			added = true;
		}

		return added;
	}

	/**
	 * Add an array jar files, zip files and/or directories to the list of
	 * places to search for classes.
	 *
	 * @param files
	 *            the array of jar files, zip files and/or directories. The
	 *            array can contain a mixture of all of the above.
	 *
	 * @return the total number of items from the array that were actually
	 *         added. (Elements that aren't jars, zip files or directories are
	 *         skipped.)
	 */
	public int add(File[] files) {
		int totalAdded = 0;

		for (File file : files) {
			if (add(file))
				totalAdded++;
		}

		return totalAdded;
	}

	/**
	 * Add a <tt>Collection</tt> of jar files, zip files and/or directories to
	 * the list of places to search for classes.
	 *
	 * @param files
	 *            the collection of jar files, zip files and/or directories.
	 *
	 * @return the total number of items from the collection that were actually
	 *         added. (Elements that aren't jars, zip files or directories are
	 *         skipped.)
	 */
	public int add(Collection<File> files) {
		int totalAdded = 0;

		for (File file : files) {
			if (add(file))
				totalAdded++;
		}

		return totalAdded;
	}

	/**
	 * Clear the finder's notion of where to search.
	 */
	public void clear() {
		placesToSearch.clear();
		foundClasses.clear();
	}

	/**
	 * Search all classes in the search areas, keeping only those that pass the
	 * specified filter.
	 *
	 * @param classes
	 *            where to store the resulting matches
	 * @param filter
	 *            the filter, or null for no filter
	 *
	 * @return the number of matched classes added to the collection
	 */
	public Collection<String> findClasses() {

		foundClasses.clear();

		// Load all the classes first.

		for (File file : placesToSearch.values()) {
			String name = file.getPath();

			log.info("Finding classes in " + name);
			if (isJar(name))
				processJar(name);
			else if (isZip(name))
				processZip(name);
			else
				processDirectory(file);
		}

		log.info("Loaded " + foundClasses.size() + " classes.");
		
		return foundClasses;
	}

	/*----------------------------------------------------------------------*\
	                          Private Methods
	\*----------------------------------------------------------------------*/

	private void processJar(String jarName) {
		JarFile jar = null;
		try {
			jar = new JarFile(jarName);
			File jarFile = new File(jarName);
			processOpenZip(jar, jarFile, new CustomClassVisitor());
		}

		catch (IOException ex) {
			log.error("Can't open jar file \"" + jarName + "\"", ex);
		}

		finally {
			try {
				if (jar != null)
					jar.close();
			}

			catch (IOException ex) {
				log.error("Can't close " + jarName, ex);
			}

			jar = null;
		}
	}

	private void processZip(String zipName) {
		ZipFile zip = null;

		try {
			zip = new ZipFile(zipName);
			File zipFile = new File(zipName);
			processOpenZip(zip, zipFile, new CustomClassVisitor());
		}

		catch (IOException ex) {
			log.error("Can't open jar file \"" + zipName + "\"", ex);
		}

		finally {
			try {
				if (zip != null)
					zip.close();
			}

			catch (IOException ex) {
				log.error("Can't close " + zipName, ex);
			}

			zip = null;
		}
	}

	private void processOpenZip(ZipFile zip, File zipFile, ClassVisitor classVisitor) {
		String zipName = zipFile.getPath();
		for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
			ZipEntry entry = e.nextElement();

			if ((!entry.isDirectory()) && (entry.getName().toLowerCase().endsWith(".class"))) {
				try {
					log.debug("Loading " + zipName + "(" + entry.getName() + ")");
					loadClassData(zip.getInputStream(entry), classVisitor);
				}

				catch (IOException ex) {
					log.error("Can't open \"" + entry.getName() + "\" in zip file \"" + zipName + "\": ", ex);
				}

				catch (ClassUtilException ex) {
					log.error("Can't open \"" + entry.getName() + "\" in zip file \"" + zipName + "\": ", ex);
				}
			}
		}
	}

	private void processDirectory(File dir) {
		RecursiveFileFinder finder = new RecursiveFileFinder();
		RegexFileFilter nameFilter = new RegexFileFilter("\\.class$", FileFilterMatchType.FILENAME);
		AndFileFilter fileFilter = new AndFileFilter(nameFilter, new FileOnlyFilter());
		Collection<File> files = new ArrayList<File>();
		finder.findFiles(dir, fileFilter, files);

		String path = null;
		InputStream is = null;
		try {
			ClassVisitor classVisitor = new CustomClassVisitor();
			for (File f : files) {
				path = f.getPath();
				log.debug("Loading " + f.getPath());

				is = new FileInputStream(f);
				loadClassData(is, classVisitor);
			}

		} catch (IOException ex) {
			log.error("Can't open \"" + path + "\": ", ex);
		}

		catch (ClassUtilException ex) {
			log.error("Can't open \"" + path + "\": ", ex);
		}

		finally {
			if (is != null) {
				try {
					is.close();
				}

				catch (IOException ex) {
					log.error("Can't close InputStream for \"" + path + "\"", ex);
				}
			}
		}
	}

	private void loadJarClassPathEntries(File jarFile) {
		try {
			JarFile jar = new JarFile(jarFile);
			Manifest manifest = jar.getManifest();
			if (manifest == null)
				return;

			Map map = manifest.getEntries();
			Attributes attrs = manifest.getMainAttributes();
			Set<Object> keys = attrs.keySet();

			for (Object key : keys) {
				String value = (String) attrs.get(key);

				if (key.toString().equals("Class-Path")) {
					String jarName = jar.getName();
					log.debug("Adding Class-Path from jar " + jarName);

					StringBuilder buf = new StringBuilder();
					StringTokenizer tok = new StringTokenizer(value);
					while (tok.hasMoreTokens()) {
						buf.setLength(0);
						String element = tok.nextToken();
						String parent = jarFile.getParent();
						if (parent != null) {
							buf.append(parent);
							buf.append(File.separator);
						}

						buf.append(element);
					}

					String element = buf.toString();
					log.debug("From " + jarName + ": " + element);

					add(new File(element));
				}
			}
		}

		catch (IOException ex) {
			log.error("I/O error processing jar file \"" + jarFile.getPath() + "\"", ex);
		}
	}

	private void loadClassData(InputStream is, ClassVisitor classVisitor) throws ClassUtilException {
		try {
			ClassReader cr = new ClassReader(is);
			cr.accept(classVisitor, CustomClassVisitor.ASM_CR_ACCEPT_CRITERIA);
		}

		catch (Exception ex) {
			throw new ClassUtilException(ClassUtil.BUNDLE_NAME, "ClassFinder.cantReadClassStream",
					"Unable to load class from open " + "input stream", ex);
		}
	}

	private boolean isJar(String fileName) {
		return fileName.toLowerCase().endsWith(".jar");
	}

	private boolean isZip(String fileName) {
		return fileName.toLowerCase().endsWith(".zip");
	}

	private class CustomClassVisitor extends ClassVisitor {

		static final int ASM_CR_ACCEPT_CRITERIA = 0;

		public CustomClassVisitor() {
			super(Opcodes.ASM5);
		}

		public CustomClassVisitor(File classFile) throws ClassUtilException {
			super(Opcodes.ASM5);
			try {
				ClassReader cr = new ClassReader(new FileInputStream(classFile));
				cr.accept(this, ASM_CR_ACCEPT_CRITERIA);
			}

			catch (IOException ex) {
				throw new ClassUtilException(ClassUtil.BUNDLE_NAME, "ClassInfo.cantReadClassFile",
						"Unable to load class file \"{0}\"", new Object[] { classFile.getPath() }, ex);
			}
		}

		/**
		 * Create a <tt>ClassInfo</tt> object from an <tt>InputStream</tt>.
		 *
		 * @param is
		 *            the open <tt>InputStream</tt> containing the class bytes
		 *
		 * @throws ClassUtilException
		 *             load error
		 */
		public CustomClassVisitor(InputStream is) throws ClassUtilException {
			super(Opcodes.ASM5);
			try {
				ClassReader cr = new ClassReader(is);
				cr.accept(this, ASM_CR_ACCEPT_CRITERIA);
			}

			catch (IOException ex) {
				throw new ClassUtilException(ClassUtil.BUNDLE_NAME, "ClassInfo.cantReadClassStream",
						"Unable to load class from open " + "input stream", ex);
			}
		}

		/**
		 * "Visit" a class. Required by ASM <tt>ClassVisitor</tt> interface.
		 *
		 * @param version
		 *            class version
		 * @param access
		 *            class access modifiers, etc.
		 * @param name
		 *            internal class name
		 * @param signature
		 *            class signature
		 * @param superName
		 *            internal super class name
		 * @param interfaces
		 *            internal names of all directly implemented interfaces
		 */
		@Override
		public void visit(int version, int access, String name, String signature, String superName,
				String[] interfaces) {
			foundClasses.add(translateInternalClassName(name));
		}

		/**
		 * Translate an internal class/interface name to an external one.
		 *
		 * @param internalName
		 *            the internal JVM name, from the ASM API
		 *
		 * @return the external name
		 */
		private String translateInternalClassName(String internalName) {
			return internalName.replaceAll("/", ".");
		}

	}
}
