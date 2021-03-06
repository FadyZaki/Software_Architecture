package com.architecture.specification.model.intended.portinterface;

import com.architecture.specification.util.HelperConstants;

/**
 * Represents an endpoint of a communication channel provided or required by component
 * @author FadyShining
 *
 */
public abstract class PortInterface {

	private String portInterfaceIdentifier;
	private PortInterfaceCommunicationType portInterfaceCommunicationType;
	private PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType;

	public PortInterface(String portInterfaceIdentifier, PortInterfaceCommunicationType portInterfaceCommunicationType,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType) {
		this.portInterfaceIdentifier = portInterfaceIdentifier;
		this.portInterfaceCommunicationType = portInterfaceCommunicationType;
		this.portInterfaceCommunicationSynchronizationType = portInterfaceCommunicationSynchronizationType;
	}

	public String getPortInterfaceIdentifier() {
		return portInterfaceIdentifier;
	}

	public PortInterfaceCommunicationType getPortInterfaceCommunicationType() {
		return portInterfaceCommunicationType;
	}

	public PortInterfaceCommunicationSynchronizationType getPortInterfaceCommunicationSynchronizationType() {
		return portInterfaceCommunicationSynchronizationType;
	}

//	public String getPortInterfaceSignature() {
//		return portInterfaceIdentifier + HelperConstants.UNDERSCORE_SYMBOL + portInterfaceCommunicationType.toString()
//				+ HelperConstants.UNDERSCORE_SYMBOL + portInterfaceCommunicationSynchronizationType.toString();
//	}
//
//	public static String constructPortInterfaceSignature(String portInterfaceIdentifier,
//			PortInterfaceCommunicationType portInterfaceCommunicationType,
//			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType) {
//		return portInterfaceIdentifier + HelperConstants.UNDERSCORE_SYMBOL + portInterfaceCommunicationType.toString()
//				+ HelperConstants.UNDERSCORE_SYMBOL + portInterfaceCommunicationSynchronizationType.toString();
//	}

	public abstract PortInterfaceType getPortInterfaceType();

	@Override
	public int hashCode() {
		return getPortInterfaceIdentifier().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PortInterface))
			return false;
		if (obj == this)
			return true;

		PortInterface rhs = (PortInterface) obj;
		return (this.getPortInterfaceIdentifier().equals(rhs.getPortInterfaceIdentifier()));
	}

}
