package com.architecture.specification.library;

import java.util.HashSet;

public class RequiredPortInterface extends PortInterface {

	private PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType;

	public RequiredPortInterface(String portInterfaceIdentifier,
			PortInterfaceCommunicationType portInterfaceCommunicationType,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType) {
		super(portInterfaceIdentifier, portInterfaceCommunicationType);
		this.portInterfaceCommunicationSynchronizationType = portInterfaceCommunicationSynchronizationType;
	}
	
	public RequiredPortInterface(String portInterfaceIdentifier,
			PortInterfaceCommunicationType portInterfaceCommunicationType, ArchitecturalComponent ownerComponent,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType) {
		super(portInterfaceIdentifier, portInterfaceCommunicationType, ownerComponent);
		this.portInterfaceCommunicationSynchronizationType = portInterfaceCommunicationSynchronizationType;
	}

	@Override
	public PortInterfaceType getPortInterfaceType() {
		return PortInterfaceType.REQUIRED;
	}

	public PortInterfaceCommunicationSynchronizationType getPortInterfaceCommunicationSynchronizationType() {
		return portInterfaceCommunicationSynchronizationType;
	}

	@Override
	public int hashCode() {
		return (getPortInterfaceIdentifier().toString() + getPortInterfaceCommunicationSynchronizationType())
				.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PortInterface))
			return false;
		if (obj == this)
			return true;

		RequiredPortInterface rhs = (RequiredPortInterface) obj;
		return (this.getPortInterfaceIdentifier().equals(rhs.getPortInterfaceIdentifier())
				&& this.getPortInterfaceCommunicationSynchronizationType() == rhs
						.getPortInterfaceCommunicationSynchronizationType());
	}

}
