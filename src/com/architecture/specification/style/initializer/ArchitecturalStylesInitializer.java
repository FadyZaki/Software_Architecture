package com.architecture.specification.style.initializer;

import com.architecture.specification.style.builder.ArchitecturalStylesBuilder;

public abstract class ArchitecturalStylesInitializer implements IArchitecturalStylesInitializer {

	protected ArchitecturalStylesBuilder architecturalStylesBuilder;

	public ArchitecturalStylesInitializer(ArchitecturalStylesBuilder architecturalStylesBuilder) {
		this.architecturalStylesBuilder = architecturalStylesBuilder;
	}
}
