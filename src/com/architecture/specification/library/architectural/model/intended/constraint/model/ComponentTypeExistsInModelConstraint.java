package com.architecture.specification.library.architectural.model.intended.constraint.model;

import java.util.HashSet;
import java.util.List;

import com.architecture.specification.library.architectural.model.intended.IntendedArchitecturalModel;
import com.architecture.specification.library.architectural.model.intended.communication.link.CommunicationLink;
import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.library.architectural.style.component.type.ArchitecturalComponentType;
import com.architecture.specification.library.architectural.style.component.type.InMemoryArchitecturalComponentTypes;
import com.architecture.specification.library.exceptions.BrokenConstraintException;

public class ComponentTypeExistsInModelConstraint implements IArchitecturalModelConstraint {

	private ArchitecturalComponentType componentType;

	public ComponentTypeExistsInModelConstraint(String componentType) {
		this.componentType = InMemoryArchitecturalComponentTypes.getInMemoryComponentTypes().get(componentType);
	}

	@Override
	public boolean verify(IntendedArchitecturalModel modelToBeVerified) {
		if (componentType == null)
			return false;

		for (ArchitecturalComponent c : modelToBeVerified.getModelComponentsIdentifiersMap().values()) {
			HashSet<ArchitecturalComponentType> componentTypes = c.getComponentTypes();
			if (componentTypes.contains(componentType))
				return true;
		}
		return false;
	}

}
