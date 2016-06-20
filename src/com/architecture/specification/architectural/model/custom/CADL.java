package com.architecture.specification.architectural.model.custom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.architecture.specification.library.architectural.model.consistency.checker.ArchitecturalModelVerifier;
import com.architecture.specification.library.architectural.model.extracted.ExtractedArchitecturalModel;
import com.architecture.specification.library.architectural.model.extracted.extractor.ArchitecturalModelExtractor;
import com.architecture.specification.library.architectural.model.extracted.parser.ImplementationParser;
import com.architecture.specification.library.architectural.model.intended.IntendedArchitecturalModel;
import com.architecture.specification.library.architectural.model.intended.builder.ArchitecturalModelBuilder;
import com.architecture.specification.library.architectural.style.builder.ArchitecturalStylesBuilder;
import com.architecture.specification.library.exceptions.ArchitecturalStyleException;
import com.architecture.specification.library.exceptions.BlackBoxCommunicationLinkException;
import com.architecture.specification.library.exceptions.BrokenConstraintException;
import com.architecture.specification.library.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.library.exceptions.ComponentNotFoundException;
import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;
import com.architecture.specification.library.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.library.exceptions.PortInterfaceNotFoundException;
import com.architecture.specification.library.exceptions.UnusedComponentException;
import com.architecture.specification.library.exceptions.UnusedRequiredPortInterfaceException;
import com.architecture.specification.library.exceptions.VerificationException;

public class CADL {

	public static void main(String[] args)
			throws IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException,
			IOException, ComponentNotFoundException, PortInterfaceNotDefinedInComponentException,
			PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException, VerificationException, BrokenConstraintException, ArchitecturalStyleException, BlackBoxCommunicationLinkException {

		ArchitecturalStylesBuilder architecturalStyleBuilder = new ArchitecturalStylesBuilder();
		CustomArchitecturalStylesInitializer customArchitecturalStylesInitializer = new CustomArchitecturalStylesInitializer(
				architecturalStyleBuilder);
		architecturalStyleBuilder.buildArchitecturalStyles(customArchitecturalStylesInitializer);
		
		ArchitecturalModelBuilder architecturalModelBuilder = new ArchitecturalModelBuilder("Social Network Model");
		CustomArchitecturalModelInitializer customArchitecturalModelInitializer = new CustomArchitecturalModelInitializer(
				architecturalModelBuilder);
		IntendedArchitecturalModel intendedArchitecturalModel = architecturalModelBuilder.buildArchitecturalModel(customArchitecturalModelInitializer);
		System.out.println("Architectural Model Successfully Built");
		
		List<String> verifiableClassFiles = Arrays.asList("D:/Work/TrialWorkspace/CS5001-150023144-practical-04/bin");
		List<String> blackboxClassFiles = Arrays.asList("D:/Work/TrialWorkspace/CS5001-150023144-practical-04/ip2location.jar");
		List<String> uncheckedClassFiles = Arrays.asList("C:/Program Files/Java/jdk1.8.0_60/jre/lib/rt.jar");

		ArchitecturalModelExtractor architecturalModelExtractor = new ArchitecturalModelExtractor(new ImplementationParser());
		ExtractedArchitecturalModel extractedArchitecturalModel = architecturalModelExtractor.extractArchitecturalModelFromImplementation(verifiableClassFiles, blackboxClassFiles, uncheckedClassFiles, intendedArchitecturalModel);
		
		
		ArchitecturalModelVerifier verifier = new ArchitecturalModelVerifier(intendedArchitecturalModel, extractedArchitecturalModel);
		verifier.verifySpecificationAgainstImplementation();
		System.out.println("Architectural Model Successfully Verified");

		

	}
}
