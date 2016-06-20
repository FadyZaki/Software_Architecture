package com.architecture.specification.library.architectural.style.builder;

import java.util.List;

import com.architecture.specification.library.architectural.model.intended.constraint.component.IArchitecturalComponentConstraint;
import com.architecture.specification.library.architectural.model.intended.constraint.model.IArchitecturalModelConstraint;
import com.architecture.specification.library.architectural.style.ArchitecturalStyle;
import com.architecture.specification.library.architectural.style.InMemoryArchitecturalStyles;
import com.architecture.specification.library.architectural.style.component.type.ArchitecturalComponentType;
import com.architecture.specification.library.architectural.style.component.type.InMemoryArchitecturalComponentTypes;
import com.architecture.specification.library.architectural.style.initializer.IArchitecturalStylesInitializer;

public class ArchitecturalStylesBuilder implements IArchitecturalStylesBuilder {

	public void buildArchitecturalStyles(IArchitecturalStylesInitializer initializer) {

		InMemoryArchitecturalComponentTypes.initializeBasicComponentTypes();
		InMemoryArchitecturalStyles.initializeBasicArchitecturalStyles();
		if (initializer != null) {
			initializer.initializeCustomComponentTypes();
			initializer.initializeCustomStyles();
		}
	}

	public void addComponentType(String componentTypeIdentifier, List<IArchitecturalComponentConstraint> componentTypeConstraints) {
		InMemoryArchitecturalComponentTypes.getInMemoryComponentTypes().put(componentTypeIdentifier,
				new ArchitecturalComponentType(componentTypeIdentifier, componentTypeConstraints));
	}

	public void addArchitecturalStyle(String styleIdentifier, List<IArchitecturalModelConstraint> styleConstraints) {
		InMemoryArchitecturalStyles.getInMemoryModelStyles().put(styleIdentifier, new ArchitecturalStyle(styleIdentifier, styleConstraints));
	}
}
