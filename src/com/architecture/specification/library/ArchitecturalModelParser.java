package com.architecture.specification.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import japa.parser.ASTHelper;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class ArchitecturalModelParser {

	ArrayList<File> classFilesRelevantToArchitecture;
	ArchitecturalModel intendedArchitecturalModel;
	ArchitecturalModel perceivedArchitecturalModel;

	public ArchitecturalModelParser(String classFilesDirectory, ArchitecturalModel intendedArchitecturalModel)
			throws IOException {
		classFilesRelevantToArchitecture = new ArrayList<File>();
		this.intendedArchitecturalModel = intendedArchitecturalModel;
		this.perceivedArchitecturalModel = new ArchitecturalModel();

		prepareModelParser(classFilesDirectory);

	}

	private void prepareModelParser(String classFilesDirectory) throws IOException {
		List<File> classFiles = new ArrayList<File>();
		listSourceFiles(new File[] { new File(classFilesDirectory) }, classFiles);

		ClassFilter cf = new ClassFilter();
		ClassReader cr;
		try {
			for (File classFile : classFiles) {
				cr = new ClassReader(new FileInputStream(classFile));
				cr.accept(cf, 0);
				if (cf.isClassRelevantToArchitecture()) {
					classFilesRelevantToArchitecture.add(classFile);
				}
			}
		} catch (IOException e) {
			System.out.println("Encountered a problem while reading the class files!");
			e.printStackTrace();
		}
	}

	private void listSourceFiles(File[] files, List<File> sourceFiles) throws IOException {
		for (File file : files) {
			if (file.isDirectory()) {
				listSourceFiles(file.listFiles(), sourceFiles);
			} else {
				if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("class")) {
					sourceFiles.add(file);
				}
			}
		}

	}

	public void verifyAgainstImplementation() {

		HashMap<String, HashSet<ArchitecturalComponent>> classComponentsMap = intendedArchitecturalModel
				.getClassComponentsMap();

		ClassPrinter cp = new ClassPrinter();
		ClassReader cr;
		try {
			for (File classFile : classFilesRelevantToArchitecture) {
				cr = new ClassReader(new FileInputStream(classFile));
				cr.accept(cp, 0);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Encountered a problem while reading the class files!");
			e.printStackTrace();
		}

		// CompilationUnit cu;
		// FileInputStream in = null;
		// try {
		// for (File sourceFile : sourceFiles) {
		// in = new FileInputStream(sourceFile);
		// cu = JavaParser.parse(in);
		// cu.accept(new SourceCodeVisitor(), null);
		// }
		// } catch (ParseException | IOException e) {
		// System.out.println("Problem while parsing the source files!");
		// } finally {
		// if (in != null)
		// try {
		// in.close();
		// } catch (IOException e) {
		// System.out.println("Problem closing the input stream!");
		// }
		// }

	}

	private class ClassFilter extends ClassVisitor {

		private boolean classRelevantToArchitecture;

		public ClassFilter() {
			super(Opcodes.ASM5);
			this.classRelevantToArchitecture = true;
		}

		public void visit(int version, int access, String name, String signature, String superName,
				String[] interfaces) {
			classRelevantToArchitecture = true;
			if (!classIsPublic(access) || classIsInterface(access) || classIsAbstract(access) || classIsEnum(access))
				classRelevantToArchitecture = false;
		}

		public void visitOuterClass(String owner, String name, String desc) {
			classRelevantToArchitecture = false;
		}

		private boolean classIsPublic(int access) {
			return (access & Opcodes.ACC_PUBLIC) == 0 ? false : true;
		}

		private boolean classIsInterface(int access) {
			return (access & Opcodes.ACC_INTERFACE) == 0 ? false : true;
		}

		private boolean classIsAbstract(int access) {
			return (access & Opcodes.ACC_ABSTRACT) == 0 ? false : true;
		}

		private boolean classIsEnum(int access) {
			return (access & Opcodes.ACC_ENUM) == 0 ? false : true;
		}

		public boolean isClassRelevantToArchitecture() {
			return classRelevantToArchitecture;
		}

	}

	private class ClassPrinter extends ClassVisitor {

		public ClassPrinter() {
			super(Opcodes.ASM5);
		}

		public void visit(int version, int access, String name, String signature, String superName,
				String[] interfaces) {
			System.out.println("className " + name.replace("/", "."));
		}

		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			System.out.println("method " + name);
			return null;
		}

	}

//	/**
//	 * Simple visitor implementation for visiting AST nodes.
//	 */
//	private static class SourceCodeVisitor extends VoidVisitorAdapter<Object> {
//
//		@Override
//		public void visit(final ClassOrInterfaceDeclaration n, Object arg) {
//			// We're interested in Public Concrete classes
//			if (!Modifier.isAbstract(n.getModifiers()) && !Modifier.isInterface(n.getModifiers())
//					&& Modifier.isPublic(n.getModifiers())) {
//				System.out.println(n.getName());
//				for (Object o : HelperUtility.safeList(n.getExtends())) {
//					ClassOrInterfaceType t = (ClassOrInterfaceType) o;
//					try {
//						Class<?> c = Class.forName(t.getName());
//						Class<?> parent = c.getSuperclass();
//						while (parent != null) {
//							Method[] meths = c.getDeclaredMethods();
//							if (meths.length != 0) {
//								for (Method m : meths)
//									System.out.println("inherited method:" + m.toGenericString());
//							} else {
//								System.out.println("  -- no methods --%n");
//							}
//							parent = parent.getSuperclass();
//						}
//					} catch (ClassNotFoundException e) {
//						System.out.println("Source files for parent classes are missing");
//					}
//				}
//			}
//
//			super.visit(n, arg);
//		}
//
//		@Override
//		public void visit(final MethodDeclaration n, Object arg) {
//			if (Modifier.isPublic(n.getModifiers())) {
//				System.out.println(n.getName());
//			}
//			super.visit(n, arg);
//		}
//
//		@Override
//		public void visit(final MethodCallExpr n, Object arg) {
//			System.out.println("method call :" + n);
//		}
//	}
}
