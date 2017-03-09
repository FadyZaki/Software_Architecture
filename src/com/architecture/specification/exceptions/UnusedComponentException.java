package com.architecture.specification.exceptions;

import com.architecture.specification.model.intended.component.ArchitecturalComponent;

public class UnusedComponentException extends Exception {

	private static final long serialVersionUID = -8158622145485954130L;

	public UnusedComponentException(ArchitecturalComponent c) {
		super("\nComponent " + c.getComponentIdentifier() + " is never used in any communication link");	
	}
	
	

}
