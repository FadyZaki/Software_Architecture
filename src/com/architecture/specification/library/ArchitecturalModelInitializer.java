package com.architecture.specification.library;

public abstract class ArchitecturalModelInitializer implements IArchitecturalModelInitializer  {

	protected ArchitecturalModelBuilder architecturalModelBuilder;
	
	public ArchitecturalModelInitializer(ArchitecturalModelBuilder architecturalModelBuilder) {
		this.architecturalModelBuilder = architecturalModelBuilder;
	}
}
