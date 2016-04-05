package com.architecture.specification.library;

import java.util.HashSet;

public abstract class PortInterface {

	private String portInterfaceIdentifier;
	private ArchitecturalComponent ownerComponent;
	private PortInterfaceCommunicationType portInterfaceCommunicationType;


	public PortInterface(String portInterfaceIdentifier, PortInterfaceCommunicationType portInterfaceCommunicationType) {
		this.portInterfaceIdentifier = portInterfaceIdentifier;
		this.portInterfaceCommunicationType = portInterfaceCommunicationType;
	}

	public void setOwnerComponent(ArchitecturalComponent ownerComponent) {
		this.ownerComponent = ownerComponent;
	}
	
	public ArchitecturalComponent getOwnerComponent() {
		return ownerComponent;
	}

	public String getPortInterfaceIdentifier() {
		return portInterfaceIdentifier;
	}

	public PortInterfaceCommunicationType getPortInterfaceCommunicationType() {
		return portInterfaceCommunicationType;
	}

	public abstract PortInterfaceType getPortInterfaceType();

}
