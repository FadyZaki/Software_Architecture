package com.architecture.specification.library.exceptions;

public class ComponentClassNotFoundException extends Exception {

	public ComponentClassNotFoundException(String classname) {
		super("The class " + classname + " is not available as part of the implementation");
	}
}
