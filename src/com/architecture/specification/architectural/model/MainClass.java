package com.architecture.specification.architectural.model;
import java.io.IOException;

import com.architecture.specification.library.ArchitecturalModelBuilder;
import com.architecture.specification.library.ArchitecturalModelParser;
import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;
import com.architecture.specification.library.exceptions.UnusedComponentException;
import com.architecture.specification.library.exceptions.UnusedRequiredPortInterfaceException;

import japa.parser.ParseException;


public class MainClass {

	public static void main(String[] args) throws IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException, IOException, ParseException {
		
		ArchitecturalModelBuilder architecturalModelBuilder = new ArchitecturalModelBuilder();
		CustomArchitecturalModelInitializer customArchitecturalModelInitializer = new CustomArchitecturalModelInitializer(architecturalModelBuilder);
		architecturalModelBuilder.buildArchitecturalModel(customArchitecturalModelInitializer);
		
		String sourceFilesDirectory = "/src";
		ArchitecturalModelParser.verifyAgainstImplementation(sourceFilesDirectory, architecturalModelBuilder.getArchitecturalModel());
		
	}
}
