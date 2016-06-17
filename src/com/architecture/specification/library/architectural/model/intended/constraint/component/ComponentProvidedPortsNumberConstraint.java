package com.architecture.specification.library.architectural.model.intended.constraint.component;

import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;

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
