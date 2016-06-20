package com.architecture.specification.architectural.model.custom;

import java.util.ArrayList;

import com.architecture.specification.library.architectural.model.intended.constraint.component.IArchitecturalComponentConstraint;
import com.architecture.specification.library.architectural.model.intended.constraint.model.IArchitecturalModelConstraint;
import com.architecture.specification.library.architectural.style.builder.ArchitecturalStylesBuilder;
import com.architecture.specification.library.architectural.style.component.type.ArchitecturalComponentType;
import com.architecture.specification.library.architectural.style.initializer.ArchitecturalStylesInitializer;

public class CustomArchitecturalStylesInitializer extends ArchitecturalStylesInitializer {

	ArchitecturalStylesBuilder builder;
	
	public CustomArchitecturalStylesInitializer(ArchitecturalStylesBuilder architecturalStylesBuilder) {
		super(architecturalStylesBuilder);
	}

	@Override
	public void initializeCustomComponentTypes() {
		builder.addComponentType("Controller", new ArrayList<IArchitecturalComponentConstraint>());
	}

	@Override
	public void initializeCustomStyles() {
		builder.addArchitecturalStyle("MVC", new ArrayList<IArchitecturalModelConstraint>());
	}

}
