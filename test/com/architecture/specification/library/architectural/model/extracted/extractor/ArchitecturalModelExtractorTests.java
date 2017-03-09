package com.architecture.specification.library.architectural.model.extracted.extractor;


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

import com.architecture.specification.exceptions.ArchitecturalStyleException;
import com.architecture.specification.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.exceptions.ComponentNotFoundException;
import com.architecture.specification.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.exceptions.PortInterfaceNotFoundException;
import com.architecture.specification.model.extracted.extractor.ArchitecturalModelExtractor;
import com.architecture.specification.model.extracted.parser.ImplementationParser;
import com.architecture.specification.model.intended.IntendedArchitecturalModel;
import com.architecture.specification.model.intended.builder.ArchitecturalModelBuilder;
import com.architecture.specification.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.model.intended.constraint.model.IArchitecturalModelConstraint;
import com.architecture.specification.model.intended.portinterface.PortInterface;
import com.architecture.specification.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.style.ArchitecturalStyle;
import com.architecture.specification.util.JavassistFilterUtility;

import static org.mockito.Mockito.atLeast;
	import static org.mockito.Mockito.atLeastOnce;
	import static org.mockito.Mockito.atMost;
	import static org.mockito.Mockito.never;
	import static org.mockito.Mockito.times;
	import static org.mockito.Mockito.verify;

	import javassist.ClassPool;
	import javassist.NotFoundException;

	@RunWith(MockitoJUnitRunner.class)
	public class ArchitecturalModelExtractorTests {

		@Mock
		ImplementationParser mockImplementationParser;
		
		@Mock
		IntendedArchitecturalModel mockIntendedArchitecturalModel;
		
		ArchitecturalModelExtractor architecturalModelExtractor;

		@Before
		public void setup() {
			architecturalModelExtractor = new ArchitecturalModelExtractor(mockImplementationParser);
		}

		@Test
		public void parseImplementationCodeMethodIsCalledAtLeastOnce() throws IOException, NotFoundException {
			// given
			architecturalModelExtractor.extractArchitecturalModelFromImplementation(Mockito.anyList(), Mockito.anyList(), Mockito.anyList(), mockIntendedArchitecturalModel);
				
			// then
			Mockito.verify(mockImplementationParser, atLeastOnce()).parseImplementationCode(Mockito.anyList(), Mockito.anyList(), Mockito.anyList());
			
		}
		
		@Test
		public void getVerifiableClassesMetaDataMethodIsCalledAtLeastOnce() throws IOException, NotFoundException {
			// given
			architecturalModelExtractor.extractArchitecturalModelFromImplementation(Mockito.anyList(), Mockito.anyList(), Mockito.anyList(), mockIntendedArchitecturalModel);
				
			// then
			Mockito.verify(mockImplementationParser, atLeastOnce()).getVeriafiableClassesMetadata();
			
		}
		
		@Test
		public void getBlackboxClassesMetadataMethodIsCalledAtLeastOnce() throws IOException, NotFoundException {
			// given
			architecturalModelExtractor.extractArchitecturalModelFromImplementation(Mockito.anyList(), Mockito.anyList(), Mockito.anyList(), mockIntendedArchitecturalModel);
				
			// then
			Mockito.verify(mockImplementationParser, atLeastOnce()).getBlackboxClassesMetadata();
			
		}
		
		
		@Test
		public void getModelComponentsIdentifiersMapMethodIsCalledAtLeastOnce() throws IOException, NotFoundException {
			// given
			architecturalModelExtractor.extractArchitecturalModelFromImplementation(Mockito.anyList(), Mockito.anyList(), Mockito.anyList(), mockIntendedArchitecturalModel);
				
			// then
			Mockito.verify(mockIntendedArchitecturalModel, atLeastOnce()).getModelComponentsIdentifiersMap();
			
		}
}
