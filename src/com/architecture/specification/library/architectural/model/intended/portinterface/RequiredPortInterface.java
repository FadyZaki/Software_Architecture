package com.architecture.specification.library.architectural.model.intended.portinterface;

public class RequiredPortInterface extends PortInterface {


	public RequiredPortInterface(String portInterfaceIdentifier,
			PortInterfaceCommunicationType portInterfaceCommunicationType,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType) {
		super(portInterfaceIdentifier, portInterfaceCommunicationType, portInterfaceCommunicationSynchronizationType);
	}

	@Override
	public PortInterfaceType getPortInterfaceType() {
		return PortInterfaceType.REQUIRED;
	}

}
