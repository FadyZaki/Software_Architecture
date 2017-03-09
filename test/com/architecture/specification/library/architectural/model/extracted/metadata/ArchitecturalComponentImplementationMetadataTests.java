package com.architecture.specification.library.architectural.model.extracted.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections4.SetUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.architecture.specification.exceptions.ArchitecturalStyleException;
import com.architecture.specification.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.exceptions.ComponentNotFoundException;
import com.architecture.specification.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.exceptions.PortInterfaceNotFoundException;
import com.architecture.specification.model.extracted.metadata.ArchitecturalComponentImplementationMetaData;
import com.architecture.specification.model.extracted.metadata.ClassMetaData;
import com.architecture.specification.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.model.intended.constraint.model.IArchitecturalModelConstraint;
import com.architecture.specification.model.intended.portinterface.PortInterface;
import com.architecture.specification.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.style.ArchitecturalStyle;
import com.architecture.specification.style.InMemoryArchitecturalStyles;

@RunWith(MockitoJUnitRunner.class)
public class ArchitecturalComponentImplementationMetadataTests {

	@Mock
	ClassMetaData mockClassMetadata;

	ArchitecturalComponentImplementationMetaData architecturalComponentImplementationMetadata;

	@Before
	public void setup() {
		architecturalComponentImplementationMetadata = new ArchitecturalComponentImplementationMetaData("testComponent");
		when(mockClassMetadata.getFullyQualifiedName()).thenReturn("testClassMetadata");
	}
	
	@Test
	public void modelClassMetadataListIsEmptyAtInitialization() throws ComponentNotFoundException {

		// then
		assertTrue(architecturalComponentImplementationMetadata.getAllComponentImplementedClasses().isEmpty());
	}
	
	@Test
	public void modelDeclaredMethodsIsEmptyAtInitialization() throws ComponentNotFoundException {

		// then
		assertTrue(architecturalComponentImplementationMetadata.getAllComponentDeclaredMethods().isEmpty());
	}
	
	@Test
	public void modelAllComponentProvidedMethodsIsEmptyAtInitialization() throws ComponentNotFoundException {

		// then
		assertTrue(architecturalComponentImplementationMetadata.getAllComponentProvidedMethods().isEmpty());
	}
	
	@Test
	public void modelAllComponentRequiredMethodsIsEmptyAtInitialization() throws ComponentNotFoundException {

		// then
		assertTrue(architecturalComponentImplementationMetadata.getAllComponentRequiredMethods().isEmpty());
	}

	@Test
	public void modelContainsAClassMetaWhenAClassMetadataIsAddedToTheModel() throws ComponentNotFoundException {
		// given
		int sizeBeforeAddition = architecturalComponentImplementationMetadata.getAllComponentImplementedClasses().size();

		// given
		architecturalComponentImplementationMetadata.addClassToComponent(mockClassMetadata);
		int sizeAfterAddition = architecturalComponentImplementationMetadata.getAllComponentImplementedClasses().size();

		// then
		assertEquals(sizeBeforeAddition + 1, sizeAfterAddition);
	}
}
