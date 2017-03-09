package com.architecture.specification.model.intended.constraint.model;

import java.util.HashSet;
import java.util.List;

import com.architecture.specification.model.intended.IntendedArchitecturalModel;
import com.architecture.specification.model.intended.communication.link.CommunicationLink;
import com.architecture.specification.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.model.intended.portinterface.PortInterfaceCommunicationSynchronizationType;
import com.architecture.specification.model.intended.portinterface.PortInterfaceType;
import com.architecture.specification.style.component.type.ArchitecturalComponentType;
import com.architecture.specification.style.component.type.InMemoryArchitecturalComponentTypes;

public class ConnectedComponentTypesConstraint implements IArchitecturalModelConstraint {

	private ArchitecturalComponentType firstComponentType;
	private ArchitecturalComponentType secondComponentType;
	private PortInterfaceCommunicationSynchronizationType syncType;

	public ConnectedComponentTypesConstraint(String firstComponentType, String secondComponentType, PortInterfaceCommunicationSynchronizationType syncType) {
		this.firstComponentType = InMemoryArchitecturalComponentTypes.getInMemoryComponentTypes().get(firstComponentType);
		this.secondComponentType = InMemoryArchitecturalComponentTypes.getInMemoryComponentTypes().get(secondComponentType);
		this.syncType = syncType;
	}

	@Override
	public boolean verify(IntendedArchitecturalModel modelToBeVerified) {
		HashSet<ArchitecturalComponent> componentsFromFirstType = new HashSet<ArchitecturalComponent>();
		HashSet<ArchitecturalComponent> componentsFromSecondType = new HashSet<ArchitecturalComponent>();

		for (ArchitecturalComponent c : modelToBeVerified.getModelComponentsIdentifiersMap().values()) {
			HashSet<ArchitecturalComponentType> componentTypes = c.getComponentTypes();
			if (componentTypes.contains(firstComponentType))
				componentsFromFirstType.add(c);
			else if (componentTypes.contains(secondComponentType))
				componentsFromSecondType.add(c);
		}

		HashSet<ArchitecturalComponent> componentsFromFirstTypeConnectedToSecondType = new HashSet<ArchitecturalComponent>();
		HashSet<ArchitecturalComponent> componentsFromSecondTypeConnectedToFirstType = new HashSet<ArchitecturalComponent>();
		for (CommunicationLink cl : modelToBeVerified.getCommunicationLinks()) {
			if (syncType == null || cl.getProvidedPortInterface().getPortInterfaceCommunicationSynchronizationType() == syncType) {

				if (componentsFromFirstType.contains(cl.getProvidingComponent()) && componentsFromSecondType.contains(cl.getRequiringComponent())) {
					componentsFromFirstTypeConnectedToSecondType.add(cl.getProvidingComponent());
					componentsFromSecondTypeConnectedToFirstType.add(cl.getRequiringComponent());
				}

				if (componentsFromSecondType.contains(cl.getProvidingComponent()) && componentsFromFirstType.contains(cl.getRequiringComponent())) {
					componentsFromSecondTypeConnectedToFirstType.add(cl.getProvidingComponent());
					componentsFromFirstTypeConnectedToSecondType.add(cl.getRequiringComponent());
				}
			}
		}

		return (componentsFromFirstType.equals(componentsFromFirstTypeConnectedToSecondType)
				&& componentsFromSecondType.equals(componentsFromSecondTypeConnectedToFirstType));
	}

}
