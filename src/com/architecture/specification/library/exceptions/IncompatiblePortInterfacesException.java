package com.architecture.specification.library.exceptions;

public class IncompatiblePortInterfacesException extends Exception {

	private static final long serialVersionUID = 3189163079177967932L;
	private static final String INCOMPATIBLE_PORT_INTERFACES = "These two port interfaces are not compatible: ";

	public IncompatiblePortInterfacesException(String providedPortInterfaceIdentifier, String requiredPortInterfaceIdentifier) {
		super(INCOMPATIBLE_PORT_INTERFACES + " Provided Interface: " + providedPortInterfaceIdentifier
				+ " Required Interface: " + requiredPortInterfaceIdentifier);
	}
}
