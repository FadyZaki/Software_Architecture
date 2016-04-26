package com.architecture.specification.library.exceptions;

public class ComponentNotFoundException extends Exception {

	private static final long serialVersionUID = 184033192480045488L;

	public ComponentNotFoundException(String componentIdentifier) {
		super("The component " + componentIdentifier + " was never defined as part of the model");
	}
}
