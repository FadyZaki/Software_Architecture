package com.architecture.specification.library.architectural.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.architecture.specification.library.architectural.model.component.ArchitecturalComponent;
import com.architecture.specification.library.util.ClassFinder;
import com.architecture.specification.library.util.ClassMetaData;
import com.architecture.specification.library.util.JavassistUtilityFilter;
import com.architecture.specification.library.util.classfilter.AbstractClassFilter;
import com.architecture.specification.library.util.classfilter.AndClassFilter;
import com.architecture.specification.library.util.classfilter.EnumClassFilter;
import com.architecture.specification.library.util.classfilter.InnerClassFilter;
import com.architecture.specification.library.util.classfilter.InterfaceClassFilter;
import com.architecture.specification.library.util.classfilter.JavassistClassFilter;
import com.architecture.specification.library.util.classfilter.NotClassFilter;
import com.architecture.specification.library.util.classfilter.PublicClassFilter;
import com.architecture.specification.library.util.methodfilter.AndMethodFilter;
import com.architecture.specification.library.util.methodfilter.ClassBasedMethodFilter;
import com.architecture.specification.library.util.methodfilter.JavassistMethodFilter;
import com.architecture.specification.library.util.methodfilter.NotMethodFilter;
import com.architecture.specification.library.util.methodfilter.PublicMethodFilter;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ArchitecturalModelParser {

	List<ClassMetaData> classesRelevantToArchitecture;
	ArchitecturalModel intendedArchitecturalModel;

	public ArchitecturalModelParser(String classFilesDirectory, ArchitecturalModel intendedArchitecturalModel)
			throws IOException {
		classesRelevantToArchitecture = new ArrayList<ClassMetaData>();
		this.intendedArchitecturalModel = intendedArchitecturalModel;

		prepareModelParser(classFilesDirectory);

	}

	private void prepareModelParser(String classFilesDirectory) throws IOException {
		ClassPool pool = ClassPool.getDefault();
		try {
			pool.insertClassPath(classFilesDirectory);
			JavassistClassFilter classFilter = new AndClassFilter(new NotClassFilter(new InterfaceClassFilter()), // Must
																													// not
																													// be
																													// an
																													// interface
					new NotClassFilter(new EnumClassFilter()), // Must not be an
																// enum
					new NotClassFilter(new AbstractClassFilter()), // Must not
																	// be
																	// abstract
					new NotClassFilter(new InnerClassFilter()), // Must not be
																// an inner
																// class
					new PublicClassFilter()); // Must be a public class

			JavassistMethodFilter methodFilter = new AndMethodFilter(
					new NotMethodFilter(new ClassBasedMethodFilter(pool.get("java.lang.Object"))), // methods
																									// must
																									// be
																									// not
																									// inherited
																									// from
																									// java.lang.Object
																									// Class
					new NotMethodFilter(new ClassBasedMethodFilter(pool.get("java.lang.Thread"))), // methods
																									// must
																									// be
																									// not
																									// inherited
																									// from
																									// java.lang.Thread
																									// Class
					new PublicMethodFilter()); // Must be a public method

			ClassFinder cf = new ClassFinder();
			cf.add(new File(classFilesDirectory));
			List<CtClass> foundClasses = new ArrayList<CtClass>();
			for (String c : cf.findClasses()) {
				foundClasses.add(pool.get(c));
			}
			List<CtClass> filteredClasses = JavassistUtilityFilter.filterClasses(classFilter, foundClasses);
			for (CtClass ctClass : filteredClasses) {
				System.out.println("Class " + ctClass.getName());
				List<CtMethod> classProvidedMethods = JavassistUtilityFilter.filterMethods(methodFilter,
						Arrays.asList(ctClass.getMethods()));
				for (CtMethod ctMethod : classProvidedMethods) {
					// System.out.println("Method Declared : " +
					// ctMethod.getName());
					try {
						ctMethod.instrument(new ExprEditor() {
							public void edit(MethodCall m) throws CannotCompileException {
								System.out.println("Method call class name " + m.getClassName() + "\nMethod Call name "
										+ m.getMethodName());
							}
						});
					} catch (CannotCompileException e) {
						System.out.println("Problem which trying to process the method calls inside a method declaration");
						e.printStackTrace();
					}
				}
			}

		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void verifyAgainstImplementation() {

		HashMap<String, HashSet<ArchitecturalComponent>> classComponentsMap = intendedArchitecturalModel
				.getClassComponentsMap();

	}

}
