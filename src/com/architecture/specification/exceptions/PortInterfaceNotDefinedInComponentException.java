package com.architecture.specification.exceptions;

import com.architecture.specification.model.intended.component.ArchitecturalComponent;

public class PortInterfaceNotDefinedInComponentException extends Exception {

	private static final long serialVersionUID = 184033192480045488L;

	public PortInterfaceNotDefinedInComponentException(String portInterfaceSignature, ArchitecturalComponent component) {
		super("The port interface " + portInterfaceSignature + " was never defined as part of this component " + component.getComponentIdentifier());
	}
}
