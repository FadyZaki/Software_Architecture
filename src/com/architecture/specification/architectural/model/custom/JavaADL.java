package com.architecture.specification.architectural.model.custom;

import java.io.IOException;

import com.architecture.specification.library.architectural.model.ArchitecturalModelBuilder;
import com.architecture.specification.library.architectural.model.ArchitecturalModelParser;
import com.architecture.specification.library.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.library.exceptions.ComponentNotFoundException;
import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;
import com.architecture.specification.library.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.library.exceptions.PortInterfaceNotFoundException;
import com.architecture.specification.library.exceptions.UnusedComponentException;
import com.architecture.specification.library.exceptions.UnusedRequiredPortInterfaceException;

public class JavaADL {

	public static void main(String[] args)
			throws IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException,
			IOException, ComponentNotFoundException, PortInterfaceNotDefinedInComponentException,
			PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException {

		ArchitecturalModelBuilder architecturalModelBuilder = new ArchitecturalModelBuilder();
		CustomArchitecturalModelInitializer customArchitecturalModelInitializer = new CustomArchitecturalModelInitializer(
				architecturalModelBuilder);
		architecturalModelBuilder.buildArchitecturalModel(customArchitecturalModelInitializer);
		System.out.println("Architectural Model Successfully built");

		String sourceFilesDirectory = "D:/Work/TrialWorkspace/CS5001-150023144-practical-04/bin";
		ArchitecturalModelParser modelParser = new ArchitecturalModelParser(sourceFilesDirectory,
				architecturalModelBuilder.getArchitecturalModel());
		modelParser.verifyAgainstImplementation();

	}
}
