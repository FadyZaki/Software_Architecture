package com.architecture.specification.library.architectural.style;

import java.util.List;

import com.architecture.specification.library.architectural.model.constraint.component.ArchitecturalComponentConstraint;
import com.architecture.specification.library.architectural.model.constraint.model.IArchitecturalModelConstraint;
import com.architecture.specification.library.architectural.style.component.type.ArchitecturalComponentType;
import com.architecture.specification.library.architectural.style.component.type.InMemoryArchitecturalComponentTypes;

public class ArchitecturalStylesBuilder implements IArchitecturalStylesBuilder {

	public void buildArchitecturalStyles(IArchitecturalStylesInitializer initializer) {

		InMemoryArchitecturalComponentTypes.initializeBasicComponentTypes();
		InMemoryArchitecturalStyles.initializeBasicArchitecturalStyles();
		if (initializer != null) {
			initializer.initializeCustomComponentTypes();
			initializer.initializeCustomStyles();
		}
	}

	public void addComponentType(String componentTypeIdentifier, List<ArchitecturalComponentConstraint> componentTypeConstraints) {
		InMemoryArchitecturalComponentTypes.getInMemoryComponentTypes().put(componentTypeIdentifier,
				new ArchitecturalComponentType(componentTypeIdentifier, componentTypeConstraints));
	}

	public void addArchitecturalStyle(String styleIdentifier, List<IArchitecturalModelConstraint> styleConstraints) {
		InMemoryArchitecturalStyles.getInMemoryModelStyles().put(styleIdentifier, new ArchitecturalStyle(styleIdentifier, styleConstraints));
	}
}
