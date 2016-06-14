package com.architecture.specification.library.exceptions;

public class ArchitecturalStyleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2532338421551223462L;

	public ArchitecturalStyleException(String msg, String styleIdentifier) {
		super(msg + styleIdentifier);
	}
}
