package com.architecture.specification.library.architectural.model.intended.builder;

import static org.junit.Assert.assertEquals;

import com.architecture.specification.library.architectural.style.InMemoryArchitecturalStyles;
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

import com.architecture.specification.library.architectural.model.intended.IntendedArchitecturalModel;
import com.architecture.specification.library.architectural.model.intended.communication.link.CommunicationLink;
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

@RunWith(MockitoJUnitRunner.class)
public class IntendedArchitecturalModelTests {

	IntendedArchitecturalModel intendedArchitecturalModel;

	@Mock
	ArchitecturalComponent mockComponent;

	@Mock
	CommunicationLink mockCommunicationLink;

	@Mock
	IArchitecturalModelConstraint mockModelConstraint;
	
	@Mock
	ArchitecturalStyle mockStyle;

	@Before
	public void setup() {
		intendedArchitecturalModel = new IntendedArchitecturalModel("testModel");
		when(mockComponent.getComponentIdentifier()).thenReturn("testComponent");
	}

	@Test
	public void getModelIdentifierIsEqualToTheIdentifierGivenToTheConstructor() {

		// then
		assertTrue(intendedArchitecturalModel.getModelIdentifier().equals("testModel"));
	}

	@Test
	public void modelComponentsIdentifiersMapIsEmptyAtInitialization() {

		// then
		assertTrue(intendedArchitecturalModel.getModelComponentsIdentifiersMap().isEmpty());
	}

	@Test
	public void modelClassComponentsMapIsEmptyAtInitialization() {

		// then
		assertTrue(intendedArchitecturalModel.getClassComponentsMap().isEmpty());
	}

	@Test
	public void modelCommunicationLinksListIsEmptyAtInitialization() {

		// then
		assertTrue(intendedArchitecturalModel.getCommunicationLinks().isEmpty());
	}

	@Test
	public void modelModelCompliantStylesListIsEmptyAtInitialization() {

		// then
		assertTrue(intendedArchitecturalModel.getModelCompliantStyles().isEmpty());
	}

	@Test
	public void modelConstraintsListIsEmptyAtInitialization() {

		// then
		assertTrue(intendedArchitecturalModel.getModelConstraints().isEmpty());
	}

	@Test
	public void modelContainsAComponentAComponentIsAddedToTheModel() throws ComponentNotFoundException {
		// given
		int sizeBeforeAddition = intendedArchitecturalModel.getModelComponentsIdentifiersMap().keySet().size();

		// given
		intendedArchitecturalModel.getModelComponentsIdentifiersMap().put(mockComponent.getComponentIdentifier(), mockComponent);
		int sizeAfterAddition = intendedArchitecturalModel.getModelComponentsIdentifiersMap().keySet().size();

		// then
		assertEquals(sizeBeforeAddition + 1, sizeAfterAddition);
	}
	
	@Test
	public void modelContainsAConstraintWhenConstraintIsAddedToTheModel() throws ComponentNotFoundException {
		// given
		int sizeBeforeAddition = intendedArchitecturalModel.getModelConstraints().size();

		// given
		intendedArchitecturalModel.getModelConstraints().add(mockModelConstraint);
		int sizeAfterAddition = intendedArchitecturalModel.getModelConstraints().size();

		// then
		assertEquals(sizeBeforeAddition + 1, sizeAfterAddition);
	}
	
	@Test
	public void modelContainsACommunicationLinkWhenCommunicationLinkIsAddedToTheModel() throws ComponentNotFoundException {
		// given
		int sizeBeforeAddition = intendedArchitecturalModel.getCommunicationLinks().size();

		// given
		intendedArchitecturalModel.getCommunicationLinks().add(mockCommunicationLink);
		int sizeAfterAddition = intendedArchitecturalModel.getCommunicationLinks().size();

		// then
		assertEquals(sizeBeforeAddition + 1, sizeAfterAddition);
	}
	
	@Test
	public void modelContainsAStyleWhenStyleIsAddedToTheModel() throws ComponentNotFoundException {
		// given
		int sizeBeforeAddition = intendedArchitecturalModel.getModelCompliantStyles().size();

		// given
		intendedArchitecturalModel.getModelCompliantStyles().add(mockStyle);
		int sizeAfterAddition = intendedArchitecturalModel.getModelCompliantStyles().size();

		// then
		assertEquals(sizeBeforeAddition + 1, sizeAfterAddition);
	}
}
