package com.architecture.specification.model.intended.communication.link;

import com.architecture.specification.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.model.intended.portinterface.RequiredPortInterface;

public class CommunicationLink {

	private final ArchitecturalComponent providingComponent;
	private final ArchitecturalComponent requiringComponent;
	private final ArchitecturalComponent innermostProvidingComponent;
	private final ProvidedPortInterface providedPortInterface;	
	private final RequiredPortInterface requiredPortInterface;
	
	public CommunicationLink(ArchitecturalComponent providingComponent, ArchitecturalComponent requiringComponent, ArchitecturalComponent innermostProvidingComponent,
			ProvidedPortInterface providedPortInterface, RequiredPortInterface requiredPortInterface) {
		this.providingComponent = providingComponent;
		this.requiringComponent = requiringComponent;
		this.innermostProvidingComponent = innermostProvidingComponent;
		this.providedPortInterface = providedPortInterface;
		this.requiredPortInterface = requiredPortInterface;
	}

	public ArchitecturalComponent getProvidingComponent() {
		return providingComponent;
	}

	public ArchitecturalComponent getRequiringComponent() {
		return requiringComponent;
	}
	
	public ArchitecturalComponent getInnermostProvidingComponent() {
		return innermostProvidingComponent;
	}

	public ProvidedPortInterface getProvidedPortInterface() {
		return providedPortInterface;
	}
	
	public ProvidedPortInterface getPortInterfaceAsProvided() {
		return providedPortInterface;
	}

	public RequiredPortInterface getRequiredPortInterface() {
		return requiredPortInterface;
	}
	
	public RequiredPortInterface getPortInterfaceAsRequired() {
		return requiredPortInterface;
	}
	
	

}
