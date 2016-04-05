package com.architecture.specification.library;

import java.util.HashSet;

public class ProvidedPortInterface extends PortInterface {

	private final HashSet<PortInterfaceCommunicationSynchronizationType> portInterfaceCommunicationSynchronizationTypes;

	public ProvidedPortInterface(String portInterfaceIdentifier,
			PortInterfaceCommunicationType portInterfaceCommunicationType,
			HashSet<PortInterfaceCommunicationSynchronizationType> portInterfaceCommunicationSynchronizationTypes) {
		super(portInterfaceIdentifier, portInterfaceCommunicationType);
		this.portInterfaceCommunicationSynchronizationTypes = portInterfaceCommunicationSynchronizationTypes;
	}

	@Override
	public PortInterfaceType getPortInterfaceType() {
		return PortInterfaceType.PROVIDED;
	}

	public HashSet<PortInterfaceCommunicationSynchronizationType> getPortInterfaceCommunicationSynchronizationTypes() {
		return portInterfaceCommunicationSynchronizationTypes;
	}

	public boolean canConnectTo(RequiredPortInterface requiredPortInterface) {
		return (this.getPortInterfaceIdentifier().equals(requiredPortInterface.getPortInterfaceIdentifier())
				&& this.getPortInterfaceCommunicationType() == this.getPortInterfaceCommunicationType()
				&& this.getPortInterfaceCommunicationSynchronizationTypes()
						.contains(requiredPortInterface.getPortInterfaceCommunicationSynchronizationType()));
	}

	@Override
	public int hashCode() {
		return getPortInterfaceIdentifier().toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PortInterface))
			return false;
		if (obj == this)
			return true;

		ProvidedPortInterface rhs = (ProvidedPortInterface) obj;
		return this.getPortInterfaceIdentifier().equals(rhs.getPortInterfaceIdentifier());
	}

}
