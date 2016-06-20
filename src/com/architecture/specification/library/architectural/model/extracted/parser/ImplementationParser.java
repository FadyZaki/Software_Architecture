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
import com.architecture.specification.library.util.methodfilter.AddObserverMethodFilter;
import com.architecture.specification.library.util.methodfilter.AndMethodFilter;
import com.architecture.specification.library.util.methodfilter.ClassListBasedMethodFilter;
import com.architecture.specification.library.util.methodfilter.JavassistMethodFilter;
import com.architecture.specification.library.util.methodfilter.MainMethodFilter;
import com.architecture.specification.library.util.methodfilter.NotMethodFilter;
import com.architecture.specification.library.util.methodfilter.NotifyObserversMethodFilter;
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

/**
 * This class is responsible for parsing the class files and generating
 * useful metadata that can be used by the model extractor to extract the
 * implemented model
 */
public class ImplementationParser {

	List<ClassMetaData> veriafiableClassesMetadata;
	List<ClassMetaData> blackboxClassesMetadata;

	public ImplementationParser() {
		veriafiableClassesMetadata = new ArrayList<ClassMetaData>();
		blackboxClassesMetadata = new ArrayList<ClassMetaData>();
	}

	/**
	 * This method is responsible for parsing the class files and generating
	 * useful metadata that can be used by the model extractor to extract the
	 * implemented model
	 * @param veriafiableClassFilesDirectories directory containing all verifiable class files
	 * @param blackboxClassFilesDirectories directory containing all blackbox class files
	 * @param uncheckedClassFiles directory containing all unchecked class files
	 * @throws IOException
	 */
	public void parseImplementationCode(List<String> veriafiableClassFilesDirectories, List<String> blackboxClassFilesDirectories,
			List<String> uncheckedClassFiles) throws IOException {

		ClassPool pool = ClassPool.getDefault();

		try {
			for (String classFileToBeVerified : veriafiableClassFilesDirectories) {
				pool.insertClassPath(classFileToBeVerified);
			}
			for (String blackboxFile : blackboxClassFilesDirectories) {
				pool.insertClassPath(blackboxFile);
			}
			for (String uncheckedFile : uncheckedClassFiles) {
				pool.insertClassPath(uncheckedFile);
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

			ClassFinder cf = new ClassFinder();
			HashSet<String> verifiableClassFiles = new HashSet<String>();
			HashSet<String> blackboxClassFiles = new HashSet<String>();
			List<CtClass> checkedClasses = new ArrayList<CtClass>();
			for (String classFileDirectory : veriafiableClassFilesDirectories) {
				cf.add(new File(classFileDirectory));
				for (String c : cf.findClasses()) {
					checkedClasses.add(pool.get(c));
					verifiableClassFiles.add(c);
				}
			}

			for (String classFileDirectory : blackboxClassFilesDirectories) {
				cf.add(new File(classFileDirectory));
				for (String c : cf.findClasses()) {
					checkedClasses.add(pool.get(c));
					blackboxClassFiles.add(c);
				}
			}

			List<CtClass> filteredCheckedClasses = JavassistFilterUtility.filterClasses(classFilter, checkedClasses);

			List<CtClass> uncheckedClasses = new ArrayList<CtClass>();
			for (String classFile : uncheckedClassFiles) {
				cf.add(new File(classFile));
				for (String c : cf.findClasses()) {
					CtClass currentClass = pool.get(c);
					if (!checkedClasses.contains(currentClass)) {
						uncheckedClasses.add(currentClass);
					}
				}

				uncheckedClasses.addAll(JavassistFilterUtility.filterClasses(new NotClassFilter(classFilter), checkedClasses));
			}

			JavassistMethodFilter methodDeclarationsFilter = new NotMethodFilter(new ClassListBasedMethodFilter(uncheckedClasses));
			JavassistMethodFilter methodCallsFilter = new OrMethodFilter(methodDeclarationsFilter, new NotifyObserversMethodFilter(), new AddObserverMethodFilter());

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
				List<CtMethod> filteredDeclaredMethods = JavassistFilterUtility.filterMethods(methodDeclarationsFilter, new ArrayList<>(allDeclaredMethods));

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

				ClassMetaData cmd = new ClassMetaData(fullyQualifiedName, declaredMethodsMap, providedMethodsMap, implementedInterfacesNames,
						superClassesNames);
				if (verifiableClassFiles.contains(cmd.getFullyQualifiedName()))
					veriafiableClassesMetadata.add(cmd);
				else
					blackboxClassesMetadata.add(cmd);
			}


		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<ClassMetaData> getVeriafiableClassesMetadata() {
		return veriafiableClassesMetadata;
	}

	public List<ClassMetaData> getBlackboxClassesMetadata() {
		return blackboxClassesMetadata;
	}

}