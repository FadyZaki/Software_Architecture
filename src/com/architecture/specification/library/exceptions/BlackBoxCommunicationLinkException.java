package com.architecture.specification.library.exceptions;

import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;

public class BlackBoxCommunicationLinkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -341089121404303060L;
	
	public BlackBoxCommunicationLinkException(String providingComponentIdentifier, String requiringComponentIdentifier) {
		super("A communication link exists between two blackbox components " + providingComponentIdentifier + " and " + requiringComponentIdentifier);
	}

}
