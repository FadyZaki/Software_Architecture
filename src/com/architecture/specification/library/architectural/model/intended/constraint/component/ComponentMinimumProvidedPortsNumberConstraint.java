package com.architecture.specification.library.architectural.model.intended.constraint.component;

import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;

public class ComponentMinimumProvidedPortsNumberConstraint implements IArchitecturalComponentConstraint {

	private int minimumNumberOfProvidedPorts;

	public ComponentMinimumProvidedPortsNumberConstraint(int exactNumberOfProvidedPorts) {
		this.minimumNumberOfProvidedPorts = exactNumberOfProvidedPorts;
	}

	@Override
	public boolean verify(ArchitecturalComponent componentToBeVerified) {
		return componentToBeVerified.getProvidedInterfaces().size() >= minimumNumberOfProvidedPorts;
	}
}
