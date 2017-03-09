package com.architecture.specification.exceptions;

import com.architecture.specification.model.intended.component.ArchitecturalComponent;

public class BlackBoxCommunicationLinkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -341089121404303060L;
	
	public BlackBoxCommunicationLinkException(String providingComponentIdentifier, String requiringComponentIdentifier) {
		super("A communication link exists between two blackbox components " + providingComponentIdentifier + " and " + requiringComponentIdentifier);
	}

}
