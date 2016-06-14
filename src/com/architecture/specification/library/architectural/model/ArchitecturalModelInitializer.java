package com.architecture.specification.library.architectural.model;

public abstract class ArchitecturalModelInitializer implements IArchitecturalModelInitializer  {

	protected ArchitecturalModelBuilder builder;
	
	public ArchitecturalModelInitializer(ArchitecturalModelBuilder builder) {
		this.builder = builder;
	}
}
