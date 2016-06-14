package com.architecture.specification.library.architectural.model.constraint.component;

import com.architecture.specification.library.architectural.model.component.ArchitecturalComponent;

public class ComponentProvidedPortsNumberConstraint implements ArchitecturalComponentConstraint {

	private int exactNumberOfProvidedPorts;

	public ComponentProvidedPortsNumberConstraint(int exactNumberOfProvidedPorts) {
		this.exactNumberOfProvidedPorts = exactNumberOfProvidedPorts;
	}

	@Override
	public boolean verify(ArchitecturalComponent componentToBeVerified) {
		return componentToBeVerified.getProvidedInterfaces().size() == exactNumberOfProvidedPorts;
	}
}
