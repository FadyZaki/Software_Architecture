package com.architecture.specification.library.exceptions;

import com.architecture.specification.library.ArchitecturalComponent;
import com.architecture.specification.library.RequiredPortInterface;

public class UnusedRequiredPortInterfaceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 184033192480045488L;

	public UnusedRequiredPortInterfaceException(ArchitecturalComponent c) {
		super("One or more of the required port interfaces which belongs to component "
				+ c.getComponentIdentifier() + " are never used in any communication link");
	}
}
