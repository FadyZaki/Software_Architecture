package com.architecture.specification.exceptions;

import com.architecture.specification.model.intended.component.ArchitecturalComponent;

public class UnusedRequiredPortInterfaceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 184033192480045488L;

	public UnusedRequiredPortInterfaceException(ArchitecturalComponent c) {
		super("\nOne or more of the required ports of the component "
				+ c.getComponentIdentifier() + " are never used in any communication link");
	}
}
