package com.architecture.specification.architectural.model.custom;

import com.architecture.specification.library.architectural.style.builder.ArchitecturalStylesBuilder;
import com.architecture.specification.library.architectural.style.initializer.ArchitecturalStylesInitializer;

public class CustomArchitecturalStylesInitializer extends ArchitecturalStylesInitializer {

	public CustomArchitecturalStylesInitializer(ArchitecturalStylesBuilder architecturalStylesBuilder) {
		super(architecturalStylesBuilder);
	}

	@Override
	public void initializeCustomComponentTypes() {
		return;
	}

	@Override
	public void initializeCustomStyles() {
		return;
	}

}
