package com.architecture.specification.architectural.model;

import com.architecture.specification.library.ArchitecturalModel;
import com.architecture.specification.library.ArchitecturalModelBuilder;
import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;


public class MainClass {

	public static void main(String[] args) throws IncompatiblePortInterfacesException {
		
		ArchitecturalModelBuilder architecturalModelBuilder = new ArchitecturalModelBuilder();
		CustomArchitecturalModelInitializer customArchitecturalModelInitializer = new CustomArchitecturalModelInitializer(architecturalModelBuilder);
		architecturalModelBuilder.buildArchitecturalModel(customArchitecturalModelInitializer);
		
	}
}
