package com.architecture.specification.library.architectural.model.constraint.component;

import com.architecture.specification.library.architectural.model.component.ArchitecturalComponent;

public class ComponentRequiredPortsNumberConstraint implements ArchitecturalComponentConstraint {

	private int exactNumberOfRequiredPorts;

	public ComponentRequiredPortsNumberConstraint(int exactNumberOfRequiredPorts) {
		this.exactNumberOfRequiredPorts = exactNumberOfRequiredPorts;
	}

	@Override
	public boolean verify(ArchitecturalComponent componentToBeVerified) {
		return componentToBeVerified.getRequiredInterfaces().size() == exactNumberOfRequiredPorts;
	}

}
