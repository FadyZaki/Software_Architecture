package com.architecture.specification.library.architectural.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.architecture.specification.library.architectural.model.communication.link.CommunicationLink;
import com.architecture.specification.library.architectural.model.component.ArchitecturalComponent;
import com.architecture.specification.library.architectural.model.portinterface.ProvidedPortInterface;
import com.architecture.specification.library.architectural.model.portinterface.RequiredPortInterface;
import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;

public class ArchitecturalModel {

	private HashMap<String, ArchitecturalComponent> modelComponentsIdentifiersMap;
	private HashSet<CommunicationLink> communicationLinks;
	private HashMap<ArchitecturalComponent, HashSet<ArchitecturalComponent>> concurrentComponentsMap;

	private HashMap<String, ProvidedPortInterface> modelProvidedPortInterfacesMap;
	private HashMap<String, RequiredPortInterface> modelRequiredPortInterfacesMap;

	public ArchitecturalModel() {
		modelComponentsIdentifiersMap = new HashMap<String, ArchitecturalComponent>();
		communicationLinks = new HashSet<CommunicationLink>();
		concurrentComponentsMap = new HashMap<ArchitecturalComponent, HashSet<ArchitecturalComponent>>();
		modelProvidedPortInterfacesMap = new HashMap<String, ProvidedPortInterface>();
		modelRequiredPortInterfacesMap = new HashMap<String, RequiredPortInterface>();
	}

	public HashMap<String, ArchitecturalComponent> getModelComponentsIdentifiersMap() {
		return modelComponentsIdentifiersMap;
	}

	public HashSet<CommunicationLink> getCommunicationLinks() {
		return communicationLinks;
	}

	public HashMap<ArchitecturalComponent, HashSet<ArchitecturalComponent>> getConcurrentComponentsMap() {
		return concurrentComponentsMap;
	}

	public HashMap<String, ProvidedPortInterface> getModelProvidedPortInterfacesMap() {
		return modelProvidedPortInterfacesMap;
	}

	public HashMap<String, RequiredPortInterface> getModelRequiredPortInterfacesMap() {
		return modelRequiredPortInterfacesMap;
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

	// NOT_USED
	public HashMap<RequiredPortInterface, Integer> getRequiredPortInterfacesCommunicationLinksCountMap() {
		HashMap<RequiredPortInterface, Integer> requiredPortInterfacesCommunicationLinksCountMap = new HashMap<RequiredPortInterface, Integer>();
		for (CommunicationLink c : communicationLinks) {
			int count = requiredPortInterfacesCommunicationLinksCountMap.containsKey(c.getRequiredPortInterface())
					? requiredPortInterfacesCommunicationLinksCountMap.get(c.getRequiredPortInterface()) : 0;
			requiredPortInterfacesCommunicationLinksCountMap.put(c.getRequiredPortInterface(), ++count);
		}

		return requiredPortInterfacesCommunicationLinksCountMap;
	}

	public HashMap<String, HashSet<ArchitecturalComponent>> getClassComponentsMap() {
		HashMap<String, HashSet<ArchitecturalComponent>> classComponentsMap = new HashMap<String, HashSet<ArchitecturalComponent>>();
		for (ArchitecturalComponent c : modelComponentsIdentifiersMap.values()) {
			for (String classFullName :c.getComponentClasses()) {
				HashSet<ArchitecturalComponent> classComponents = classComponentsMap
						.containsKey(classFullName)
								? classComponentsMap.get(classFullName)
								: new HashSet<ArchitecturalComponent>();
				classComponents.add(c);
				classComponentsMap.put(classFullName, classComponents);
			}
		}

		return classComponentsMap;
	}

}
