package com.architecture.specification.model.intended.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.architecture.specification.model.intended.constraint.component.IArchitecturalComponentConstraint;
import com.architecture.specification.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.style.component.type.ArchitecturalComponentType;

/**
 * Defines an architectural component in terms classes, port interfaces and constraints
 * @author FadyShining
 *
 */
public class ArchitecturalComponent {

	private String componentIdentifier;
	private ArchitecturalComponent parentComponent;
	private HashSet<ArchitecturalComponent> childrenComponents;
	private ArrayList<String> componentClasses;
	private HashSet<ProvidedPortInterface> providedInterfaces;
	private HashSet<RequiredPortInterface> requiredInterfaces;
	private HashSet<ArchitecturalComponentType> componentTypes;
	private ArrayList<IArchitecturalComponentConstraint> componentConstraints;

	public ArchitecturalComponent(String componentIdentifier, ArchitecturalComponent parentComponent,
			ArrayList<String> componentClasses, HashSet<ProvidedPortInterface> providedInterfaces,
			HashSet<RequiredPortInterface> requiredInterfaces, HashSet<ArchitecturalComponentType> componentTypes, ArrayList<IArchitecturalComponentConstraint> componentConstraints) {
		this.componentIdentifier = componentIdentifier;
		this.parentComponent = parentComponent;
		this.childrenComponents = new HashSet<ArchitecturalComponent>();
		this.componentClasses = componentClasses != null ? componentClasses : new ArrayList<String>();
		this.providedInterfaces = providedInterfaces != null ? providedInterfaces : new HashSet<ProvidedPortInterface>();
		this.requiredInterfaces = requiredInterfaces != null ? requiredInterfaces : new HashSet<RequiredPortInterface>();
		this.componentTypes = componentTypes != null ? componentTypes : new HashSet<ArchitecturalComponentType>();
		this.componentConstraints = componentConstraints != null ? componentConstraints : new ArrayList<IArchitecturalComponentConstraint>();
		for (ArchitecturalComponentType componentType : this.componentTypes)
			this.componentConstraints.addAll(componentType.getComponentTypeConstraints());
	}

	public String getComponentIdentifier() {
		return componentIdentifier;
	}

	public ArrayList<String> getComponentClasses() {
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

	public HashSet<ArchitecturalComponentType> getComponentTypes() {
		return componentTypes;
	}

	public ArrayList<IArchitecturalComponentConstraint> getComponentConstraints() {
		return componentConstraints;
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
