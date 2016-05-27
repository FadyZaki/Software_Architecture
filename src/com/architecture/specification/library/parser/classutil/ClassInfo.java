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

package com.architecture.specification.library.parser.classutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Modifier;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

/**
 * <p>Holds information about a loaded class in a way that doesn't rely on
 * the underlying API used to load the class information.</p>
 *
 * <p>This class relies on the ASM byte-code manipulation library. If that
 * library is not available, this package will not work. See
 * <a href="http://asm.objectweb.org"><i>asm.objectweb.org</i></a> for
 * details on ASM.</p>
 *
 * @version <tt>$Revision$</tt>
 *
 * @author Copyright &copy; 2006 Brian M. Clapper
 */
public class ClassInfo extends EmptyVisitor
{
    static int ASM_CR_ACCEPT_CRITERIA = 0;

    private int access = 0;
    private String className = null;
    private String superClassName = null;
    private String outerClassName = null;
    private String[] implementedInterfaces = null;
    private File locationFound = null;
    private Set<FieldInfo> fields = new HashSet<FieldInfo>();
    private Set<MethodInfo> methods = new HashSet<MethodInfo>();

    /**
     * Create a <tt>ClassInfo</tt> object from a file.
     *
     * @param classFile  the abstract path to the class file to load
     *
     * @throws ClassUtilException load error
     */
    public ClassInfo(File classFile) throws ClassUtilException
    {
        try
        {
            ClassReader cr = new ClassReader(new FileInputStream(classFile));
            cr.accept(this, ASM_CR_ACCEPT_CRITERIA);
        }

        catch (IOException ex)
        {
            throw new ClassUtilException(ClassUtil.BUNDLE_NAME,
                                         "ClassInfo.cantReadClassFile",
                                         "Unable to load class file \"{0}\"",
                                         new Object[] {classFile.getPath()},
                                         ex);
        }
    }

    /**
     * Create a <tt>ClassInfo</tt> object from an <tt>InputStream</tt>.
     *
     * @param is  the open <tt>InputStream</tt> containing the class bytes
     *
     * @throws ClassUtilException load error
     */
    public ClassInfo(InputStream is) throws ClassUtilException
    {
        try
        {
            ClassReader cr = new ClassReader(is);
            cr.accept(this, ASM_CR_ACCEPT_CRITERIA);
        }

        catch (IOException ex)
        {
            throw new ClassUtilException(ClassUtil.BUNDLE_NAME,
                                         "ClassInfo.cantReadClassStream",
                                         "Unable to load class from open " +
                                         "input stream",
                                         ex);
        }
    }

    /**
     * Create a new <tt>ClassInfo</tt> object.
     *
     * @param name           the class name
     * @param superClassName the parent class name, or null
     * @param interfaces     the names of interfaces the class implements,
     *                       or null
     * @param asmAccessMask  ASM API's access mask for the class
     * @param location       File (jar, zip) or directory where class was found
     */
    ClassInfo(String name,
              String superClassName,
              String outerClassName,
              String[] interfaces,
              int asmAccessMask,
              File location)
    {
        setClassFields(name, superClassName, outerClassName, interfaces, asmAccessMask, location);
    }

    /*----------------------------------------------------------------------*\
                              Public Methods
    \*----------------------------------------------------------------------*/

    /**
     * Get the class name.
     *
     * @return the class name
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Get the parent (super) class name, if any. Returns null if the
     * superclass is <tt>java.lang.Object</tt>. Note: To find other
     * ancestor classes, use {@link ClassFinder#findAllSuperClasses}.
     *
     * @return the super class name, or null
     *
     * @see ClassFinder#findAllSuperClasses
     */
    public String getSuperClassName()
    {
        return superClassName;
    }
    
    /**
     * Get the owner (outer) class name, if any. Returns null if the
     * class is not an inner class
     *
     * @return the outer class name, or null
     */
    public String getOuterClassName()
    {
        return outerClassName;
    }

    /**
     * Get the names of all <i>directly</i> implemented interfaces. To find
     * indirectly implemented interfaces, use
     * {@link ClassFinder#findAllInterfaces}.
     *
     * @return an array of the names of all directly implemented interfaces,
     *         or null if there are none
     *
     * @see ClassFinder#findAllInterfaces
     */
    public String[] getInterfaces()
    {
        return implementedInterfaces;
    }

    /**
     * @return the ASM access
     */
    public int getAccess()
    {
        return access;
    }

    /**
     * Get the location (the jar file, zip file or directory) where the
     * class was found.
     *
     * @return where the class was found
     */
    public File getClassLocation()
    {
        return locationFound;
    }

    /**
     * Get the set of fields in the class.
     *
     * @return the set of fields, if any.
     */
    public Set<FieldInfo> getFields()
    {
        return fields;
    }

    /**
     * Get the set of methods in the class.
     *
     * @return the set of methods, if any
     */
    public Set<MethodInfo> getMethods()
    {
        return methods;
    }

    /**
     * Get a string representation of this object.
     *
     * @return the string representation
     */
    public String toString()
    {
        StringBuilder buf = new StringBuilder();

        if ((access & Opcodes.ACC_PUBLIC) != 0)
            buf.append ("public ");

        if ((access & Opcodes.ACC_ABSTRACT) != 0)
            buf.append ("abstract ");

        if ((access & Opcodes.ACC_INTERFACE) != 0)
            buf.append ("interface ");
        
        if ((access & Opcodes.ACC_ENUM) != 0)
            buf.append ("enum ");
        
        else
            buf.append ("class ");

        buf.append (className);

        String sep = " ";
        if (implementedInterfaces.length > 0)
        {
            buf.append (" implements");
            for (String intf : implementedInterfaces)
            {
                buf.append (sep);
                buf.append (intf);
            }
        }

        if ((superClassName != null) &&
            (! superClassName.equals ("java.lang.Object")))
        {
            buf.append (sep);
            buf.append ("extends ");
            buf.append (superClassName);
        }

        return (buf.toString());
    }

    /*----------------------------------------------------------------------*\
                  Public Methods Required by ClassVisitor
    \*----------------------------------------------------------------------*/

    /**
     * "Visit" a class. Required by ASM <tt>ClassVisitor</tt> interface.
     *
     * @param version     class version
     * @param access      class access modifiers, etc.
     * @param name        internal class name
     * @param signature   class signature (not used here)
     * @param superName   internal super class name
     * @param interfaces  internal names of all directly implemented
     *                    interfaces
     */
    @Override
    public void visit(int      version,
                      int      access,
                      String   name,
                      String   signature,
                      String   superName,
                      String[] interfaces)
    {
        setClassFields(name, superName, null, interfaces, access, null);
    }
    
    
    /**
     * "Visit" the outer class.
     *
     * @param owner field outer class
     * @param name field name
     * @param desc field descendants
     *
     * @return null.
     */
    @Override
	public void visitOuterClass(String owner, String name, String desc) {
    	this.outerClassName = owner;
	}

    /**
     * "Visit" a field.
     *
     * @param access      field access modifiers, etc.
     * @param name        field name
     * @param description field description
     * @param signature   field signature
     * @param value       field value, if any
     *
     * @return null.
     */
    @Override
    public FieldVisitor visitField(int access,
                                   String name,
                                   String description,
                                   String signature,
                                   Object value)
    {
        fields.add(new FieldInfo(access,
                                 name,
                                 description,
                                 signature,
                                 value));
        return null;
    }

    /**
     * "Visit" a method.
     *
     * @param access      field access modifiers, etc.
     * @param name        field name
     * @param description field description
     * @param signature   field signature
     * @param exceptions  list of exception names the method throws
     *
     * @return null.
     */
    @Override
    public MethodVisitor visitMethod(int access,
                                     String name,
                                     String description,
                                     String signature,
                                     String[] exceptions)
    {
        methods.add(new MethodInfo(access,
                                   name,
                                   description,
                                   signature,
                                   exceptions));
        return null;
    }

    /*----------------------------------------------------------------------*\
                              Private Methods
    \*----------------------------------------------------------------------*/

    /**
     * Translate an internal class/interface name to an external one.
     *
     * @param internalName the internal JVM name, from the ASM API
     *
     * @return the external name
     */
    private String translateInternalClassName(String internalName)
    {
        return internalName.replaceAll("/", ".");
    }

    /**
     * Set the fields in this object.
     *
     * @param name           the class name
     * @param superClassName the parent class name, or null
     * @param interfaces     the names of interfaces the class implements,
     *                       or null
     * @param asmAccessMask  ASM API's access mask for the class
     * @param location       File (jar, zip) or directory where class was found
     */
    private void setClassFields(String name,
                                String superClassName,
                                String outerClassName,
                                String[] interfaces,
                                int asmAccessMask,
                                File location)
    {
        this.className = translateInternalClassName(name);
        this.locationFound = location;

        if ((superClassName != null) &&
             (! superClassName.equals ("java/lang/Object")))
        {
            this.superClassName = translateInternalClassName(superClassName);
        }
        
        if (outerClassName != null) {
        	this.outerClassName = outerClassName;
        }

        if (interfaces != null)
        {
            this.implementedInterfaces = new String[interfaces.length];
            for (int i = 0; i < interfaces.length; i++)
            {
                this.implementedInterfaces[i] =
                    translateInternalClassName(interfaces[i]);
            }
        }

        access = asmAccessMask;
    }
}
