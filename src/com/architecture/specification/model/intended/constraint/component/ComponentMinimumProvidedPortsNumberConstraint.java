package com.architecture.specification.model.intended.constraint.component;

import com.architecture.specification.model.intended.component.ArchitecturalComponent;

public class ComponentMinimumProvidedPortsNumberConstraint implements IArchitecturalComponentConstraint {

	private int minimumNumberOfProvidedPorts;

	public ComponentMinimumProvidedPortsNumberConstraint(int minimumNumberOfProvidedPorts) {
		this.minimumNumberOfProvidedPorts = minimumNumberOfProvidedPorts;
	}

	@Override
	public boolean verify(ArchitecturalComponent componentToBeVerified) {
		return componentToBeVerified.getProvidedInterfaces().size() >= minimumNumberOfProvidedPorts;
	}
}
