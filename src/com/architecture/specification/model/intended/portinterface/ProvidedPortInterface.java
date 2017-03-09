package com.architecture.specification.model.intended.portinterface;

import java.util.HashSet;

public class ProvidedPortInterface extends PortInterface {

	public ProvidedPortInterface(String portInterfaceIdentifier,
			PortInterfaceCommunicationType portInterfaceCommunicationType,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType) {
		super(portInterfaceIdentifier, portInterfaceCommunicationType, portInterfaceCommunicationSynchronizationType);
	}

	@Override
	public PortInterfaceType getPortInterfaceType() {
		return PortInterfaceType.PROVIDED;
	}

	public boolean canConnectTo(RequiredPortInterface requiredPortInterface) {
		return (this.getPortInterfaceIdentifier().equals(requiredPortInterface.getPortInterfaceIdentifier())
				&& this.getPortInterfaceCommunicationType() == this.getPortInterfaceCommunicationType()
				&& this.getPortInterfaceCommunicationSynchronizationType()
						== requiredPortInterface.getPortInterfaceCommunicationSynchronizationType());
	}


}
