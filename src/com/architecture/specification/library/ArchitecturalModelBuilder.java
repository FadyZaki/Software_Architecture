package com.architecture.specification.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import com.architecture.specification.architectural.model.CustomArchitecturalModelInitializer;
import com.architecture.specification.library.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.library.exceptions.ComponentNotFoundException;
import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;
import com.architecture.specification.library.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.library.exceptions.PortInterfaceNotFoundException;
import com.architecture.specification.library.exceptions.UnusedComponentException;
import com.architecture.specification.library.exceptions.UnusedRequiredPortInterfaceException;

public class ArchitecturalModelBuilder implements IArchitecturalModelBuilder {

	public ArchitecturalModel getArchitecturalModel() {
		return architecturalModel;
	}

	private ArchitecturalModel architecturalModel;

	public ArchitecturalModelBuilder() {
		this.architecturalModel = new ArchitecturalModel();
	}

	@Override
	public void buildArchitecturalModel(CustomArchitecturalModelInitializer initializer)
			throws IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException,
			ComponentNotFoundException, PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException {

		initializer.initializeModelPortInterfaces();
		initializer.initializeModelArchitecturalComponents();
		refineArchitecturalComponentsHierarchy();

		initializer.initializeModelComponentsCommunicationLinks();
		verifyCommunicationLinksCompatibility();
		verifyEachComponentIsUsed();
		verifyEachRequiredPortIsUsed();

		initializer.initializeModelConcurrentComponentsMap();
		refineConcurrentComponentsMap();
		
		architecturalModel.getClassComponentsMap();
	}

	private void refineArchitecturalComponentsHierarchy() {
		adjustChildrenComponentsForEachComponent();
		for (ArchitecturalComponent c : architecturalModel.getModelComponentsIdentifiersMap().values()) {
			if (c.getParentComponent() == null) {
				adjustComponentsTree(c);
			}
		}
	}

	private void adjustChildrenComponentsForEachComponent() {
		for (ArchitecturalComponent c : architecturalModel.getModelComponentsIdentifiersMap().values()) {
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

		for (ArchitecturalComponent c : architecturalModel.getModelComponentsIdentifiersMap().values()) {
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
			requiredPortInterfaces.add(cl.getRequiredPortInterface());
			componentsUsedRequiredPortInterfacesMap.put(cl.getRequiringComponent(), requiredPortInterfaces);
		}

		for (ArchitecturalComponent c : architecturalModel.getModelComponentsIdentifiersMap().values()) {
			if (c.getParentComponent() == null) {
				if (!c.getRequiredInterfaces().isEmpty()
						&& !Objects.equals(c.getRequiredInterfaces(), componentsUsedRequiredPortInterfacesMap.get(c)))
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

	public void addComponent(String componentIdentifier, String parentComponentIdentifier,
			ArrayList<String> componentClasses, HashSet<ProvidedPortInterface> providedInterfaces,
			HashSet<RequiredPortInterface> requiredInterfaces) throws ComponentNotFoundException {
		HashMap<String, ArchitecturalComponent> componentsIdentifersMap = architecturalModel
				.getModelComponentsIdentifiersMap();
		ArchitecturalComponent parentComponent = componentsIdentifersMap.get(parentComponentIdentifier);
		if (parentComponentIdentifier != null && parentComponent == null)
			throw new ComponentNotFoundException(parentComponentIdentifier);

		ArchitecturalComponent architecturalComponent = new ArchitecturalComponent(componentIdentifier, parentComponent,
				componentClasses, providedInterfaces, requiredInterfaces);
		architecturalModel.getModelComponentsIdentifiersMap().put(architecturalComponent.getComponentIdentifier(),
				architecturalComponent);
	}

	public void addPortInterface(String portInterfaceIdentifier, PortInterfaceType portInterfaceType,
			PortInterfaceCommunicationType portInterfaceCommunicationType,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType) {

		if (portInterfaceType == PortInterfaceType.PROVIDED) {
			ProvidedPortInterface p = new ProvidedPortInterface(portInterfaceIdentifier, portInterfaceCommunicationType,
					portInterfaceCommunicationSynchronizationType);
			architecturalModel.getModelProvidedPortInterfacesMap().put(p.getPortInterfaceSignature(), p);
		}

		if (portInterfaceType == PortInterfaceType.REQUIRED) {
			RequiredPortInterface r = new RequiredPortInterface(portInterfaceIdentifier, portInterfaceCommunicationType,
					portInterfaceCommunicationSynchronizationType);
			architecturalModel.getModelRequiredPortInterfacesMap().put(r.getPortInterfaceSignature(), r);
		}

	}

	public ProvidedPortInterface getProvidedPortInterface(String portInterfaceIdentifier,
			PortInterfaceCommunicationType portInterfaceCommunicationType,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType)
					throws PortInterfaceNotFoundException {

		String providedPortInterfaceSignature = PortInterface.constructPortInterfaceSignature(portInterfaceIdentifier,
				portInterfaceCommunicationType, portInterfaceCommunicationSynchronizationType);
		ProvidedPortInterface p = architecturalModel.getModelProvidedPortInterfacesMap()
				.get(providedPortInterfaceSignature);

		if (p == null)
			throw new PortInterfaceNotFoundException(providedPortInterfaceSignature);
		else
			return p;

	}

	public RequiredPortInterface getRequiredPortInterface(String portInterfaceIdentifier,
			PortInterfaceCommunicationType portInterfaceCommunicationType,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType)
					throws PortInterfaceNotFoundException {
		String requiredPortInterfaceSignature = PortInterface.constructPortInterfaceSignature(portInterfaceIdentifier,
				portInterfaceCommunicationType, portInterfaceCommunicationSynchronizationType);
		RequiredPortInterface r = architecturalModel.getModelRequiredPortInterfacesMap()
				.get(requiredPortInterfaceSignature);

		if (r == null)
			throw new PortInterfaceNotFoundException(requiredPortInterfaceSignature);
		else
			return r;

	}

	public void addCommunicationLink(String providingComponentIdentifier, String requiringComponentIdentifier,
			String innermostProvidingComponentIdentifier, String portInterfaceIdentifier,
			PortInterfaceCommunicationType portInterfaceCommunicationType,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType)
					throws ComponentNotFoundException, PortInterfaceNotDefinedInComponentException,
					PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException {
		HashMap<String, ArchitecturalComponent> componentsIdentifersMap = architecturalModel
				.getModelComponentsIdentifiersMap();
		ArchitecturalComponent providingComponent = componentsIdentifersMap.get(providingComponentIdentifier);
		ArchitecturalComponent requiringComponent = componentsIdentifersMap.get(requiringComponentIdentifier);

		if (providingComponent == null)
			throw new ComponentNotFoundException(providingComponentIdentifier);

		if (requiringComponent == null)
			throw new ComponentNotFoundException(requiringComponentIdentifier);

		ProvidedPortInterface p = getProvidedPortInterface(portInterfaceIdentifier, portInterfaceCommunicationType,
				portInterfaceCommunicationSynchronizationType);
		RequiredPortInterface r = getRequiredPortInterface(portInterfaceIdentifier, portInterfaceCommunicationType,
				portInterfaceCommunicationSynchronizationType);

		HashMap<String, ProvidedPortInterface> providingComponentPortInterfacesMap = providingComponent
				.getProvidedInterfacesMap();
		HashMap<String, RequiredPortInterface> requiringComponentPortInterfacesMap = requiringComponent
				.getRequiredInterfacesMap();

		if (!providingComponentPortInterfacesMap.containsKey(p.getPortInterfaceSignature()))
			throw new PortInterfaceNotDefinedInComponentException(p.getPortInterfaceSignature(), providingComponent);

		if (!requiringComponentPortInterfacesMap.containsKey(r.getPortInterfaceSignature()))
			throw new PortInterfaceNotDefinedInComponentException(r.getPortInterfaceSignature(), requiringComponent);

		ArchitecturalComponent innermostProvidingComponent = null;
		if (innermostProvidingComponentIdentifier != null) {
			innermostProvidingComponent = componentsIdentifersMap.get(innermostProvidingComponentIdentifier);
			if (innermostProvidingComponent == null)
				throw new ComponentNotFoundException(innermostProvidingComponentIdentifier);

			if (!componentIsDescendantOfAnother(innermostProvidingComponent, providingComponent))
				throw new ComponentNotDescendantOfAnotherException(innermostProvidingComponent.getComponentIdentifier(),
						providingComponent.getComponentIdentifier());

			if (!innermostProvidingComponent.getProvidedInterfacesMap().containsKey(p.getPortInterfaceSignature()))
				throw new PortInterfaceNotDefinedInComponentException(p.getPortInterfaceSignature(),
						innermostProvidingComponent);

		}

		CommunicationLink communicationLink = new CommunicationLink(providingComponent, requiringComponent,
				innermostProvidingComponent, p, r);
		architecturalModel.getCommunicationLinks().add(communicationLink);
	}

	private boolean componentIsDescendantOfAnother(ArchitecturalComponent descendantComponent,
			ArchitecturalComponent parentComponent) {
		while ((descendantComponent = descendantComponent.getParentComponent()) != null) {
			if (descendantComponent.equals(parentComponent))
				return true;
		}
		return false;
	}

	public void addConcurrentComponentsEntry(String componentIdentifier,
			ArrayList<String> concurrentComponentsIdentifiers) {
		HashMap<String, ArchitecturalComponent> componentsIdentifersMap = architecturalModel
				.getModelComponentsIdentifiersMap();
		HashSet<ArchitecturalComponent> concurrentComponents = new HashSet<ArchitecturalComponent>();
		for (String identifer : concurrentComponentsIdentifiers) {
			concurrentComponents.add(componentsIdentifersMap.get(identifer));
		}
		architecturalModel.getConcurrentComponentsMap().put(componentsIdentifersMap.get(componentIdentifier),
				new HashSet<ArchitecturalComponent>(concurrentComponents));
	}

}
