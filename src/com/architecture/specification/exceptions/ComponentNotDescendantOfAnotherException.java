package com.architecture.specification.exceptions;

public class ComponentNotDescendantOfAnotherException extends Exception {

	private static final long serialVersionUID = 2518702661537025915L;

	public ComponentNotDescendantOfAnotherException(String descendantComponentIdentifier,
			String parentComponentIdentifier) {
		super("Component " + descendantComponentIdentifier + " is not a descendant of the " + parentComponentIdentifier + " component!" );
	}

}
