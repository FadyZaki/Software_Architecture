package com.architecture.specification.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;

public abstract class ArchitecturalModel implements IArchitecturalModel {

	private HashMap<String, ArchitecturalComponent> modelComponentsIdentifiersMap = new HashMap<String, ArchitecturalComponent>();
	private HashSet<CommunicationLink> communicationLinks = new HashSet<CommunicationLink>();
	private HashMap<ArchitecturalComponent, HashSet<ArchitecturalComponent>> concurrentComponentsMap = new HashMap<ArchitecturalComponent, HashSet<ArchitecturalComponent>>();

	@Override
	public void buildArchitecturalModel() {
		HashSet<ArchitecturalComponent> modelComponents = new HashSet<ArchitecturalComponent>();
		initializeArchitecturalComponents(modelComponents);
		refineArchitecturalComponents(modelComponents);
		initializeArchitecturalComponentsIdentifiersMap(modelComponents);
		initializeComponentsCommunicationLinks(communicationLinks, modelComponentsIdentifiersMap);
		try {
			verifyCommunicationLinks();
		} catch (IncompatiblePortInterfacesException e) {
			System.out.println(e.getMessage());
		}

			for (ArchitecturalComponent c : modelComponentsIdentifiersMap.values()) {
				concurrentComponentsMap.put(c, new HashSet<ArchitecturalComponent>());
			}
		initializeConcurrentComponentsMap(concurrentComponentsMap, modelComponentsIdentifiersMap);
		refineConcurrentComponentsMap();
	}

	private void verifyCommunicationLinks() throws IncompatiblePortInterfacesException {
		for (CommunicationLink cl : communicationLinks) {
			if (!cl.getProvidedPortInterface().canConnectTo(cl.getRequiredPortInterface()))
				throw new IncompatiblePortInterfacesException(
						cl.getProvidedPortInterface().getPortInterfaceIdentifier(),
						cl.getRequiredPortInterface().getPortInterfaceIdentifier());
		}

	}

	private void refineArchitecturalComponents(HashSet<ArchitecturalComponent> modelComponents) {
		for (ArchitecturalComponent c : modelComponents) {
			ArchitecturalComponent currentComponent = c;
			while (currentComponent.getParentComponent() != null) {
				currentComponent.getParentComponent().addComponentClasses(c.getComponentClasses());
				currentComponent = currentComponent.getParentComponent();
			}
		}
	}

	private void initializeArchitecturalComponentsIdentifiersMap(HashSet<ArchitecturalComponent> modelComponents) {
		for (ArchitecturalComponent c : modelComponents) {
			modelComponentsIdentifiersMap.put(c.getComponentIdentifier(), c);
		}
	}

	private void refineConcurrentComponentsMap() {
		for (Map.Entry<ArchitecturalComponent, HashSet<ArchitecturalComponent>> entry : concurrentComponentsMap
				.entrySet()) {
			ArchitecturalComponent currentComponent = entry.getKey();
			for (ArchitecturalComponent c : entry.getValue()) {
				concurrentComponentsMap.get(c).add(currentComponent);
			}
		}
	}

	protected abstract void initializeArchitecturalComponents(HashSet<ArchitecturalComponent> modelComponents);

	protected abstract void initializeComponentsCommunicationLinks(
			HashSet<CommunicationLink> componentsCommunicationLinks,
			HashMap<String, ArchitecturalComponent> modelComponentsIdentifiersMap);

	protected abstract void initializeConcurrentComponentsMap(
			HashMap<ArchitecturalComponent, HashSet<ArchitecturalComponent>> concurrentComponentsMap,
			HashMap<String, ArchitecturalComponent> modelComponentsIdentifiersMap);

}
