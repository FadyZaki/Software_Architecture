package com.architecture.specification.architectural.model.custom;

import com.architecture.specification.library.architectural.style.ArchitecturalStylesInitializer;
import com.architecture.specification.library.architectural.style.ArchitecturalStylesBuilder;

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
