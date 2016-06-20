package com.architecture.specification.library.architectural.model.intended.constraint.component;

import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;

public class ComponentMinimumRequiredPortsNumberConstraint implements IArchitecturalComponentConstraint {

	private int minimumNumberOfRequiredPorts;

	public ComponentMinimumRequiredPortsNumberConstraint(int exactNumberOfRequiredPorts) {
		this.minimumNumberOfRequiredPorts = exactNumberOfRequiredPorts;
	}

	@Override
	public boolean verify(ArchitecturalComponent componentToBeVerified) {
		return componentToBeVerified.getRequiredInterfaces().size() >= minimumNumberOfRequiredPorts;
	}

}
