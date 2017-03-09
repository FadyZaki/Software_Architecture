package com.architecture.specification.exceptions;

public class BrokenConstraintException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4873994212958128982L;
	
	public BrokenConstraintException(String constraintBreaker, String brokenConstraint) {
		super("\n" + constraintBreaker + " breaks one of its constraints : " + brokenConstraint);
	}

}
