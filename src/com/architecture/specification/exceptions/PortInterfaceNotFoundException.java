package com.architecture.specification.exceptions;

public class PortInterfaceNotFoundException extends Exception {

	private static final long serialVersionUID = 2760916415950710706L;

	public PortInterfaceNotFoundException(String portInterfaceSignature) {
		super("The port interface " + portInterfaceSignature + " was never defined as part of the model");
	}
}
