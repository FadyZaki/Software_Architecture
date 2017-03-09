package com.architecture.specification.model.intended.constraint.component;

import com.architecture.specification.model.intended.component.ArchitecturalComponent;

public class ComponentMinimumRequiredPortsNumberConstraint implements IArchitecturalComponentConstraint {

	private int minimumNumberOfRequiredPorts;

	public ComponentMinimumRequiredPortsNumberConstraint(int minimumNumberOfRequiredPorts) {
		this.minimumNumberOfRequiredPorts = minimumNumberOfRequiredPorts;
	}

	@Override
	public boolean verify(ArchitecturalComponent componentToBeVerified) {
		return componentToBeVerified.getRequiredInterfaces().size() >= minimumNumberOfRequiredPorts;
	}

}
