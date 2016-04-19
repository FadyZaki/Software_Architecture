package com.architecture.specification.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;

public class ArchitecturalModel {

	private HashSet<ArchitecturalComponent> modelComponents;
	private HashSet<CommunicationLink> communicationLinks;
	private HashMap<ArchitecturalComponent, HashSet<ArchitecturalComponent>> concurrentComponentsMap;

	public ArchitecturalModel() {
		modelComponents = new HashSet<ArchitecturalComponent>();
		communicationLinks = new HashSet<CommunicationLink>();
		concurrentComponentsMap = new HashMap<ArchitecturalComponent, HashSet<ArchitecturalComponent>>();
	}

	public HashSet<ArchitecturalComponent> getModelComponents() {
		return modelComponents;
	}

	public HashSet<CommunicationLink> getCommunicationLinks() {
		return communicationLinks;
	}

	public HashMap<ArchitecturalComponent, HashSet<ArchitecturalComponent>> getConcurrentComponentsMap() {
		return concurrentComponentsMap;
	}

	public HashMap<String, ArchitecturalComponent> getArchitecturalComponentsIdentifiersMap() {
		HashMap<String, ArchitecturalComponent> componentsIdentifiersMap = new HashMap<String, ArchitecturalComponent>();
		for (ArchitecturalComponent c : modelComponents) {
			componentsIdentifiersMap.put(c.getComponentIdentifier(), c);
		}

		return componentsIdentifiersMap;
	}

	public HashMap<RequiredPortInterface, HashSet<ArchitecturalComponent>> getUsedRequiredPortInterfacesComponentsMap() {
		HashMap<RequiredPortInterface, HashSet<ArchitecturalComponent>> requiredPortInterfacesComponentsMap = new HashMap<RequiredPortInterface, HashSet<ArchitecturalComponent>>();
		for (CommunicationLink cl : communicationLinks) {
			HashSet<ArchitecturalComponent> requiredPortComponents = requiredPortInterfacesComponentsMap
					.containsKey(cl.getRequiredPortInterface())
							? requiredPortInterfacesComponentsMap.get(cl.getRequiredPortInterface())
							: new HashSet<ArchitecturalComponent>();
			requiredPortComponents.add(cl.getRequiringComponent());
			requiredPortInterfacesComponentsMap.put(cl.getRequiredPortInterface(), requiredPortComponents);
		}

		return requiredPortInterfacesComponentsMap;
	}

	public HashMap<RequiredPortInterface, Integer> getRequiredPortInterfacesCommunicationLinksCountMap() {
		HashMap<RequiredPortInterface, Integer> requiredPortInterfacesCommunicationLinksCountMap = new HashMap<RequiredPortInterface, Integer>();
		for (CommunicationLink c : communicationLinks) {
			int count = requiredPortInterfacesCommunicationLinksCountMap.containsKey(c.getRequiredPortInterface())
					? requiredPortInterfacesCommunicationLinksCountMap.get(c.getRequiredPortInterface()) : 0;
			requiredPortInterfacesCommunicationLinksCountMap.put(c.getRequiredPortInterface(), ++count);
		}

		return requiredPortInterfacesCommunicationLinksCountMap;
	}

}
