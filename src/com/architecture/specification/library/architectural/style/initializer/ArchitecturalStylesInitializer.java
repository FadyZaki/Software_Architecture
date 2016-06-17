package com.architecture.specification.library.architectural.style.initializer;

import com.architecture.specification.library.architectural.style.builder.ArchitecturalStylesBuilder;

public abstract class ArchitecturalStylesInitializer implements IArchitecturalStylesInitializer {

	protected ArchitecturalStylesBuilder architecturalStylesBuilder;

	public ArchitecturalStylesInitializer(ArchitecturalStylesBuilder architecturalStylesBuilder) {
		this.architecturalStylesBuilder = architecturalStylesBuilder;
	}
}
