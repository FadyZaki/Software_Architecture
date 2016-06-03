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

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import com.architecture.specification.library.architectural.model.component.ArchitecturalComponent;
import com.architecture.specification.library.architectural.model.component.BlackboxArchitecturalComponent;
import com.architecture.specification.library.exceptions.ComponentClassNotFoundException;
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
import com.architecture.specification.library.util.methodfilter.MainMethodFilter;
import com.architecture.specification.library.util.methodfilter.NotMethodFilter;
import com.architecture.specification.library.util.methodfilter.OrMethodFilter;
import com.architecture.specification.library.util.methodfilter.PublicMethodFilter;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

public class ArchitecturalModelParser {

	List<ClassMetaData> classesRelevantToArchitecture;
	ArchitecturalModel intendedArchitecturalModel;

	public ArchitecturalModelParser(List<String> classFilesToBeVerified, List<String> blackboxClassFiles, ArchitecturalModel intendedArchitecturalModel)
			throws IOException, ComponentClassNotFoundException {
		classesRelevantToArchitecture = new ArrayList<ClassMetaData>();
		this.intendedArchitecturalModel = intendedArchitecturalModel;

		prepareModelParser(classFilesToBeVerified, blackboxClassFiles);

	}

	private void prepareModelParser(List<String> classFilesToBeVerified, List<String> blackboxClassFiles) throws IOException, ComponentClassNotFoundException {
		ClassPool pool = ClassPool.getDefault();
		try {
			for (String classFileToBeVerified : classFilesToBeVerified) {
				pool.insertClassPath(classFileToBeVerified);
			}
			for (String blackboxFile : blackboxClassFiles) {
				pool.insertClassPath(blackboxFile);
			}
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

			JavassistMethodFilter methodDeclarationFilter = new AndMethodFilter(new NotMethodFilter(new ClassBasedMethodFilter(pool.get("java.lang.Object"))), // methods
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
			List<CtClass> foundClasses = new ArrayList<CtClass>();
			for (String classFile : classFilesToBeVerified) {
				cf.add(new File(classFile));
				for (String c : cf.findClasses()) {
					foundClasses.add(pool.get(c));
				}
			}

			for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getModelComponentsIdentifiersMap().values()) {
				if (architecturalComponent instanceof BlackboxArchitecturalComponent) {
					for (String classname : architecturalComponent.getComponentClasses()) {
						try {
							foundClasses.add(pool.get(classname));
						} catch (NotFoundException nfe) {
							throw new ComponentClassNotFoundException(classname);
						}
					}
				}
			}

			List<CtClass> filteredClasses = JavassistUtilityFilter.filterClasses(classFilter, foundClasses);
			OrMethodFilter filteredClassesMethodBasedFilter = new OrMethodFilter();
			for (CtClass filteredClass : filteredClasses) {
				filteredClassesMethodBasedFilter.addFilter(new ClassBasedMethodFilter(filteredClass));
			}
			JavassistMethodFilter methodCallsFilter = new AndMethodFilter(methodDeclarationFilter, filteredClassesMethodBasedFilter);
			HashSet<ClassMetaData> classMetaDataSet = new HashSet<ClassMetaData>();
			for (CtClass ctClass : filteredClasses) {
				String fullyQualifiedName = ctClass.getName();

				List<CtMethod> filteredDeclaredMethods = JavassistUtilityFilter.filterMethods(methodDeclarationFilter, Arrays.asList(ctClass.getMethods()));
				HashSet<String> providedMethods = new HashSet<String>();
				for (CtMethod ctMethod : filteredDeclaredMethods) {
					if (new NotMethodFilter(new MainMethodFilter()).accept(ctMethod)) // don't
																						// add
																						// the
																						// main
																						// method
																						// as
																						// a
																						// provided
																						// method
						providedMethods.add(ctMethod.getName());
				}

				HashSetValuedHashMap<String, String> requiredMethodsCommunicationsMap = new HashSetValuedHashMap<String, String>();
				for (CtMethod ctMethod : filteredDeclaredMethods) {
					List<CtMethod> methodCalls = new ArrayList<CtMethod>();
					try {
						ctMethod.instrument(new ExprEditor() {
							public void edit(MethodCall m) throws CannotCompileException {
								try {
									methodCalls.add(m.getMethod());
								} catch (NotFoundException e) {
									System.out.println("Compilation Error through this method call " + m.getMethodName());
								}
							}
						});
					} catch (CannotCompileException e) {
						System.out.println("Problem while trying to process the method calls inside a method declaration");
						e.printStackTrace();
					}
					List<CtMethod> filteredMethodCalls = JavassistUtilityFilter.filterMethods(methodCallsFilter, methodCalls);
					for (CtMethod filteredMethodCall : filteredMethodCalls) {
						requiredMethodsCommunicationsMap.put(filteredMethodCall.getName(), filteredMethodCall.getDeclaringClass().getName());
					}
				}
				classMetaDataSet.add(new ClassMetaData(fullyQualifiedName, providedMethods, requiredMethodsCommunicationsMap));
			}

			for (ClassMetaData c : classMetaDataSet) {
				System.out.println("class name : " + c.getClassFullyQualifiedName());
				System.out.println("provided methods : ");
				for (String s : c.getProvidedMethods()) {
					System.out.println(s);
				}
				System.out.println(c.getRequiredMethodsCommunicationsMap().toString());
			}

		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void verifyAgainstImplementation() {

		HashMap<String, HashSet<ArchitecturalComponent>> classComponentsMap = intendedArchitecturalModel.getClassComponentsMap();

	}

}
