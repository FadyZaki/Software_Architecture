package com.architecture.specification.library.exceptions;

import com.architecture.specification.library.ArchitecturalComponent;
import com.architecture.specification.library.RequiredPortInterface;

public class UnusedRequiredPortInterfaceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 184033192480045488L;

	public UnusedRequiredPortInterfaceException(RequiredPortInterface r, ArchitecturalComponent c) {
		super("This Port Interface " + r.getPortInterfaceIdentifier() + " that belongs to this component "
				+ c.getComponentIdentifier() + " is never used in any communication link");
	}
}
