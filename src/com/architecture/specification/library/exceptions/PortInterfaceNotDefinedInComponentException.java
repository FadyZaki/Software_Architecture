package com.architecture.specification.library.exceptions;

import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;

public class PortInterfaceNotDefinedInComponentException extends Exception {

	private static final long serialVersionUID = 184033192480045488L;

	public PortInterfaceNotDefinedInComponentException(String portInterfaceSignature, ArchitecturalComponent component) {
		super("The port interface " + portInterfaceSignature + " was never defined as part of this component " + component.getComponentIdentifier());
	}
}
