package com.architecture.specification.library.architectural.model.extracted.parser;

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
import java.util.Set;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.synth.SynthSpinnerUI;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.SetUtils.SetView;
import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import com.architecture.specification.library.architectural.model.extracted.ExtractedArchitecturalModel;
import com.architecture.specification.library.architectural.model.extracted.metadata.ArchitecturalComponentImplementationMetaData;
import com.architecture.specification.library.architectural.model.extracted.metadata.ClassMetaData;
import com.architecture.specification.library.architectural.model.extracted.metadata.FieldAccessMetaData;
import com.architecture.specification.library.architectural.model.extracted.metadata.MethodCallMetaData;
import com.architecture.specification.library.architectural.model.extracted.metadata.MethodDeclarationMetaData;
import com.architecture.specification.library.architectural.model.intended.IntendedArchitecturalModel;
import com.architecture.specification.library.architectural.model.intended.communication.link.CommunicationLink;
import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.library.architectural.model.intended.component.BlackboxArchitecturalComponent;
import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterface;
import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterfaceCommunicationSynchronizationType;
import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterfaceCommunicationType;
import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterfaceType;
import com.architecture.specification.library.architectural.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.library.architectural.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.library.exceptions.VerificationException;
import com.architecture.specification.library.util.ClassFinder;
import com.architecture.specification.library.util.JavassistFilterUtility;
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

public class ImplementationParser {

	public ImplementationParser() {
	}

	public List<ClassMetaData> parseImplementationCode(List<String> classFilesToBeVerified, List<String> blackboxClassFiles,
			IntendedArchitecturalModel intendedArchitecturalModel) throws IOException{

		ClassPool pool = ClassPool.getDefault();
		List<ClassMetaData> relevantClassesMetadata = new ArrayList<ClassMetaData>();
		try {
			for (String classFileToBeVerified : classFilesToBeVerified) {
				pool.insertClassPath(classFileToBeVerified);
			}
			for (String blackboxFile : blackboxClassFiles) {
				pool.insertClassPath(blackboxFile);
			}
			JavassistClassFilter classFilter = new AndClassFilter(new NotClassFilter(new EnumClassFilter()), // Must
																												// not
																												// be
																												// an
																												// enum
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
					new NotMethodFilter(new ClassBasedMethodFilter(pool.get("java.lang.Thread"))) // methods
																									// must
																									// be
																									// not
																									// inherited
																									// from
																									// java.lang.Thread
																									// Class
					
					
			);

			ClassFinder cf = new ClassFinder();
			List<CtClass> checkedClasses = new ArrayList<CtClass>();
			for (String classFile : classFilesToBeVerified) {
				cf.add(new File(classFile));
				for (String c : cf.findClasses()) {
					checkedClasses.add(pool.get(c));
				}
			}

			for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getModelComponentsIdentifiersMap().values()) {
				if (architecturalComponent instanceof BlackboxArchitecturalComponent) {
					for (String classname : architecturalComponent.getComponentClasses()) {
						try {
							checkedClasses.add(pool.get(classname));
						} catch (NotFoundException nfe) {
							//do nothing
						}
					}
				}
			}

			List<CtClass> uncheckedClasses = new ArrayList<CtClass>();
			for (String classFile : blackboxClassFiles) {
				cf.add(new File(classFile));
				for (String c : cf.findClasses()) {
					CtClass currentClass = pool.get(c);
					if (!checkedClasses.contains(currentClass)) {
						uncheckedClasses.add(currentClass);
					}
				}
			}

			List<CtClass> filteredCheckedClasses = JavassistFilterUtility.filterClasses(classFilter, checkedClasses);
			List<CtClass> filteredUncheckedClasses = JavassistFilterUtility.filterClasses(classFilter, uncheckedClasses);

			System.out.println("ahoooom");
			for (CtClass ctt : filteredCheckedClasses) {
				System.out.println(ctt.getName());
			}
			
			System.out.println("ahoooom tany");
			for (CtClass ctt : filteredUncheckedClasses) {
				System.out.println(ctt.getName());
			}
			OrMethodFilter filteredClassesMethodBasedFilter = new OrMethodFilter();
			for (CtClass filteredClass : filteredCheckedClasses) {
				filteredClassesMethodBasedFilter.addFilter(new ClassBasedMethodFilter(filteredClass));
			}
			for (CtClass uncheckedBlackBoxClass : filteredUncheckedClasses) {
				filteredClassesMethodBasedFilter.addFilter(new ClassBasedMethodFilter(uncheckedBlackBoxClass));
			}
			JavassistMethodFilter methodCallsFilter = new AndMethodFilter(methodDeclarationFilter, filteredClassesMethodBasedFilter);

			for (CtClass ctClass : filteredCheckedClasses) {
				String fullyQualifiedName = ctClass.getName();

				HashSet<CtClass> recursiveSuperClasses = new HashSet<CtClass>();
				CtClass currentClass = ctClass.getSuperclass();
				while (currentClass != null) {
					recursiveSuperClasses.add(currentClass);
					currentClass = currentClass.getSuperclass();
				}

				HashSet<String> implementedInterfacesNames = new HashSet<String>();
				HashSet<String> superClassesNames = new HashSet<String>();
				for (CtClass implmenetedInterface : ctClass.getInterfaces())
					implementedInterfacesNames.add(implmenetedInterface.getName());
				for (CtClass superClass : recursiveSuperClasses) {
					superClassesNames.add(superClass.getName());
					for (CtClass implmentedInterface : superClass.getInterfaces())
						implementedInterfacesNames.add(implmentedInterface.getName());
				}

				Set<CtMethod> allDeclaredMethods = SetUtils.union(new HashSet<>(Arrays.asList(ctClass.getMethods())),
						new HashSet<>(Arrays.asList(ctClass.getDeclaredMethods())));
				List<CtMethod> filteredDeclaredMethods = JavassistFilterUtility.filterMethods(methodDeclarationFilter, new ArrayList<>(allDeclaredMethods));

				HashSetValuedHashMap<String, MethodDeclarationMetaData> declaredMethodsMap = new HashSetValuedHashMap<String, MethodDeclarationMetaData>();
				HashSetValuedHashMap<String, MethodDeclarationMetaData> providedMethodsMap = new HashSetValuedHashMap<String, MethodDeclarationMetaData>();
				for (CtMethod ctMethod : filteredDeclaredMethods) {
					boolean isMainMethod = false;
					boolean isPublicMethod = false;
					if (new MainMethodFilter().accept(ctMethod))
						isMainMethod = true;

					if (new PublicMethodFilter().accept(ctMethod))
						isPublicMethod = true;

					List<CtMethod> methodCalls = new ArrayList<CtMethod>();
					HashSetValuedHashMap<String, MethodCallMetaData> methodCallsMap = new HashSetValuedHashMap<String, MethodCallMetaData>();
					HashMap<String, FieldAccessMetaData> fieldAccessMap = new HashMap<String, FieldAccessMetaData>();
					try {
						// System.out.println("Declared Method name : " +
						// ctMethod.getName());
						ctMethod.instrument(new ExprEditor() {

							public void edit(MethodCall m) throws CannotCompileException {
								try {
										methodCalls.add(m.getMethod());
								} catch (NotFoundException nfe) {
									System.out.println("Compilation Error while trying to call this method " + m.getMethodName());
								}
							}

							public void edit(FieldAccess f) throws CannotCompileException {
								try {
									String fieldIdentifier = f.getField().getName();
									FieldAccessMetaData fieldAccess = fieldAccessMap.containsKey(fieldIdentifier) ? fieldAccessMap.get(fieldIdentifier)
											: new FieldAccessMetaData(fieldIdentifier, f.getField().getDeclaringClass().getName(),
													f.getField().getType().getName());
									if (f.isReader())
										fieldAccess.setRead(true);
									else
										fieldAccess.setWritten(true);

									fieldAccessMap.put(fieldAccess.getFieldIdentifier(), fieldAccess);
								} catch (NotFoundException e) {
									System.out.println("Compilation Error while trying to access this field " + f.getFieldName());
								}
							}
						});
					} catch (CannotCompileException e) {
						System.out.println("Problem while trying to process the method calls and fields accesses inside a method declaration");
					}
					List<CtMethod> filteredMethodCalls = JavassistFilterUtility.filterMethods(methodCallsFilter, methodCalls);
					for (CtMethod filteredMethodCall : filteredMethodCalls) {
						List<String> methodParameterTypes = new ArrayList<>();
						for (CtClass parameterType : filteredMethodCall.getParameterTypes()) {
							methodParameterTypes.add(parameterType.getName());
						}
						methodCallsMap.put(filteredMethodCall.getName(), new MethodCallMetaData(filteredMethodCall.getName(),
								filteredMethodCall.getDeclaringClass().getName(), filteredMethodCall.getReturnType().getName(), methodParameterTypes));
					}

					List<String> methodParameterTypes = new ArrayList<>();
					for (CtClass parameterType : ctMethod.getParameterTypes()) {
						methodParameterTypes.add(parameterType.getName());
					}
					MethodDeclarationMetaData methodDeclaration = new MethodDeclarationMetaData(ctMethod.getName(), ctMethod.getDeclaringClass().getName(),
							ctMethod.getReturnType().getName(), methodParameterTypes, methodCallsMap, fieldAccessMap);
					declaredMethodsMap.put(methodDeclaration.getMethodIdentifier(), methodDeclaration);
					for (MethodCallMetaData methodCall : methodDeclaration.getMethodCallsMap().values()) {
						methodCall.setCallerMethod(methodDeclaration);
					}

					if (!isMainMethod && isPublicMethod)
						providedMethodsMap.put(methodDeclaration.getMethodIdentifier(), methodDeclaration);

				}

				relevantClassesMetadata
						.add(new ClassMetaData(fullyQualifiedName, declaredMethodsMap, providedMethodsMap, implementedInterfacesNames, superClassesNames));
			}

			// for (ArchitecturalComponentImplementationMetaData ac :
			// implementedArchitecturalModel.getActualImplementedComponents().values())
			// {
			// for (ClassMetaData c :
			// ac.getComponentImplementedClasses().values()) {
			// System.out.println("class name : " + c.getFullyQualifiedName());
			// System.out.println("declared methods : ");
			// for (String s : c.getDeclaredMethodsMap().keySet()) {
			// System.out.println(s);
			// }
			// }
			// }

		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return relevantClassesMetadata;
	}

}