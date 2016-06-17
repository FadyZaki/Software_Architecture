package com.architecture.specification.library.architectural.model.intended;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import com.architecture.specification.library.architectural.model.intended.communication.link.CommunicationLink;
import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.library.architectural.model.intended.constraint.model.IArchitecturalModelConstraint;
import com.architecture.specification.library.architectural.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.library.architectural.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.library.architectural.style.ArchitecturalStyle;
import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;

public class IntendedArchitecturalModel {

	private String modelIdentifier;
	private HashMap<String, ArchitecturalComponent> modelComponentsIdentifiersMap;
	private HashSet<CommunicationLink> communicationLinks;

	private HashMap<String, ProvidedPortInterface> modelProvidedPortInterfacesMap;
	private HashMap<String, RequiredPortInterface> modelRequiredPortInterfacesMap;
	
	private List<IArchitecturalModelConstraint> modelConstraints;
	private List<ArchitecturalStyle> modelCompliantStyles;


	public IntendedArchitecturalModel(String modelIdentifier) {
		this.modelIdentifier = modelIdentifier;
		modelComponentsIdentifiersMap = new HashMap<String, ArchitecturalComponent>();
		communicationLinks = new HashSet<CommunicationLink>();
		modelProvidedPortInterfacesMap = new HashMap<String, ProvidedPortInterface>();
		modelRequiredPortInterfacesMap = new HashMap<String, RequiredPortInterface>();
		modelConstraints = new ArrayList<IArchitecturalModelConstraint>();
		modelCompliantStyles = new ArrayList<ArchitecturalStyle>();
	}

	
	public String getModelIdentifier() {
		return modelIdentifier;
	}

	public HashMap<String, ArchitecturalComponent> getModelComponentsIdentifiersMap() {
		return modelComponentsIdentifiersMap;
	}

	public HashSet<CommunicationLink> getCommunicationLinks() {
		return communicationLinks;
	}

	public HashMap<String, ProvidedPortInterface> getModelProvidedPortInterfacesMap() {
		return modelProvidedPortInterfacesMap;
	}

	public HashMap<String, RequiredPortInterface> getModelRequiredPortInterfacesMap() {
		return modelRequiredPortInterfacesMap;
	}
	
	public List<IArchitecturalModelConstraint> getModelConstraints() {
		return modelConstraints;
	}

	public List<ArchitecturalStyle> getModelCompliantStyles() {
		return modelCompliantStyles;
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

	public HashSetValuedHashMap<ArchitecturalComponent, CommunicationLink> getRequiringComponentCommunicationLinksMap() {
		HashSetValuedHashMap<ArchitecturalComponent, CommunicationLink> requiredPortInterfacesCommunicationLinksMap = new HashSetValuedHashMap<ArchitecturalComponent, CommunicationLink>();
		for (CommunicationLink c : communicationLinks) {
			requiredPortInterfacesCommunicationLinksMap.put(c.getRequiringComponent(), c);
		}
		return requiredPortInterfacesCommunicationLinksMap;
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
