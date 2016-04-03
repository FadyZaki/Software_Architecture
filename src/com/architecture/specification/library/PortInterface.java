package com.architecture.specification.library;

import java.util.HashSet;

public abstract class PortInterface {

	private final String portInterfaceIdentifier;
	private final PortInterfaceCommunicationType portInterfaceCommunicationType;


	public PortInterface(String portInterfaceName, PortInterfaceCommunicationType portInterfaceCommunicationType) {
		this.portInterfaceIdentifier = portInterfaceName;
		this.portInterfaceCommunicationType = portInterfaceCommunicationType;
	}

	public String getPortInterfaceIdentifier() {
		return portInterfaceIdentifier;
	}

	public PortInterfaceCommunicationType getPortInterfaceCommunicationType() {
		return portInterfaceCommunicationType;
	}

	public abstract PortInterfaceType getPortInterfaceType();

}
