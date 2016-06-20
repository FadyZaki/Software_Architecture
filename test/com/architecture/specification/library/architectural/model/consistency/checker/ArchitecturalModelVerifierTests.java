package com.architecture.specification.library.architectural.model.consistency.checker;



	import static org.junit.Assert.*;
	import static org.mockito.Mockito.when;

	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.HashSet;
	import java.util.List;

	import org.apache.commons.collections4.SetUtils;
	import org.junit.Before;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.mockito.Mock;
	import org.mockito.Mockito;
	import org.mockito.internal.matchers.Any;
	import org.mockito.runners.MockitoJUnitRunner;

import com.architecture.specification.library.architectural.model.extracted.ExtractedArchitecturalModel;
import com.architecture.specification.library.architectural.model.extracted.extractor.ArchitecturalModelExtractor;
import com.architecture.specification.library.architectural.model.extracted.parser.ImplementationParser;
import com.architecture.specification.library.architectural.model.intended.IntendedArchitecturalModel;
	import com.architecture.specification.library.architectural.model.intended.builder.ArchitecturalModelBuilder;
	import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;
	import com.architecture.specification.library.architectural.model.intended.constraint.model.IArchitecturalModelConstraint;
	import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterface;
	import com.architecture.specification.library.architectural.model.intended.portinterface.ProvidedPortInterface;
	import com.architecture.specification.library.architectural.model.intended.portinterface.RequiredPortInterface;
	import com.architecture.specification.library.architectural.style.ArchitecturalStyle;
	import com.architecture.specification.library.exceptions.ArchitecturalStyleException;
	import com.architecture.specification.library.exceptions.ComponentNotDescendantOfAnotherException;
	import com.architecture.specification.library.exceptions.ComponentNotFoundException;
	import com.architecture.specification.library.exceptions.PortInterfaceNotDefinedInComponentException;
	import com.architecture.specification.library.exceptions.PortInterfaceNotFoundException;
import com.architecture.specification.library.exceptions.VerificationException;
import com.architecture.specification.library.util.JavassistFilterUtility;

	import static org.mockito.Mockito.atLeast;
	import static org.mockito.Mockito.atLeastOnce;
	import static org.mockito.Mockito.atMost;
	import static org.mockito.Mockito.never;
	import static org.mockito.Mockito.times;
	import static org.mockito.Mockito.verify;

	import javassist.ClassPool;
	import javassist.NotFoundException;

	@RunWith(MockitoJUnitRunner.class)
	public class ArchitecturalModelVerifierTests {
		
		@Mock
		IntendedArchitecturalModel mockIntendedArchitecturalModel;
		
		@Mock
		ExtractedArchitecturalModel mockExtractedArchitecturalModel;
		
		@Mock
		ArchitecturalComponent mockComponent;
		
		ArchitecturalModelVerifier architecturalModelVerfifier;
		
		@Before
		public void setup() {
			architecturalModelVerfifier = new ArchitecturalModelVerifier(mockIntendedArchitecturalModel, mockExtractedArchitecturalModel);
		}

		@Test
		public void getClassComponentsMapMethodIsCalledAtLeastOnce() throws IOException, NotFoundException, VerificationException {
			// given
			architecturalModelVerfifier.verifySpecificationAgainstImplementation();
			
			// then
			Mockito.verify(mockIntendedArchitecturalModel, atLeastOnce()).getClassComponentsMap();
			
		}
		
		@Test
		public void getModelComponentsIdentifiersMapIsCalledAtLeastOnce() throws IOException, NotFoundException, VerificationException {
			// given
			architecturalModelVerfifier.verifySpecificationAgainstImplementation();
			
			// then
			Mockito.verify(mockIntendedArchitecturalModel, atLeastOnce()).getModelComponentsIdentifiersMap();
			
		}
		
		@Test
		public void getComponentsClassesNamesMethodIsCalledAtLeastOnce() throws IOException, NotFoundException, VerificationException {
			// given
			architecturalModelVerfifier.verifySpecificationAgainstImplementation();
			
			// then
			Mockito.verify(mockExtractedArchitecturalModel, atLeastOnce()).getComponentsClassesNames();
			
		}
}
