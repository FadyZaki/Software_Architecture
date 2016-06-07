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
import java.util.Set;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.synth.SynthSpinnerUI;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.SetUtils.SetView;
import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import com.architecture.specification.library.architectural.implementation.metadata.ArchitecturalComponentImplementationMetaData;
import com.architecture.specification.library.architectural.implementation.metadata.ClassMetaData;
import com.architecture.specification.library.architectural.implementation.metadata.FieldAccessMetaData;
import com.architecture.specification.library.architectural.implementation.metadata.ArchitecturalModelImplementation;
import com.architecture.specification.library.architectural.implementation.metadata.MethodCallMetaData;
import com.architecture.specification.library.architectural.implementation.metadata.MethodDeclarationMetaData;
import com.architecture.specification.library.architectural.model.communication.link.CommunicationLink;
import com.architecture.specification.library.architectural.model.component.ArchitecturalComponent;
import com.architecture.specification.library.architectural.model.component.BlackboxArchitecturalComponent;
import com.architecture.specification.library.architectural.model.portinterface.PortInterface;
import com.architecture.specification.library.architectural.model.portinterface.PortInterfaceCommunicationSynchronizationType;
import com.architecture.specification.library.architectural.model.portinterface.PortInterfaceCommunicationType;
import com.architecture.specification.library.architectural.model.portinterface.PortInterfaceType;
import com.architecture.specification.library.architectural.model.portinterface.ProvidedPortInterface;
import com.architecture.specification.library.architectural.model.portinterface.RequiredPortInterface;
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

public class ArchitecturalModelParser {

	ArchitecturalModel intendedArchitecturalModel;
	ArchitecturalModelImplementation implementedArchitecturalModel;

	public ArchitecturalModelParser(List<String> classFilesToBeVerified, List<String> blackboxClassFiles, ArchitecturalModel intendedArchitecturalModel)
			throws IOException, VerificationException {
		this.intendedArchitecturalModel = intendedArchitecturalModel;
		this.implementedArchitecturalModel = new ArchitecturalModelImplementation();
		prepareModelParser(classFilesToBeVerified, blackboxClassFiles);
	}

	private void prepareModelParser(List<String> classFilesToBeVerified, List<String> blackboxClassFiles) throws IOException, VerificationException {
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
							throw new VerificationException("This class " + classname
									+ " is available in architecture specification but was never defined as part of the implementation");
						}
					}
				}
			}

			List<CtClass> filteredClasses = JavassistFilterUtility.filterClasses(classFilter, foundClasses);

			OrMethodFilter filteredClassesMethodBasedFilter = new OrMethodFilter();
			for (CtClass filteredClass : filteredClasses) {
				filteredClassesMethodBasedFilter.addFilter(new ClassBasedMethodFilter(filteredClass));
			}
			JavassistMethodFilter methodCallsFilter = new AndMethodFilter(methodDeclarationFilter, filteredClassesMethodBasedFilter);
			for (CtClass ctClass : filteredClasses) {
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
								} catch (NotFoundException e) {
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

				if (!intendedArchitecturalModel.getClassComponentsMap().containsKey(fullyQualifiedName)) {
					throw new VerificationException(
							"This class is available in implementation but was never defined as part of the architecture specification " + fullyQualifiedName);
				} else {
					ClassMetaData cmd = new ClassMetaData(fullyQualifiedName, declaredMethodsMap, providedMethodsMap, implementedInterfacesNames,
							superClassesNames);
					System.out.println("class name : " + cmd.getFullyQualifiedName());
					System.out.println("declared methods : ");
					for (MethodDeclarationMetaData mmd : cmd.getDeclaredMethodsMap().values())
						System.out.println(mmd.getMethodIdentifier() + " " + mmd.getMethodDeclaringClass() + " " + mmd.getMethodReturnType());
					for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getClassComponentsMap().get(fullyQualifiedName)) {
						String componentIdentifier = architecturalComponent.getComponentIdentifier();
						ArchitecturalComponentImplementationMetaData currentComponentMetaData = implementedArchitecturalModel.getActualImplementedComponents()
								.containsKey(componentIdentifier) ? implementedArchitecturalModel.getActualImplementedComponents().get(componentIdentifier)
										: new ArchitecturalComponentImplementationMetaData(componentIdentifier);
						currentComponentMetaData.getComponentImplementedClasses().put(cmd.getFullyQualifiedName(), cmd);
					}
				}
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

	}

	public void verifyAgainstImplementation() throws VerificationException {

		// Consistency checking between classes in the architecture
		// specification and the code implementing it
		Set<String> actualClassesNames = implementedArchitecturalModel.getComponentsClassesNames();
		Set<String> intendedClassesNames = intendedArchitecturalModel.getClassComponentsMap().keySet();

		SetView<String> availableClassesNotIntended = SetUtils.difference(actualClassesNames, intendedClassesNames);
		if (!availableClassesNotIntended.isEmpty())
			throw new VerificationException("These classes are available in implementation but are never defined as part of the architecture specification "
					+ availableClassesNotIntended.toString());

		SetView<String> intendedClassesNotAvailable = SetUtils.difference(intendedClassesNames, actualClassesNames);
		if (!intendedClassesNotAvailable.isEmpty())
			throw new VerificationException("These classes are intended in the architecture specification but are not available as part of the implementation"
					+ intendedClassesNotAvailable.toString());

		// Consistency checking between the providedPorts in the architecture
		// specification and the code implementing it
		for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getModelComponentsIdentifiersMap().values()) {

			HashSetValuedHashMap<String, MethodDeclarationMetaData> componentDeclaredMethods = implementedArchitecturalModel.getActualImplementedComponents()
					.get(architecturalComponent.getComponentIdentifier()).getComponentDeclaredMethods();
			HashSetValuedHashMap<String, MethodDeclarationMetaData> componentProvidedMethods = implementedArchitecturalModel.getActualImplementedComponents()
					.get(architecturalComponent.getComponentIdentifier()).getComponentProvidedMethods();
			HashSetValuedHashMap<String, MethodCallMetaData> componentRequiredMethods = implementedArchitecturalModel.getActualImplementedComponents()
					.get(architecturalComponent.getComponentIdentifier()).getComponentRequiredMethodsMap();
			ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent = implementedArchitecturalModel.getActualImplementedComponents()
					.get(architecturalComponent.getComponentIdentifier());

			Set<MethodDeclarationMetaData> intendedComponentProvidedMethods = new HashSet<MethodDeclarationMetaData>();
			for (ProvidedPortInterface p : architecturalComponent.getProvidedInterfaces()) {

				switch (p.getPortInterfaceCommunicationType()) {
				case TASK_EXECUTION:
					intendedComponentProvidedMethods.addAll(verifyProvidedTaskExecution(implementedArchitecturalComponent, p));
					break;
				case SHARED_DATA:
					intendedComponentProvidedMethods.addAll(verifyProvidedSharedData(implementedArchitecturalComponent, p));
					break;
				case MESSAGE_PASSING:
					intendedComponentProvidedMethods.addAll(verifyProvidedMessagePassing(implementedArchitecturalComponent, p));
					break;
				default:
					break;
				}

			}

			SetView<MethodDeclarationMetaData> availableMethodsNotIntended = SetUtils.difference(new HashSet<>(componentProvidedMethods.values()),
					intendedComponentProvidedMethods);
			if (!availableMethodsNotIntended.isEmpty())
				throw new VerificationException(
						"These public methods are available in the implementation but are never defined as part of the architecture specification"
								+ availableMethodsNotIntended.toString());

			// Consistency checking of required ports
			for (RequiredPortInterface r : architecturalComponent.getRequiredInterfaces()) {
				switch (r.getPortInterfaceCommunicationType()) {
				case TASK_EXECUTION:
					verifyRequiredTaskExecution(implementedArchitecturalComponent, r);
					break;
				case SHARED_DATA:
					verifyRequiredSharedData(implementedArchitecturalComponent, r);
					break;
				case MESSAGE_PASSING:
					verifyRequiredMessagePassing(implementedArchitecturalComponent, r);
					break;
				default:
					break;
				}
			}
		}

		// Consistency checking of communication links specified in the
		// architectural model being fulfilled by the implementation code
		for (CommunicationLink communicationLink : intendedArchitecturalModel.getCommunicationLinks()) {
			ArchitecturalComponentImplementationMetaData providingComponent;
			ArchitecturalComponentImplementationMetaData requiringComponent;
			if(communicationLink.getInnermostProvidingComponent() != null)
				providingComponent = implementedArchitecturalModel.getActualImplementedComponents().get(communicationLink.getInnermostProvidingComponent());
			else
				providingComponent = implementedArchitecturalModel.getActualImplementedComponents().get(communicationLink.getProvidingComponent());
			
			requiringComponent = implementedArchitecturalModel.getActualImplementedComponents().get(communicationLink.getRequiringComponent());
			
//			switch (communicationLink.getRequiredPortInterface().getPortInterfaceCommunicationType()) {
//			case TASK_EXECUTION:
//				verifyRequiredTaskExecution(implementedArchitecturalComponent, r);
//				break;
//			case SHARED_DATA:
//				verifyRequiredSharedData(implementedArchitecturalComponent, r);
//				break;
//			case MESSAGE_PASSING:
//				verifyRequiredMessagePassing(implementedArchitecturalComponent, r);
//				break;
//			default:
//				break;
//			}
		}

	}
	
	private Set<MethodDeclarationMetaData> verifyProvidedTaskExecution(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent,
			ProvidedPortInterface p) throws VerificationException {
		Set<MethodDeclarationMetaData> providedMethodsUsedByThisTaskExecutionPort = new HashSet<MethodDeclarationMetaData>();
		if (!implementedArchitecturalComponent.getComponentProvidedMethods().containsKey(p.getPortInterfaceIdentifier()))
			throw new VerificationException("The intended 'Task Execution' port provided by " + implementedArchitecturalComponent.getComponentIdentifier()
					+ " is not part of the implementation!");

		Set<MethodDeclarationMetaData> providedPortActualMethods = implementedArchitecturalComponent.getComponentProvidedMethods()
				.get(p.getPortInterfaceIdentifier());
		if (p.getPortInterfaceCommunicationSynchronizationType() == PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK) {
			boolean methodWithFutureReturnTypeIsAvailable = false;
			for (MethodDeclarationMetaData mmd : providedPortActualMethods) {
				if (mmd.getMethodReturnType().equals("java.util.concurrent.Future")) {
					methodWithFutureReturnTypeIsAvailable = true;
					providedMethodsUsedByThisTaskExecutionPort.add(mmd);
				}
			}

			if (!methodWithFutureReturnTypeIsAvailable)
				throw new VerificationException("The intended 'Asynchronous Task Execution' port provided by "
						+ implementedArchitecturalComponent.getComponentIdentifier() + " is not part of the implementation!");
		} else {
			providedMethodsUsedByThisTaskExecutionPort.addAll(providedPortActualMethods);
		}

		return providedMethodsUsedByThisTaskExecutionPort;

	}
	
	private void verifyRequiredTaskExecution(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent, RequiredPortInterface r)
			throws VerificationException {
		if (!implementedArchitecturalComponent.getComponentRequiredMethodsMap().containsKey(r.getPortInterfaceIdentifier()))
			throw new VerificationException("The intended 'Task Execution' port required by " + implementedArchitecturalComponent.getComponentIdentifier()
					+ " is not part of the implementation!");

		Set<MethodCallMetaData> requiredPortActualMethods = implementedArchitecturalComponent.getComponentRequiredMethodsMap()
				.get(r.getPortInterfaceIdentifier());
		if (r.getPortInterfaceCommunicationSynchronizationType() == PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK) {
			boolean methodWithFutureReturnTypeIsProperlyCalled = true;
			for (MethodCallMetaData mmd : requiredPortActualMethods) {
				if (!mmd.getMethodReturnType().equals("java.util.concurrent.Future")) {
					methodWithFutureReturnTypeIsProperlyCalled = false;
					break;
				}
				if (!recursiveCheckForMethodCallWithinMethodDeclaration(mmd.getCallerMethod(), "get", "java.util.concurrent.Future",
						implementedArchitecturalComponent.getComponentDeclaredMethods())) {
					methodWithFutureReturnTypeIsProperlyCalled = false;
					break;
				}
			}

			if (!methodWithFutureReturnTypeIsProperlyCalled)
				throw new VerificationException("The intended 'Asynchronous Task Execution' port provided by "
						+ implementedArchitecturalComponent.getComponentIdentifier() + " is not part of the implementation!");
		}
	}
	
	private Set<MethodDeclarationMetaData> verifyProvidedSharedData(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent,
			ProvidedPortInterface p) throws VerificationException {
		Set<MethodDeclarationMetaData> providedMethodsUsedByThisSharedDataPort = new HashSet<MethodDeclarationMetaData>();
		String sharedVariable = p.getPortInterfaceIdentifier();
		Set<MethodDeclarationMetaData> getterMethodsUsed = verifyGetterMethodConsistency(implementedArchitecturalComponent.getComponentIdentifier(),
				sharedVariable, implementedArchitecturalComponent.getComponentProvidedMethods());
		Set<MethodDeclarationMetaData> setterMethodsUsed = verifySetterMethodConsistency(implementedArchitecturalComponent.getComponentIdentifier(),
				sharedVariable, implementedArchitecturalComponent.getComponentProvidedMethods());

		if (p.getPortInterfaceCommunicationSynchronizationType() != PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK) {
			for (MethodDeclarationMetaData methodDeclaration : setterMethodsUsed) {
				if (!recursiveCheckForMethodCallWithinMethodDeclaration(methodDeclaration, "notifyObservers", "java.util.Observable",
						implementedArchitecturalComponent.getComponentDeclaredMethods()))
					throw new VerificationException(
							"The 'notify' method required for the intended 'Asynchronous/Synchronous with callback Shared Data' port provided by "
									+ implementedArchitecturalComponent.getComponentIdentifier() + " is not part of the implementation!");
			}
		}

		// TODO
		// You can check here whether any method is directly using a variable
		// without getters and setters

		providedMethodsUsedByThisSharedDataPort.addAll(getterMethodsUsed);
		providedMethodsUsedByThisSharedDataPort.addAll(setterMethodsUsed);
		return providedMethodsUsedByThisSharedDataPort;

	}
	
	private void verifyRequiredSharedData(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent, RequiredPortInterface r)
			throws VerificationException {
		String sharedVariable = r.getPortInterfaceIdentifier();
		String getterMethod = constructMethodFromVariable("get", sharedVariable);
		String setterMethod = constructMethodFromVariable("set", sharedVariable);

		boolean getterMethodIsCalled = true;
		if (!implementedArchitecturalComponent.getComponentRequiredMethodsMap().containsKey(getterMethod))
			getterMethodIsCalled = false;

		boolean setterMethodIsCalled = true;
		if (!implementedArchitecturalComponent.getComponentRequiredMethodsMap().containsKey(setterMethod))
			setterMethodIsCalled = false;

		if (!getterMethodIsCalled && !setterMethodIsCalled)
			throw new VerificationException("The intended 'Shared Data' port " + sharedVariable + " required by "
					+ implementedArchitecturalComponent.getComponentIdentifier() + " is not called as part of the implementation!");

		if (r.getPortInterfaceCommunicationSynchronizationType() != PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK) {
			if (!getterMethodIsCalled)
				throw new VerificationException("The intended 'Shared Data' port " + sharedVariable + " getter method required by "
						+ implementedArchitecturalComponent.getComponentIdentifier() + " is not called as part of the implementation!");

			boolean updateMethodIsProperlyDeclared = false;
			if (implementedArchitecturalComponent.getComponentDeclaredMethods().containsKey("update")) {
				for (MethodDeclarationMetaData methodDeclaration : implementedArchitecturalComponent.getComponentDeclaredMethods().get("update")) {
					if (implementedArchitecturalComponent.getComponentImplementedClasses().get(methodDeclaration.getMethodDeclaringClass())
							.getImplementedInterfaces().contains("java.util.Observer")) {
						if (methodDeclaration.getMethodReturnType().equals("void") && methodDeclaration.getMethodParameterTypes().isEmpty()) {
							updateMethodIsProperlyDeclared = true;
						}
					}
				}
			}
			if (!updateMethodIsProperlyDeclared)
				throw new VerificationException("The intended 'SYNC/ASYN_WITH_NO_CALLBACK Shared Data' port " + sharedVariable + " update method required by "
						+ implementedArchitecturalComponent.getComponentIdentifier() + " is not properly declared as part of the implementation!");
		}

	}
	
	private Set<MethodDeclarationMetaData> verifyProvidedMessagePassing(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent,
			ProvidedPortInterface providedPortInterface) throws VerificationException {
		return verifyMessagePassing(implementedArchitecturalComponent, providedPortInterface);
	}

	private void verifyRequiredMessagePassing(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent, RequiredPortInterface requiredPortInterface) throws VerificationException {
		verifyMessagePassing(implementedArchitecturalComponent, requiredPortInterface);
	}
	

	private Set<MethodDeclarationMetaData> verifyMessagePassing(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent, PortInterface portInterface) throws VerificationException {
		Set<MethodDeclarationMetaData> methodsUsedByThisMessagePassingPort = new HashSet<MethodDeclarationMetaData>();
		HashSetValuedHashMap<String, MethodDeclarationMetaData> componentDeclaredMehods = implementedArchitecturalComponent.getComponentDeclaredMethods();
		// check communication wire is accessed through getters and setters
		// (don't have to be public)
		String communicationWire = portInterface.getPortInterfaceIdentifier();
		Set<MethodDeclarationMetaData> getterMethodsUsed = verifyGetterMethodConsistency(implementedArchitecturalComponent.getComponentIdentifier(),
				communicationWire, implementedArchitecturalComponent.getComponentDeclaredMethods());
		Set<MethodDeclarationMetaData> setterMethodsUsed = verifySetterMethodConsistency(implementedArchitecturalComponent.getComponentIdentifier(),
				communicationWire, implementedArchitecturalComponent.getComponentDeclaredMethods());

		String sendMessageThroughCommunicationWireMethod = constructMethodFromVariable("sendMessageThrough", communicationWire);
		String receiveMessageThroughCommunicationWireMethod = constructMethodFromVariable("receiveMessageThrough", communicationWire);

		// check that at least one method calls the set method for the
		// communication wire to properly set it
		boolean communicationWireIsProperlySet = false;
		outerloop: for (MethodDeclarationMetaData methodDeclaration : componentDeclaredMehods.values()) {
			for (MethodCallMetaData methodCall : methodDeclaration.getMethodCallsMap().values()) {
				for (MethodDeclarationMetaData setterMethodDeclaration : setterMethodsUsed) {
					if (setterMethodDeclaration.equivalentToMethodCallMetaData(methodCall)) {
						communicationWireIsProperlySet = true;
						break outerloop;
					}
				}
			}
		}

		if (!communicationWireIsProperlySet)
			throw new VerificationException("The intended 'Message Passing' port of " + implementedArchitecturalComponent.getComponentIdentifier()
					+ " doesn't call a setter method for setting the communication wire as part of the implementation!");

		// check that each sendMessageMethod calls the getter method for the
		// communication wire

		boolean isProperSendMessageMethod = true;
		if (componentDeclaredMehods.containsKey(sendMessageThroughCommunicationWireMethod)) {
			Set<MethodDeclarationMetaData> sendMessageMethods = componentDeclaredMehods.get(sendMessageThroughCommunicationWireMethod);

			outerloop: for (MethodDeclarationMetaData sendMessageMethod : sendMessageMethods) {
				for (MethodDeclarationMetaData getterMethodUsed : getterMethodsUsed) {
					if (recursiveCheckForMethodCallWithinMethodDeclaration(sendMessageMethod, getterMethodUsed.getMethodIdentifier(),
							getterMethodUsed.getMethodDeclaringClass(), componentDeclaredMehods)) {
						methodsUsedByThisMessagePassingPort.add(sendMessageMethod);
						continue outerloop;
					}
				}
				isProperSendMessageMethod = false;
				break;
			}
		} else {
			isProperSendMessageMethod = false;
		}

		// Throw exception only if the sendMessage method is not properly set
		// and the communication type is not ASYNC_WITH_NO_CALLBACK
		if (!isProperSendMessageMethod
				&& (portInterface.getPortInterfaceType().equals(PortInterfaceType.REQUIRED)))
				throw new VerificationException("The intended 'Message Passing' port " + portInterface.getPortInterfaceIdentifier() + " required by " + implementedArchitecturalComponent.getComponentIdentifier()
				+ " doesn't have a proper sendMessageMethod as part of the implementation!");
		
		if (!isProperSendMessageMethod
				&& portInterface.getPortInterfaceCommunicationSynchronizationType() != PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK)
			throw new VerificationException("The intended 'Message Passing' port  " + portInterface.getPortInterfaceIdentifier() + " provided by " + implementedArchitecturalComponent.getComponentIdentifier()
					+ " doesn't have a proper sendMessageMethod as part of the implementation!");

		// check that each receiveMessageMethod calls the getter method for the
		// communication wire
		boolean isProperReceiveMessageMethod = true;
		if (componentDeclaredMehods.containsKey(receiveMessageThroughCommunicationWireMethod)) {
			Set<MethodDeclarationMetaData> receiveMessageMethods = componentDeclaredMehods.get(receiveMessageThroughCommunicationWireMethod);

			outerloop: for (MethodDeclarationMetaData receiveMessageMethod : receiveMessageMethods) {
				for (MethodDeclarationMetaData getterMethodUsed : getterMethodsUsed) {
					if (recursiveCheckForMethodCallWithinMethodDeclaration(receiveMessageMethod, getterMethodUsed.getMethodIdentifier(),
							getterMethodUsed.getMethodDeclaringClass(), componentDeclaredMehods)) {
						methodsUsedByThisMessagePassingPort.add(receiveMessageMethod);
						continue outerloop;
					}
				}
				isProperReceiveMessageMethod = false;
				break;
			}
		} else {
			isProperReceiveMessageMethod = false;
		}

		if (!isProperReceiveMessageMethod && portInterface.getPortInterfaceType().equals(PortInterfaceType.PROVIDED))
			throw new VerificationException("The intended 'Message Passing' port " + portInterface.getPortInterfaceIdentifier() + " provided by " + implementedArchitecturalComponent.getComponentIdentifier()
					+ " doesn't have a proper receiveMessageMethod as part of the implementation!");
		
		if (!isProperReceiveMessageMethod
				&& portInterface.getPortInterfaceCommunicationSynchronizationType() != PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK)
			throw new VerificationException("The intended 'Message Passing' port " + portInterface.getPortInterfaceIdentifier() + " required by " + implementedArchitecturalComponent.getComponentIdentifier()
					+ " doesn't have a proper receiveMessageMethod as part of the implementation!");

		return SetUtils.intersection(methodsUsedByThisMessagePassingPort, new HashSet<>(componentDeclaredMehods.values()));

	}


	private Set<MethodDeclarationMetaData> verifyGetterMethodConsistency(String componentIdentifier, String variable,
			HashSetValuedHashMap<String, MethodDeclarationMetaData> methodsUnderInvestigation) throws VerificationException {
		Set<MethodDeclarationMetaData> getterMethodsUsed = new HashSet<MethodDeclarationMetaData>();
		String getterMethod = constructMethodFromVariable("get", variable);
		boolean isProperGetterMethod = false;
		if (methodsUnderInvestigation.containsKey(getterMethod)) {
			for (MethodDeclarationMetaData methodDeclaration : methodsUnderInvestigation.get(getterMethod)) {
				if (methodDeclaration.getFieldAccessMap().get(variable).isRead()) {
					isProperGetterMethod = true;
					getterMethodsUsed.add(methodDeclaration);
				}
			}
		}
		if (!isProperGetterMethod)
			throw new VerificationException(
					"The intended 'Shared Data' port provided by " + componentIdentifier + " has no Getter method as part of the implementation!");

		return getterMethodsUsed;

	}

	private Set<MethodDeclarationMetaData> verifySetterMethodConsistency(String componentIdentifier, String variable,
			HashSetValuedHashMap<String, MethodDeclarationMetaData> methodsUnderInvestigation) throws VerificationException {
		Set<MethodDeclarationMetaData> setterMethodsUsed = new HashSet<MethodDeclarationMetaData>();

		String setterMethod = constructMethodFromVariable("set", variable);
		boolean isProperSetterMethod = false;
		if (methodsUnderInvestigation.containsKey(setterMethod)) {
			for (MethodDeclarationMetaData methodDeclaration : methodsUnderInvestigation.get(setterMethod)) {
				if (methodDeclaration.getFieldAccessMap().get(variable).isWritten()) {
					isProperSetterMethod = true;
					setterMethodsUsed.add(methodDeclaration);
				}
			}
		}
		if (!isProperSetterMethod)
			throw new VerificationException(
					"The intended 'Shared Data' port provided by " + componentIdentifier + " has no Setter method as part of the implementation!");

		return setterMethodsUsed;
	}

	private boolean recursiveCheckForMethodCallWithinMethodDeclaration(MethodDeclarationMetaData methodDeclaration, String methodCallIdentifier,
			String methodCallDeclaringClass, HashSetValuedHashMap<String, MethodDeclarationMetaData> allComponentDeclaredMethods) {

		if (methodDeclaration.getMethodCallsMap().containsKey(methodCallIdentifier)) {
			Set<MethodCallMetaData> notifyObserversMethodCalls = methodDeclaration.getMethodCallsMap().get(methodCallIdentifier);
			for (MethodCallMetaData mc : notifyObserversMethodCalls) {
				if (mc.getMethodIdentifier().equals(methodCallIdentifier) && mc.getMethodDeclaringClass().equals(methodCallDeclaringClass)) {
					return true;
				}
			}
		} else {
			for (MethodCallMetaData mc : methodDeclaration.getMethodCallsMap().values()) {
				if (allComponentDeclaredMethods.containsKey(mc.getMethodIdentifier())) {
					for (MethodDeclarationMetaData md : allComponentDeclaredMethods.get(mc.getMethodIdentifier())) {
						if (md.equivalentToMethodCallMetaData(mc))
							if (recursiveCheckForMethodCallWithinMethodDeclaration(md, methodCallIdentifier, methodCallDeclaringClass,
									allComponentDeclaredMethods))
								return true;

					}
				}
			}
		}
		return false;
	}

	private String constructMethodFromVariable(String methodName, String variable) {
		return methodName + variable.substring(0, 1).toUpperCase() + variable.substring(1);
	}

}
