package com.architecture.specification.model.intended.initializer;

import com.architecture.specification.model.intended.builder.ArchitecturalModelBuilder;

public abstract class ArchitecturalModelInitializer implements IArchitecturalModelInitializer  {

	protected ArchitecturalModelBuilder builder;
	
	public ArchitecturalModelInitializer(ArchitecturalModelBuilder builder) {
		this.builder = builder;
	}
}
