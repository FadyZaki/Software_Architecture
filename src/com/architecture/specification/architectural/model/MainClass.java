package com.architecture.specification.architectural.model;
import com.architecture.specification.library.ArchitecturalModelBuilder;
import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;
import com.architecture.specification.library.exceptions.UnusedComponentException;
import com.architecture.specification.library.exceptions.UnusedRequiredPortInterfaceException;


public class MainClass {

	public static void main(String[] args) throws IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException {
		
		ArchitecturalModelBuilder architecturalModelBuilder = new ArchitecturalModelBuilder();
		CustomArchitecturalModelInitializer customArchitecturalModelInitializer = new CustomArchitecturalModelInitializer(architecturalModelBuilder);
		architecturalModelBuilder.buildArchitecturalModel(customArchitecturalModelInitializer);
		
	}
}
