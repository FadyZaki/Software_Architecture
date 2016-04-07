package com.architecture.specification.library;

import java.util.HashMap;
import java.util.HashSet;

public class ArchitecturalComponent {

	private String componentIdentifier;
	private ArchitecturalComponent parentComponent;
	private HashSet<ArchitecturalComponent> childrenComponents;
	private HashSet<String> componentClasses;
	private HashSet<ProvidedPortInterface> providedInterfaces;
	private HashSet<RequiredPortInterface> requiredInterfaces;

	public ArchitecturalComponent(String componentIdentifier, ArchitecturalComponent parentComponent,
			HashSet<String> componentClasses, HashSet<ProvidedPortInterface> providedInterfaces,
			HashSet<RequiredPortInterface> requiredInterfaces) {
		this.componentIdentifier = componentIdentifier;
		this.parentComponent = parentComponent;
		this.childrenComponents = new HashSet<ArchitecturalComponent>();
		this.componentClasses = componentClasses != null ? componentClasses : new HashSet<String>();
		this.providedInterfaces = providedInterfaces != null ? providedInterfaces : new HashSet<ProvidedPortInterface>();
		this.requiredInterfaces = requiredInterfaces != null ? requiredInterfaces : new HashSet<RequiredPortInterface>();
	}

	public String getComponentIdentifier() {
		return componentIdentifier;
	}

	public HashSet<String> getComponentClasses() {
		return componentClasses;
	}

	public void addComponentClasses(HashSet<String> additionalClasses) {
		componentClasses.addAll(additionalClasses);
	}

	public HashSet<ProvidedPortInterface> getProvidedInterfaces() {
		return providedInterfaces;
	}

	public void addProvidedInterfaces(HashSet<ProvidedPortInterface> additionalPorts) {
		providedInterfaces.addAll(additionalPorts);
	}

	public HashMap<String, ProvidedPortInterface> getProvidedInterfacesMap() {
		HashMap<String, ProvidedPortInterface> providedInterfacesMap = new HashMap<String, ProvidedPortInterface>();
		for (ProvidedPortInterface ppi : providedInterfaces) {
			providedInterfacesMap.put(ppi.getPortInterfaceIdentifier(), ppi);
		}
		return providedInterfacesMap;
	}

	public HashSet<RequiredPortInterface> getRequiredInterfaces() {
		return requiredInterfaces;
	}

	public void addRequiredInterfaces(HashSet<RequiredPortInterface> additionalPorts) {
		requiredInterfaces.addAll(additionalPorts);
	}

	public HashMap<String, RequiredPortInterface> getRequiredInterfacesMap() {
		HashMap<String, RequiredPortInterface> requiredInterfacesMap = new HashMap<String, RequiredPortInterface>();
		for (RequiredPortInterface rpi : requiredInterfaces) {
			requiredInterfacesMap.put(rpi.getPortInterfaceIdentifier(), rpi);
		}
		return requiredInterfacesMap;
	}

	public ArchitecturalComponent getParentComponent() {
		return parentComponent;
	}

	public HashSet<ArchitecturalComponent> getChildrenComponents() {
		return childrenComponents;
	}

	@Override
	public int hashCode() {
		return this.componentIdentifier.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ArchitecturalComponent))
			return false;
		if (obj == this)
			return true;

		ArchitecturalComponent rhs = (ArchitecturalComponent) obj;
		return this.getComponentIdentifier().equals(rhs.componentIdentifier);
	}

}
