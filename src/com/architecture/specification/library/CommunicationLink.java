package com.architecture.specification.library;

public class CommunicationLink {

	private final ProvidedPortInterface providedPortInterface;
	private final RequiredPortInterface requiredPortInterface;

	public CommunicationLink(ProvidedPortInterface providedPortInterface, RequiredPortInterface requiredPortInterface) {
		this.providedPortInterface = providedPortInterface;
		this.requiredPortInterface = requiredPortInterface;
	}

	public ProvidedPortInterface getProvidedPortInterface() {
		return providedPortInterface;
	}

	public RequiredPortInterface getRequiredPortInterface() {
		return requiredPortInterface;
	}

}
