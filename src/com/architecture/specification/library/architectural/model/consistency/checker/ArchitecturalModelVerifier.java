package com.architecture.specification.library.architectural.model.consistency.checker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.SetUtils.SetView;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import com.architecture.specification.library.architectural.model.extracted.ExtractedArchitecturalModel;
import com.architecture.specification.library.architectural.model.extracted.metadata.ArchitecturalComponentImplementationMetaData;
import com.architecture.specification.library.architectural.model.extracted.metadata.ClassMetaData;
import com.architecture.specification.library.architectural.model.extracted.metadata.MethodCallMetaData;
import com.architecture.specification.library.architectural.model.extracted.metadata.MethodDeclarationMetaData;
import com.architecture.specification.library.architectural.model.intended.IntendedArchitecturalModel;
import com.architecture.specification.library.architectural.model.intended.communication.link.CommunicationLink;
import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.library.architectural.model.intended.component.BlackboxArchitecturalComponent;
import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterface;
import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterfaceCommunicationSynchronizationType;
import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterfaceType;
import com.architecture.specification.library.architectural.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.library.architectural.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.library.exceptions.VerificationException;

/**
 * This class is responsible for checking inconsistencies between
 * the intended model and the extracted model
 */
public class ArchitecturalModelVerifier {

	IntendedArchitecturalModel intendedArchitecturalModel;
	ExtractedArchitecturalModel extractedArchitecturalModel;

	public ArchitecturalModelVerifier(IntendedArchitecturalModel intendedArchitecturalModel, ExtractedArchitecturalModel extractedArchitecturalModel) {
		super();
		this.intendedArchitecturalModel = intendedArchitecturalModel;
		this.extractedArchitecturalModel = extractedArchitecturalModel;
	}

	/**
	 * This method is responsible for checking inconsistencies between
	 * the intended model and the extracted model
	 * @throws VerificationException
	 */
	public void verifySpecificationAgainstImplementation() throws VerificationException {

		// Consistency checking between classes in the architecture
		// specification and the code implementing it
		Set<String> actualClassesNames = extractedArchitecturalModel.getComponentsClassesNames();
		Set<String> intendedClassesNames = intendedArchitecturalModel.getClassComponentsMap().keySet();

		SetView<String> intendedClassesNotAvailable = SetUtils.difference(intendedClassesNames, actualClassesNames);
		if (!intendedClassesNotAvailable.isEmpty())
			throw new VerificationException("These classes are intended in the architecture specification but are not available as part of the implementation"
					+ intendedClassesNotAvailable.toString());

		if (!extractedArchitecturalModel.getImplementedClassesNotIntended().isEmpty())
			throw new VerificationException("These classes are available as part of the implementation but are not intended in the architecture specification "
					+ extractedArchitecturalModel.getImplementedClassesNotIntended());

		// Consistency checking between the providedPorts in the architecture
		// specification and the code implementing it
		for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getModelComponentsIdentifiersMap().values()) {
			if (architecturalComponent.getParentComponent() == null) {
				HashSetValuedHashMap<String, MethodCallMetaData> intendedCommunicationLinksComponentsMap = new HashSetValuedHashMap<String, MethodCallMetaData>();
				startRecursiveVerification(architecturalComponent, intendedCommunicationLinksComponentsMap);
			}
		}

	}

	private void startRecursiveVerification(ArchitecturalComponent architecturalComponent,
			HashSetValuedHashMap<String, MethodCallMetaData> intendedCommunicationLinksComponentsMap) throws VerificationException {
		ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent = extractedArchitecturalModel.getActualImplementedComponents()
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
				verifyProvidedMessagePassing(implementedArchitecturalComponent, p);
				break;
			default:
				break;
			}

		}

		// Consistency checking of required ports
		for (RequiredPortInterface r : architecturalComponent.getRequiredInterfaces()) {
			switch (r.getPortInterfaceCommunicationType()) {
			case TASK_EXECUTION:
				verifyRequiredTaskExecution(implementedArchitecturalComponent, r);
				break;
			case SHARED_DATA:
				intendedComponentProvidedMethods.addAll(verifyRequiredSharedData(implementedArchitecturalComponent, r));
				break;
			case MESSAGE_PASSING:
				verifyRequiredMessagePassing(implementedArchitecturalComponent, r);
				break;
			default:
				break;
			}
		}

		if (!(architecturalComponent instanceof BlackboxArchitecturalComponent)) {
			SetView<MethodDeclarationMetaData> availableMethodsNotIntended = SetUtils.difference(
					new HashSet<MethodDeclarationMetaData>(implementedArchitecturalComponent.getComponentProvidedMethods().values()),
					intendedComponentProvidedMethods);
			if (!availableMethodsNotIntended.isEmpty())
				throw new VerificationException(
						"These public methods are available in the implementation but are never defined as part of the architecture specification"
								+ availableMethodsNotIntended.toString());
		}

		// Consistency checking of communication links specified in the
		// architectural model being fulfilled by the implementation
		// code
		Set<MethodCallMetaData> intendedComponentCommunicationLinksMethodCalls = new HashSet<MethodCallMetaData>();
		for (CommunicationLink communicationLink : intendedArchitecturalModel.getRequiringComponentCommunicationLinksMap().get(architecturalComponent)) {
			ArchitecturalComponentImplementationMetaData providingComponent;
			ArchitecturalComponentImplementationMetaData requiringComponent;
			if (communicationLink.getInnermostProvidingComponent() != null)
				providingComponent = extractedArchitecturalModel.getActualImplementedComponents()
						.get(communicationLink.getInnermostProvidingComponent().getComponentIdentifier());
			else
				providingComponent = extractedArchitecturalModel.getActualImplementedComponents()
						.get(communicationLink.getProvidingComponent().getComponentIdentifier());

			requiringComponent = extractedArchitecturalModel.getActualImplementedComponents()
					.get(communicationLink.getRequiringComponent().getComponentIdentifier());

			RequiredPortInterface requiredPortInterface = communicationLink.getRequiredPortInterface();
			switch (requiredPortInterface.getPortInterfaceCommunicationType()) {
			case TASK_EXECUTION:
				intendedComponentCommunicationLinksMethodCalls
						.addAll(verifyTaskExecutionCommunicationLink(providingComponent, requiringComponent, requiredPortInterface));
				break;
			case SHARED_DATA:
				intendedComponentCommunicationLinksMethodCalls
						.addAll(verifySharedDataCommunicationLink(providingComponent, requiringComponent, requiredPortInterface));
				break;
			case MESSAGE_PASSING:
				verifyMessagePassingCommunicationLink(providingComponent, requiringComponent, requiredPortInterface);
				break;
			default:
				break;
			}
		}

		// Check for method calls that are not intended
		if (!(architecturalComponent instanceof BlackboxArchitecturalComponent)) {
			if (architecturalComponent.getParentComponent() != null) {
				intendedComponentCommunicationLinksMethodCalls
						.addAll(intendedCommunicationLinksComponentsMap.get(architecturalComponent.getParentComponent().getComponentIdentifier()));
			}

			Set<MethodCallMetaData> methodCallsNotIntended = new HashSet<>(
					SetUtils.difference(new HashSet<MethodCallMetaData>(implementedArchitecturalComponent.getComponentRequiredMethods().values()),
							intendedComponentCommunicationLinksMethodCalls));

			for (Iterator<MethodCallMetaData> i = methodCallsNotIntended.iterator(); i.hasNext();) {
				MethodCallMetaData md = i.next();
				if (md.getMethodIdentifier().equals("notifyObservers") || md.getMethodIdentifier().equals("addObserver"))
					i.remove();
			}
			if (!methodCallsNotIntended.isEmpty())
				throw new VerificationException(
						"These communication links are available in the implementation but are never defined as part of the architecture specification\n"
								+ methodCallsNotIntended);

			intendedCommunicationLinksComponentsMap.putAll(architecturalComponent.getComponentIdentifier(), intendedComponentCommunicationLinksMethodCalls);
		}

		for (ArchitecturalComponent ac : architecturalComponent.getChildrenComponents())
			startRecursiveVerification(ac, intendedCommunicationLinksComponentsMap);
	}

	private Set<MethodDeclarationMetaData> verifyProvidedTaskExecution(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent,
			ProvidedPortInterface p) throws VerificationException {
		Set<MethodDeclarationMetaData> providedMethodsUsedByThisTaskExecutionPort = new HashSet<MethodDeclarationMetaData>();
		if (!implementedArchitecturalComponent.getAllComponentProvidedMethods().containsKey(p.getPortInterfaceIdentifier()))
			throw new VerificationException("The intended 'Task Execution' port " + p.getPortInterfaceIdentifier() + " provided by "
					+ implementedArchitecturalComponent.getComponentIdentifier() + " is not part of the implementation!");

		Set<MethodDeclarationMetaData> providedPortActualMethods = implementedArchitecturalComponent.getAllComponentProvidedMethods()
				.get(p.getPortInterfaceIdentifier());
		if (p.getPortInterfaceCommunicationSynchronizationType() == PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK) {
			boolean methodWithFutureReturnTypeIsNotProperlyDeclared = false;
			for (MethodDeclarationMetaData mmd : providedPortActualMethods) {
				if (mmd.getMethodReturnType().equals("java.util.concurrent.Future")) {
					providedMethodsUsedByThisTaskExecutionPort.add(mmd);
				} else {
					methodWithFutureReturnTypeIsNotProperlyDeclared = true;
					break;
				}
			}

			if (methodWithFutureReturnTypeIsNotProperlyDeclared)
				throw new VerificationException("The intended 'Asynchronous Task Execution' port provided by "
						+ implementedArchitecturalComponent.getComponentIdentifier() + " is not part of the implementation!");
		} else {
			providedMethodsUsedByThisTaskExecutionPort.addAll(providedPortActualMethods);
		}

		return providedMethodsUsedByThisTaskExecutionPort;

	}

	private void verifyRequiredTaskExecution(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent, RequiredPortInterface r)
			throws VerificationException {
		if (!implementedArchitecturalComponent.getAllComponentRequiredMethods().containsKey(r.getPortInterfaceIdentifier()))
			throw new VerificationException("The intended 'Task Execution' port " + r.getPortInterfaceIdentifier() + " required by "
					+ implementedArchitecturalComponent.getComponentIdentifier() + " is not part of the implementation!");

		Set<MethodCallMetaData> requiredPortActualMethods = implementedArchitecturalComponent.getAllComponentRequiredMethods()
				.get(r.getPortInterfaceIdentifier());
		if (r.getPortInterfaceCommunicationSynchronizationType() == PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK) {
			boolean methodWithFutureReturnTypeIsNotProperlyCalled = false;
			for (MethodCallMetaData mmd : requiredPortActualMethods) {
				if (!mmd.getMethodReturnType().equals("java.util.concurrent.Future")) {
					methodWithFutureReturnTypeIsNotProperlyCalled = true;
					break;
				}
				if (!recursiveCheckForMethodCallWithinMethodDeclaration(mmd.getCallerMethod(), "get", "java.util.concurrent.Future",
						implementedArchitecturalComponent.getAllComponentDeclaredMethods())) {
					methodWithFutureReturnTypeIsNotProperlyCalled = true;
					break;
				}
			}

			if (methodWithFutureReturnTypeIsNotProperlyCalled)
				throw new VerificationException("The intended 'Asynchronous Task Execution' port provided by "
						+ implementedArchitecturalComponent.getComponentIdentifier() + " is not part of the implementation!");
		}
	}

	private Set<MethodCallMetaData> verifyTaskExecutionCommunicationLink(ArchitecturalComponentImplementationMetaData providingComponent,
			ArchitecturalComponentImplementationMetaData requiringComponent, PortInterface portInterface) throws VerificationException {
		Set<MethodCallMetaData> methodCallsUsedByThisCommunicationLinkTaskExecution = new HashSet<MethodCallMetaData>();
		Set<MethodCallMetaData> requiredPortActualMethodCalls = requiringComponent.getAllComponentRequiredMethods()
				.get(portInterface.getPortInterfaceIdentifier());

		boolean communicationLinkTaskExecutionIsFulfilled = false;
		for (MethodCallMetaData methodCall : requiredPortActualMethodCalls) {
			if (providingComponent.getComponentImplementedClasses().containsKey(methodCall.getMethodDeclaringClass())) {
				communicationLinkTaskExecutionIsFulfilled = true;
				methodCallsUsedByThisCommunicationLinkTaskExecution.add(methodCall);
			}
		}

		if (!communicationLinkTaskExecutionIsFulfilled)
			throw new VerificationException(
					"The intended 'Task Execution' communication link between the provider " + providingComponent.getComponentIdentifier()
							+ " and the requirer " + requiringComponent.getComponentIdentifier() + " is not completely fulfilled by the implementation!");

		return methodCallsUsedByThisCommunicationLinkTaskExecution;
	}

	private Set<MethodDeclarationMetaData> verifyProvidedSharedData(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent,
			ProvidedPortInterface p) throws VerificationException {
		Set<MethodDeclarationMetaData> providedMethodsUsedByThisSharedDataPort = new HashSet<MethodDeclarationMetaData>();
		String sharedVariable = p.getPortInterfaceIdentifier();
		Set<MethodDeclarationMetaData> getterMethodsUsed = retrieveAllProperGetterMethods(implementedArchitecturalComponent.getComponentIdentifier(),
				sharedVariable, implementedArchitecturalComponent.getAllComponentProvidedMethods());
		Set<MethodDeclarationMetaData> setterMethodsUsed = retrieveAllProperSetterMethods(implementedArchitecturalComponent.getComponentIdentifier(),
				sharedVariable, implementedArchitecturalComponent.getAllComponentProvidedMethods());

		if (getterMethodsUsed.isEmpty() && setterMethodsUsed.isEmpty())
			throw new VerificationException("The intended 'Shared Data' port " + p.getPortInterfaceIdentifier() + " provided by "
					+ implementedArchitecturalComponent.getComponentIdentifier() + " has no Getter or setter methods as part of the implementation!");

		if (p.getPortInterfaceCommunicationSynchronizationType() != PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK) {
			for (MethodDeclarationMetaData methodDeclaration : setterMethodsUsed) {
				if (!recursiveCheckForMethodCallWithinMethodDeclaration(methodDeclaration, "notifyObservers", "java.util.Observable",
						implementedArchitecturalComponent.getComponentDeclaredMethods()))
					throw new VerificationException(
							"The 'notify' method required for the intended 'Asynchronous/Synchronous with callback Shared Data' port provided by "
									+ implementedArchitecturalComponent.getComponentIdentifier() + " is not part of the implementation!");
			}
		}

		providedMethodsUsedByThisSharedDataPort.addAll(getterMethodsUsed);
		providedMethodsUsedByThisSharedDataPort.addAll(setterMethodsUsed);
		return providedMethodsUsedByThisSharedDataPort;

	}

	private Set<MethodDeclarationMetaData> verifyRequiredSharedData(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent,
			RequiredPortInterface r) throws VerificationException {
		Set<MethodDeclarationMetaData> providedMethodsUsedByThisSharedDataPort = new HashSet<MethodDeclarationMetaData>();

		String sharedVariable = r.getPortInterfaceIdentifier();
		String getterMethod = constructMethodFromVariable("get", sharedVariable);
		String setterMethod = constructMethodFromVariable("set", sharedVariable);

		boolean getterMethodIsCalled = true;
		if (!implementedArchitecturalComponent.getAllComponentRequiredMethods().containsKey(getterMethod))
			getterMethodIsCalled = false;

		boolean setterMethodIsCalled = true;
		if (!implementedArchitecturalComponent.getAllComponentRequiredMethods().containsKey(setterMethod))
			setterMethodIsCalled = false;

		if (!getterMethodIsCalled && !setterMethodIsCalled)
			throw new VerificationException("The intended 'Shared Data' port " + sharedVariable + " required by "
					+ implementedArchitecturalComponent.getComponentIdentifier() + " is not called as part of the implementation!");

		if (r.getPortInterfaceCommunicationSynchronizationType() != PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK) {
			if (!getterMethodIsCalled)
				throw new VerificationException("The intended 'Shared Data' port " + sharedVariable + " getter method required by "
						+ implementedArchitecturalComponent.getComponentIdentifier() + " is not called as part of the implementation!");

			boolean componentIsAnObserver = false;
			for (ClassMetaData cmd : implementedArchitecturalComponent.getAllComponentImplementedClasses().values()) {
				if (cmd.getImplementedInterfaces().contains("java.util.Observer")) {
					componentIsAnObserver = true;
					providedMethodsUsedByThisSharedDataPort.addAll(cmd.getDeclaredMethodsMap().get("update"));
				}
			}
			if (!componentIsAnObserver)
				throw new VerificationException("The intended 'SYNC/ASYN_WITH_CALLBACK Shared Data' port " + sharedVariable + " required by "
						+ implementedArchitecturalComponent.getComponentIdentifier() + " is not properly declared as an observer in the implementation!");
		}
		return providedMethodsUsedByThisSharedDataPort;

	}

	private Set<MethodCallMetaData> verifySharedDataCommunicationLink(ArchitecturalComponentImplementationMetaData providingComponent,
			ArchitecturalComponentImplementationMetaData requiringComponent, PortInterface portInterface) throws VerificationException {
		Set<MethodCallMetaData> methodCallsUsedByThisSharedDataCommunicationLink = new HashSet<MethodCallMetaData>();

		Set<MethodCallMetaData> requiredPortActualMethodCalls = new HashSet<MethodCallMetaData>();
		String sharedVariable = portInterface.getPortInterfaceIdentifier();
		String getterMethod = constructMethodFromVariable("get", sharedVariable);
		String setterMethod = constructMethodFromVariable("set", sharedVariable);
		requiredPortActualMethodCalls.addAll(requiringComponent.getAllComponentRequiredMethods().get(getterMethod));
		requiredPortActualMethodCalls.addAll(requiringComponent.getAllComponentRequiredMethods().get(setterMethod));

		boolean sharedDataCommunicationLinkIsFulfilled = false;
		for (MethodCallMetaData methodCall : requiredPortActualMethodCalls) {
			if (providingComponent.getAllComponentImplementedClasses().containsKey(methodCall.getMethodDeclaringClass())) {
				sharedDataCommunicationLinkIsFulfilled = true;
				methodCallsUsedByThisSharedDataCommunicationLink.add(methodCall);
			}
		}

		// Each class for each setter method in the providing component has to
		// be observed by the requiring component
		if (portInterface.getPortInterfaceCommunicationSynchronizationType() != PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK) {
			if (requiringComponent.getAllComponentRequiredMethods().containsKey("addObserver")) {
				for (MethodCallMetaData methodCall : requiringComponent.getAllComponentRequiredMethods().get("addObserver")) {
					if (methodCall.getMethodReturnType().equals("void") && methodCall.getMethodParameterTypes().size() == 1) {
						if (methodCall.getMethodDeclaringClass().equals("java.util.Observable")) {
							methodCallsUsedByThisSharedDataCommunicationLink.add(methodCall);
						}
					}

				}
			}
		}

		if (!sharedDataCommunicationLinkIsFulfilled)
			throw new VerificationException("The intended 'Shared Data' " + portInterface.getPortInterfaceIdentifier()
					+ " communication link between the provider " + providingComponent.getComponentIdentifier() + " and the requirer "
					+ requiringComponent.getComponentIdentifier() + " is not completely fulfilled by the implementation!");

		return methodCallsUsedByThisSharedDataCommunicationLink;

	}

	private void verifyProvidedMessagePassing(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent,
			ProvidedPortInterface providedPortInterface) throws VerificationException {
		verifyMessagePassing(implementedArchitecturalComponent, providedPortInterface);
	}

	private void verifyRequiredMessagePassing(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent,
			RequiredPortInterface requiredPortInterface) throws VerificationException {
		verifyMessagePassing(implementedArchitecturalComponent, requiredPortInterface);
	}

	private void verifyMessagePassing(ArchitecturalComponentImplementationMetaData implementedArchitecturalComponent, PortInterface portInterface)
			throws VerificationException {
		HashSetValuedHashMap<String, MethodDeclarationMetaData> allComponentDeclaredMethods = implementedArchitecturalComponent
				.getAllComponentDeclaredMethods();
		// check communication wire is accessed through getters and setters
		// (don't have to be public)
		String communicationWire = portInterface.getPortInterfaceIdentifier();
		Set<MethodDeclarationMetaData> getterMethodsUsed = retrieveAllProperGetterMethods(implementedArchitecturalComponent.getComponentIdentifier(),
				communicationWire, implementedArchitecturalComponent.getAllComponentDeclaredMethods());
		Set<MethodDeclarationMetaData> setterMethodsUsed = retrieveAllProperSetterMethods(implementedArchitecturalComponent.getComponentIdentifier(),
				communicationWire, implementedArchitecturalComponent.getAllComponentDeclaredMethods());

		if (getterMethodsUsed.isEmpty() || setterMethodsUsed.isEmpty())
			throw new VerificationException("The intended 'Shared Data' port provided by " + implementedArchitecturalComponent.getComponentIdentifier()
					+ " does not have both no Getter and setter methods as part of the implementation!");

		String sendMessageThroughCommunicationWireMethod = constructMethodFromVariable("sendMessageThrough", communicationWire);
		String receiveMessageThroughCommunicationWireMethod = constructMethodFromVariable("receiveMessageThrough", communicationWire);

		// check that at least one method calls the set method for the
		// communication wire to properly set it
		boolean communicationWireIsProperlySet = false;
		outerloop: for (MethodDeclarationMetaData methodDeclaration : allComponentDeclaredMethods.values()) {
			for (MethodCallMetaData methodCall : methodDeclaration.getMethodCallsMap().values()) {
				for (MethodDeclarationMetaData setterMethodDeclaration : setterMethodsUsed) {
					if (setterMethodDeclaration.equivalentToMethodCallMetaData(methodCall)) {
						communicationWireIsProperlySet = true;
						break outerloop;
					}
				}
			}
		}

		// check that at least one method calls the get method for the
		// communication wire to properly set it
		boolean communicationWireIsProperlyGet = false;
		outerloop: for (MethodDeclarationMetaData methodDeclaration : allComponentDeclaredMethods.values()) {
			for (MethodCallMetaData methodCall : methodDeclaration.getMethodCallsMap().values()) {
				for (MethodDeclarationMetaData getterMethodDeclaration : getterMethodsUsed) {
					if (getterMethodDeclaration.equivalentToMethodCallMetaData(methodCall)) {
						communicationWireIsProperlyGet = true;
						break outerloop;
					}
				}
			}
		}

		if (!communicationWireIsProperlySet || !communicationWireIsProperlyGet)
			throw new VerificationException("The intended 'Message Passing' port of " + implementedArchitecturalComponent.getComponentIdentifier() + " "
					+ portInterface.getPortInterfaceIdentifier()
					+ " doesn't call a getter/setter method for setting the communication wire as part of the implementation!");

		boolean isProperSendMessageMethod = true;
		if (!allComponentDeclaredMethods.containsKey(sendMessageThroughCommunicationWireMethod))
			isProperSendMessageMethod = false;

		// Throw exception only if the sendMessage method is not properly set
		// and the communication type is not ASYNC_WITH_NO_CALLBACK
		if (!isProperSendMessageMethod && (portInterface.getPortInterfaceType().equals(PortInterfaceType.REQUIRED)))
			throw new VerificationException("The intended 'Message Passing' port " + portInterface.getPortInterfaceIdentifier() + " required by "
					+ implementedArchitecturalComponent.getComponentIdentifier() + " doesn't have a proper sendMessageMethod as part of the implementation!");

		if (!isProperSendMessageMethod
				&& portInterface.getPortInterfaceCommunicationSynchronizationType() != PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK)
			throw new VerificationException("The intended 'Message Passing' port  " + portInterface.getPortInterfaceIdentifier() + " provided by "
					+ implementedArchitecturalComponent.getComponentIdentifier() + " doesn't have a proper sendMessageMethod as part of the implementation!");

		boolean isProperReceiveMessageMethod = true;
		if (!allComponentDeclaredMethods.containsKey(receiveMessageThroughCommunicationWireMethod))
			isProperReceiveMessageMethod = false;

		if (!isProperReceiveMessageMethod && portInterface.getPortInterfaceType().equals(PortInterfaceType.PROVIDED))
			throw new VerificationException("The intended 'Message Passing' port " + portInterface.getPortInterfaceIdentifier() + " provided by "
					+ implementedArchitecturalComponent.getComponentIdentifier()
					+ " doesn't have a proper receiveMessageMethod as part of the implementation!");

		if (!isProperReceiveMessageMethod
				&& portInterface.getPortInterfaceCommunicationSynchronizationType() != PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK)
			throw new VerificationException("The intended 'Message Passing' port " + portInterface.getPortInterfaceIdentifier() + " required by "
					+ implementedArchitecturalComponent.getComponentIdentifier()
					+ " doesn't have a proper receiveMessageMethod as part of the implementation!");

	}

	private void verifyMessagePassingCommunicationLink(ArchitecturalComponentImplementationMetaData providingComponent,
			ArchitecturalComponentImplementationMetaData requiringComponent, PortInterface portInterface) throws VerificationException {

		String communicationWire = portInterface.getPortInterfaceIdentifier();
		String sendMessageThroughCommunicationWireMethod = constructMethodFromVariable("sendMessageThrough", communicationWire);
		String receiveMessageThroughCommunicationWireMethod = constructMethodFromVariable("receiveMessageThrough", communicationWire);

		boolean messagePassingCommunicationLinkIsFulfilled = true;
		if (!requiringComponent.getAllComponentMessagePassingMethods().containsKey(sendMessageThroughCommunicationWireMethod))
			messagePassingCommunicationLinkIsFulfilled = false;
		if (portInterface.getPortInterfaceCommunicationSynchronizationType() != PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK
				&& !requiringComponent.getAllComponentMessagePassingMethods().containsKey(receiveMessageThroughCommunicationWireMethod))
			messagePassingCommunicationLinkIsFulfilled = false;

		if (!providingComponent.getAllComponentMessagePassingMethods().containsKey(receiveMessageThroughCommunicationWireMethod))
			messagePassingCommunicationLinkIsFulfilled = false;
		if (portInterface.getPortInterfaceCommunicationSynchronizationType() != PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK
				&& !requiringComponent.getAllComponentMessagePassingMethods().containsKey(sendMessageThroughCommunicationWireMethod))
			messagePassingCommunicationLinkIsFulfilled = false;

		if (!messagePassingCommunicationLinkIsFulfilled)
			throw new VerificationException(
					"The intended 'Message Passing' communication link between the provider " + providingComponent.getComponentIdentifier()
							+ " and the requirer " + requiringComponent.getComponentIdentifier() + " is not completely fulfilled by the implementation!");

	}

	private Set<MethodDeclarationMetaData> retrieveAllProperGetterMethods(String componentIdentifier, String variable,
			HashSetValuedHashMap<String, MethodDeclarationMetaData> methodsUnderInvestigation) throws VerificationException {
		Set<MethodDeclarationMetaData> getterMethodsUsed = new HashSet<MethodDeclarationMetaData>();
		String getterMethod = constructMethodFromVariable("get", variable);
		if (methodsUnderInvestigation.containsKey(getterMethod)) {
			for (MethodDeclarationMetaData methodDeclaration : methodsUnderInvestigation.get(getterMethod)) {
				getterMethodsUsed.add(methodDeclaration);
			}

		}

		return getterMethodsUsed;

	}

	private Set<MethodDeclarationMetaData> retrieveAllProperSetterMethods(String componentIdentifier, String variable,
			HashSetValuedHashMap<String, MethodDeclarationMetaData> methodsUnderInvestigation) throws VerificationException {
		Set<MethodDeclarationMetaData> setterMethodsUsed = new HashSet<MethodDeclarationMetaData>();

		String setterMethod = constructMethodFromVariable("set", variable);
		if (methodsUnderInvestigation.containsKey(setterMethod)) {
			for (MethodDeclarationMetaData methodDeclaration : methodsUnderInvestigation.get(setterMethod)) {
				setterMethodsUsed.add(methodDeclaration);
			}

		}

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
