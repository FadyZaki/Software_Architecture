package com.architecture.specification.library.architectural.style;

public abstract class ArchitecturalStylesInitializer implements IArchitecturalStylesInitializer {

	protected ArchitecturalStylesBuilder architecturalStylesBuilder;

	public ArchitecturalStylesInitializer(ArchitecturalStylesBuilder architecturalStylesBuilder) {
		this.architecturalStylesBuilder = architecturalStylesBuilder;
	}
}
