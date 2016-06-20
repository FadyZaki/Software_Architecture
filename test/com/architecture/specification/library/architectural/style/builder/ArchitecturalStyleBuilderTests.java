package com.architecture.specification.library.architectural.style.builder;

import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections4.SetUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.library.architectural.model.intended.constraint.model.IArchitecturalModelConstraint;
import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterface;
import com.architecture.specification.library.architectural.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.library.architectural.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.library.architectural.style.ArchitecturalStyle;
import com.architecture.specification.library.architectural.style.InMemoryArchitecturalStyles;
import com.architecture.specification.library.architectural.style.component.type.ArchitecturalComponentType;
import com.architecture.specification.library.architectural.style.component.type.InMemoryArchitecturalComponentTypes;
import com.architecture.specification.library.exceptions.ArchitecturalStyleException;
import com.architecture.specification.library.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.library.exceptions.ComponentNotFoundException;
import com.architecture.specification.library.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.library.exceptions.PortInterfaceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArchitecturalStyleBuilderTests {

	ArchitecturalStylesBuilder architecturalStylesBuilder;
	
	@Mock
	ArchitecturalStyle mockStyle;
	
	@Mock
	ArchitecturalComponentType mockComponentType;

	@Before
	public void setup() {
		architecturalStylesBuilder = new ArchitecturalStylesBuilder();
		when(mockStyle.getStyleIdentifier()).thenReturn("testStyle");
		when(mockComponentType.getComponentTypeIdentifier()).thenReturn("testComponentType");
	}
	
	@Test
	public void componentTypesAreDefinedOnlyAfterInitialization() {
		
		//given
		InMemoryArchitecturalComponentTypes.initializeBasicComponentTypes();
		
		// then
		assertFalse(InMemoryArchitecturalComponentTypes.getInMemoryComponentTypes().isEmpty());
	}

	@Test
	public void stylesAreDefinedAfterInitialization() {
		
		//given
		InMemoryArchitecturalStyles.initializeBasicArchitecturalStyles();
		
		// then
		assertFalse(InMemoryArchitecturalStyles.getInMemoryModelStyles().isEmpty());
	}
	
	@Test
	public void customComponentTypeIsPartOfTheSystemAfterAddComponentTypeMethodIsCalled() {
		//given
		InMemoryArchitecturalComponentTypes.initializeBasicComponentTypes();
		int sizeBeforeAddition = InMemoryArchitecturalComponentTypes.getInMemoryComponentTypes().size();
		
		//given
		architecturalStylesBuilder.addComponentType(mockComponentType.getComponentTypeIdentifier(), null);
		int sizeAfterAddition = InMemoryArchitecturalComponentTypes.getInMemoryComponentTypes().size();
		
		// then
		assertEquals(sizeBeforeAddition + 1, sizeAfterAddition);
	}
	
	@Test
	public void customStyleIsPartOfTheSystemAfterAddArchitecturalStyleMethodIsCalled() {
		//given
		InMemoryArchitecturalStyles.initializeBasicArchitecturalStyles();
		int sizeBeforeAddition = InMemoryArchitecturalStyles.getInMemoryModelStyles().size();
		
		//given
		architecturalStylesBuilder.addArchitecturalStyle(mockStyle.getStyleIdentifier(), null);
		int sizeAfterAddition = InMemoryArchitecturalStyles.getInMemoryModelStyles().size();
		
		// then
		assertEquals(sizeBeforeAddition + 1, sizeAfterAddition);
	}
	
	
	
	
}
