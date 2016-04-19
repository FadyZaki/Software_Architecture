package com.architecture.specification.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import com.architecture.specification.architectural.model.CustomArchitecturalModelInitializer;
import com.architecture.specification.library.exceptions.ComponentNotFoundException;
import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;
import com.architecture.specification.library.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.library.exceptions.UnusedComponentException;
import com.architecture.specification.library.exceptions.UnusedRequiredPortInterfaceException;

public class ArchitecturalModelBuilder implements IArchitecturalModelBuilder {

	private ArchitecturalModel architecturalModel;

	public ArchitecturalModelBuilder() {
		this.architecturalModel = new ArchitecturalModel();
	}

	@Override
	public void buildArchitecturalModel(CustomArchitecturalModelInitializer initializer)
			throws IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException {

		initializer.initializeArchitecturalComponents();
		refineArchitecturalComponentsHierarchy();

		initializer.initializeComponentsCommunicationLinks();
		verifyCommunicationLinksCompatibility();
		verifyEachComponentIsUsed();
		verifyEachRequiredPortIsUsed();

		initializer.initializeConcurrentComponentsMap();
		refineConcurrentComponentsMap();
	}

	private void refineArchitecturalComponentsHierarchy() {
		adjustChildrenComponentsForEachComponent();
		HashSet<ArchitecturalComponent> modelComponents = architecturalModel.getModelComponents();
		for (ArchitecturalComponent c : modelComponents) {
			if (c.getParentComponent() == null) {
				adjustComponentsTree(c);
			}
		}
	}

	private void adjustChildrenComponentsForEachComponent() {
		for (ArchitecturalComponent c : architecturalModel.getModelComponents()) {
			if (c.getParentComponent() != null)
				c.getParentComponent().getChildrenComponents().add(c);
		}
	}

	// post-order traversal
	private void adjustComponentsTree(ArchitecturalComponent root) {
		for (ArchitecturalComponent c : root.getChildrenComponents()) {
			adjustComponentsTree(c);
		}
		for (ArchitecturalComponent c : root.getChildrenComponents()) {
			root.getComponentClasses().addAll(c.getComponentClasses());
			root.getProvidedInterfaces().addAll(c.getProvidedInterfaces());
			root.getRequiredInterfaces().addAll(c.getRequiredInterfaces());
		}
	}

	/**
	 * Two Interfaces are compatible if they have the same attributes (interface
	 * identifier, communication type and synchronization type) and are siblings
	 * 
	 * @throws IncompatiblePortInterfacesException
	 */
	private void verifyCommunicationLinksCompatibility() throws IncompatiblePortInterfacesException {
		for (CommunicationLink cl : architecturalModel.getCommunicationLinks()) {
			if (!cl.getProvidedPortInterface().canConnectTo(cl.getRequiredPortInterface())
					|| !Objects.equals(cl.getProvidingComponent().getParentComponent(),
							cl.getRequiringComponent().getParentComponent()))
				throw new IncompatiblePortInterfacesException(
						cl.getProvidedPortInterface().getPortInterfaceIdentifier(),
						cl.getRequiredPortInterface().getPortInterfaceIdentifier());
			else
				removeUnnecessaryPortInterfaces(cl);
		}

	}

	private void removeUnnecessaryPortInterfaces(CommunicationLink cl) {
		ArchitecturalComponent providingComponent = cl.getProvidingComponent();
		ArchitecturalComponent requiringComponent = cl.getRequiringComponent();

		ArchitecturalComponent currentComponent = providingComponent.getParentComponent();
		while (currentComponent != null) {
			currentComponent.getProvidedInterfaces().remove(cl.getProvidedPortInterface());
			currentComponent = currentComponent.getParentComponent();
		}

		currentComponent = requiringComponent.getParentComponent();
		while (currentComponent != null) {
			currentComponent.getRequiredInterfaces().remove(cl.getRequiredPortInterface());
			currentComponent = currentComponent.getParentComponent();
		}
	}

	private void verifyEachComponentIsUsed() throws UnusedComponentException {
		HashSet<CommunicationLink> commLinks = architecturalModel.getCommunicationLinks();
		HashSet<ArchitecturalComponent> usedComponents = new HashSet<ArchitecturalComponent>();
		for (CommunicationLink cl : commLinks) {
			usedComponents.add(cl.getProvidingComponent());
			addUsedComponents(usedComponents, cl.getProvidingComponent(), cl.getProvidedPortInterface());

			usedComponents.add(cl.getRequiringComponent());
			addUsedComponents(usedComponents, cl.getRequiringComponent(), cl.getRequiredPortInterface());
		}

		HashSet<ArchitecturalComponent> modelComponents = architecturalModel.getModelComponents();
		for (ArchitecturalComponent c : modelComponents) {
			if (!usedComponents.contains(c))
				throw new UnusedComponentException(c);
		}

	}

	private void addUsedComponents(HashSet<ArchitecturalComponent> usedComponents, ArchitecturalComponent component,
			PortInterface p) {
		if (!component.getChildrenComponents().isEmpty()) {
			for (ArchitecturalComponent c : component.getChildrenComponents()) {
				if (c.getProvidedInterfaces().contains(p)) {
					usedComponents.add(c);
					addUsedComponents(usedComponents, c, p);
				}
			}
		}

	}

	private void verifyEachRequiredPortIsUsed() throws UnusedRequiredPortInterfaceException {

		HashMap<ArchitecturalComponent, HashSet<RequiredPortInterface>> componentsUsedRequiredPortInterfacesMap = new HashMap<ArchitecturalComponent, HashSet<RequiredPortInterface>>();
		HashSet<CommunicationLink> commLinks = architecturalModel.getCommunicationLinks();
		for (CommunicationLink cl : commLinks) {
			HashSet<RequiredPortInterface> requiredPortInterfaces = componentsUsedRequiredPortInterfacesMap
					.containsKey(cl.getRequiringComponent())
							? componentsUsedRequiredPortInterfacesMap.get(cl.getRequiringComponent())
							: new HashSet<RequiredPortInterface>();
			componentsUsedRequiredPortInterfacesMap.put(cl.getRequiringComponent(), requiredPortInterfaces);
		}
		
		for (ArchitecturalComponent c : architecturalModel.getModelComponents()) {
			if (c.getParentComponent() == null) {
				if(!c.getRequiredInterfaces().equals(componentsUsedRequiredPortInterfacesMap.get(c)))
						throw new UnusedRequiredPortInterfaceException(c);
				}
			}
		}

	private void refineConcurrentComponentsMap() {
		HashMap<ArchitecturalComponent, HashSet<ArchitecturalComponent>> concurrentComponentsMap = architecturalModel
				.getConcurrentComponentsMap();
		for (Map.Entry<ArchitecturalComponent, HashSet<ArchitecturalComponent>> entry : concurrentComponentsMap
				.entrySet()) {
			ArchitecturalComponent currentComponent = entry.getKey();
			for (ArchitecturalComponent c : entry.getValue()) {
				if (concurrentComponentsMap.containsKey(c)) {
					concurrentComponentsMap.get(c).add(currentComponent);
				} else {
					concurrentComponentsMap.put(c, new HashSet<>(Arrays.asList(currentComponent)));
				}
			}
		}
	}

	public void addComponent(String componentIdentifier, ArchitecturalComponent parentComponent,
			HashSet<String> componentClasses, HashSet<ProvidedPortInterface> providedInterfaces,
			HashSet<RequiredPortInterface> requiredInterfaces) {
		ArchitecturalComponent architecturalComponent = new ArchitecturalComponent(componentIdentifier, parentComponent,
				componentClasses, providedInterfaces, requiredInterfaces);
		architecturalModel.getModelComponents().add(architecturalComponent);
	}

	public void addCommunicationLink(String providingComponentIdentifier, String requiringComponentIdentifier,
			String portIdentifier, PortInterfaceCommunicationType portInterfaceCommunicationType,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType)
					throws ComponentNotFoundException, PortInterfaceNotDefinedInComponentException {
		HashMap<String, ArchitecturalComponent> componentsIdentifersMap = architecturalModel
				.getArchitecturalComponentsIdentifiersMap();
		ArchitecturalComponent providingComponent = componentsIdentifersMap.get(providingComponentIdentifier);
		ArchitecturalComponent requiringComponent = componentsIdentifersMap.get(requiringComponentIdentifier);

		if (providingComponent == null)
			throw new ComponentNotFoundException(providingComponentIdentifier);

		if (requiringComponent == null)
			throw new ComponentNotFoundException(requiringComponentIdentifier);

		HashMap<String, ProvidedPortInterface> providingComponentPortInterfacesMap = providingComponent
				.getProvidedInterfacesMap();
		HashMap<String, RequiredPortInterface> requiringComponentPortInterfacesMap = requiringComponent
				.getRequiredInterfacesMap();

		String portInterfaceSignature = portIdentifier + HelperConstants.UNDERSCORE_SYMBOL
				+ portInterfaceCommunicationType.toString() + HelperConstants.UNDERSCORE_SYMBOL
				+ portInterfaceCommunicationSynchronizationType.toString();
		ProvidedPortInterface p = providingComponentPortInterfacesMap.get(portInterfaceSignature);
		RequiredPortInterface r = requiringComponentPortInterfacesMap.get(portInterfaceSignature);

		if (p == null)
			throw new PortInterfaceNotDefinedInComponentException(portInterfaceSignature, providingComponent);

		if (r == null)
			throw new PortInterfaceNotDefinedInComponentException(portInterfaceSignature, requiringComponent);

		CommunicationLink communicationLink = new CommunicationLink(providingComponent, requiringComponent, p, r);
		architecturalModel.getCommunicationLinks().add(communicationLink);
	}

	public void addConcurrentComponentsEntry(String componentIdentifier,
			ArrayList<String> concurrentComponentsIdentifiers) {
		HashMap<String, ArchitecturalComponent> componentsIdentifersMap = architecturalModel
				.getArchitecturalComponentsIdentifiersMap();
		HashSet<ArchitecturalComponent> concurrentComponents = new HashSet<ArchitecturalComponent>();
		for (String identifer : concurrentComponentsIdentifiers) {
			concurrentComponents.add(componentsIdentifersMap.get(identifer));
		}
		architecturalModel.getConcurrentComponentsMap().put(componentsIdentifersMap.get(componentIdentifier),
				new HashSet<ArchitecturalComponent>(concurrentComponents));
	}

}
