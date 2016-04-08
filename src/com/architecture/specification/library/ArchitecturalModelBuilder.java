package com.architecture.specification.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import com.architecture.specification.architectural.model.CustomArchitecturalModelInitializer;
import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;
import com.architecture.specification.library.exceptions.UnusedRequiredPortInterfaceException;

public class ArchitecturalModelBuilder implements IArchitecturalModelBuilder {

	private ArchitecturalModel architecturalModel;

	public ArchitecturalModelBuilder() {
		this.architecturalModel = new ArchitecturalModel();
	}

	@Override
	public void buildArchitecturalModel(CustomArchitecturalModelInitializer initializer)
			throws IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException {

		initializer.initializeArchitecturalComponents();
		refineArchitecturalComponentsHierarchy();

		initializer.initializeComponentsCommunicationLinks();
		verifyCommunicationLinksCompatibility();
		verifyEachRequiredPortIsUsed();

		initializer.initializeConcurrentComponentsMap();
		refineConcurrentComponentsMap();
	}

	private void verifyEachRequiredPortIsUsed() throws UnusedRequiredPortInterfaceException {
		HashMap<RequiredPortInterface, HashSet<ArchitecturalComponent>> usedRequiredPortInterfacesComponentsMap = architecturalModel.getUsedRequiredPortInterfacesComponentsMap();
		for (ArchitecturalComponent c : architecturalModel.getModelComponents()){
			if(c.getParentComponent() == null){
				for (RequiredPortInterface r : c.getRequiredInterfaces()){
					if(!usedRequiredPortInterfacesComponentsMap.get(r).contains(c))
						throw new UnusedRequiredPortInterfaceException(r,c);
				}
			}
		}
		
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

	// post-order traversal
	private void adjustComponentsTree(ArchitecturalComponent root) {
		for (ArchitecturalComponent c : root.getChildrenComponents()) {
			adjustComponentsTree(c);
		}
		for (ArchitecturalComponent c : root.getChildrenComponents()) {
			root.getComponentClasses().addAll(c.getComponentClasses());
			for (ProvidedPortInterface p : c.getProvidedInterfaces()) {
				root.getProvidedInterfaces()
						.add(new ProvidedPortInterface(p.getPortInterfaceIdentifier(),
								p.getPortInterfaceCommunicationType(), root,
								p.getPortInterfaceCommunicationSynchronizationTypes()));
			}

			for (RequiredPortInterface r : c.getRequiredInterfaces()) {
				root.getRequiredInterfaces()
						.add(new RequiredPortInterface(r.getPortInterfaceIdentifier(),
								r.getPortInterfaceCommunicationType(), root,
								r.getPortInterfaceCommunicationSynchronizationType()));
			}
		}
	}

	private void adjustChildrenComponentsForEachComponent() {
		for (ArchitecturalComponent c : architecturalModel.getModelComponents()) {
			if (c.getParentComponent() != null)
				c.getParentComponent().getChildrenComponents().add(c);
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
					|| !Objects.equals(cl.getProvidedPortInterface().getOwnerComponent().getParentComponent(),
							cl.getRequiredPortInterface().getOwnerComponent().getParentComponent()))
				throw new IncompatiblePortInterfacesException(
						cl.getProvidedPortInterface().getPortInterfaceIdentifier(),
						cl.getRequiredPortInterface().getPortInterfaceIdentifier());
			else
				removeUnnecessaryPortInterfaces(cl);
		}

	}

	private void removeUnnecessaryPortInterfaces(CommunicationLink cl) {
		ArchitecturalComponent providingComponent = cl.getProvidedPortInterface().getOwnerComponent();
		ArchitecturalComponent requiringComponent = cl.getRequiredPortInterface().getOwnerComponent();

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
		for (ProvidedPortInterface p : providedInterfaces) {
			p.setOwnerComponent(architecturalComponent);
		}
		for (RequiredPortInterface r : requiredInterfaces) {
			r.setOwnerComponent(architecturalComponent);
		}
		architecturalModel.getModelComponents().add(architecturalComponent);
	}

	public void addCommunicationLink(String providingComponentIdentifier, String requiringComponentIdentifier,
			String portIdentifier) {
		HashMap<String, ArchitecturalComponent> componentsIdentifersMap = architecturalModel
				.getArchitecturalComponentsIdentifiersMap();
		ArchitecturalComponent providingComponent = componentsIdentifersMap.get(providingComponentIdentifier);
		ArchitecturalComponent requiringComponent = componentsIdentifersMap.get(requiringComponentIdentifier);
		CommunicationLink communicationLink = new CommunicationLink(
				providingComponent.getProvidedInterfacesMap().get(portIdentifier),
				requiringComponent.getRequiredInterfacesMap().get(portIdentifier));
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
