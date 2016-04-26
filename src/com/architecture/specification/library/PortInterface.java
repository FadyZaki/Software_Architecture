package com.architecture.specification.library;

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

	public String getPortInterfaceSignature() {
		return portInterfaceIdentifier + HelperConstants.UNDERSCORE_SYMBOL + portInterfaceCommunicationType.toString()
				+ HelperConstants.UNDERSCORE_SYMBOL + portInterfaceCommunicationSynchronizationType.toString();
	}

	public static String constructPortInterfaceSignature(String portInterfaceIdentifier,
			PortInterfaceCommunicationType portInterfaceCommunicationType,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType) {
		return portInterfaceIdentifier + HelperConstants.UNDERSCORE_SYMBOL + portInterfaceCommunicationType.toString()
				+ HelperConstants.UNDERSCORE_SYMBOL + portInterfaceCommunicationSynchronizationType.toString();
	}

	public abstract PortInterfaceType getPortInterfaceType();

	@Override
	public int hashCode() {
		return (getPortInterfaceSignature() + HelperConstants.UNDERSCORE_SYMBOL + getPortInterfaceType()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PortInterface))
			return false;
		if (obj == this)
			return true;

		PortInterface rhs = (PortInterface) obj;
		return (this.getPortInterfaceSignature().equals(rhs.getPortInterfaceSignature())
				&& this.getPortInterfaceType() == rhs.getPortInterfaceType());
	}

}
