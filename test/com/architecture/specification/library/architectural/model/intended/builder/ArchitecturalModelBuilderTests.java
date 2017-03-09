package com.architecture.specification.library.architectural.model.intended.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections4.SetUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.architecture.specification.exceptions.ArchitecturalStyleException;
import com.architecture.specification.exceptions.BlackBoxCommunicationLinkException;
import com.architecture.specification.exceptions.BrokenConstraintException;
import com.architecture.specification.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.exceptions.ComponentNotFoundException;
import com.architecture.specification.exceptions.IncompatiblePortInterfacesException;
import com.architecture.specification.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.exceptions.PortInterfaceNotFoundException;
import com.architecture.specification.exceptions.UnusedComponentException;
import com.architecture.specification.exceptions.UnusedRequiredPortInterfaceException;
import com.architecture.specification.exceptions.VerificationException;
import com.architecture.specification.model.intended.builder.ArchitecturalModelBuilder;
import com.architecture.specification.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.model.intended.constraint.model.IArchitecturalModelConstraint;
import com.architecture.specification.model.intended.initializer.ArchitecturalModelInitializer;
import com.architecture.specification.model.intended.portinterface.PortInterface;
import com.architecture.specification.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.style.ArchitecturalStyle;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javassist.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class ArchitecturalModelBuilderTests {

	@Mock
	ArchitecturalComponent mockComponent;

	@Mock
	ProvidedPortInterface mockProvidedPortInterface;

	@Mock
	RequiredPortInterface mockRequiredPortInterface;
	
	@Mock
	IArchitecturalModelConstraint mockConstraint;
	
	@Mock
	ArchitecturalStyle mockStyle;
	
	@Mock
	ArchitecturalModelInitializer mockInitializer;

	ArchitecturalModelBuilder architecturalModelBuilder;

	@Before
	public void setup() {
		when(mockComponent.getComponentIdentifier()).thenReturn("testComponent");
		when(mockProvidedPortInterface.getPortInterfaceIdentifier()).thenReturn("testPortInterface");
		when(mockRequiredPortInterface.getPortInterfaceIdentifier()).thenReturn("testPortInterface");
		when(mockStyle.getStyleIdentifier()).thenReturn("testStyle");
	}

	@Test
	public void modelIsEmptyWhenFirstDefined() {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");

		// then
		assertTrue(architecturalModelBuilder.getArchitecturalModel().getModelComponentsIdentifiersMap().keySet().isEmpty());
		assertTrue(architecturalModelBuilder.getArchitecturalModel().getModelConstraints().isEmpty());
		assertTrue(architecturalModelBuilder.getArchitecturalModel().getModelProvidedPortInterfacesMap().keySet().isEmpty());
		assertTrue(architecturalModelBuilder.getArchitecturalModel().getModelRequiredPortInterfacesMap().keySet().isEmpty());
	}

	@Test
	public void modelContainsAComponentWhenAComponentIsAddedToTheModel() throws ComponentNotFoundException {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");

		// when
		architecturalModelBuilder.addComponent(mockComponent.getComponentIdentifier(), null, mockComponent.getComponentClasses(),
				mockComponent.getProvidedInterfaces(), mockComponent.getRequiredInterfaces(), mockComponent.getComponentTypes(),
				mockComponent.getComponentConstraints(), false);

		// then
		assertFalse(architecturalModelBuilder.getArchitecturalModel().getModelComponentsIdentifiersMap().keySet().isEmpty());
	}

	@Test
	public void modelContainsAPortInterfaceWhenAPortInterfaceIsAddedToTheModel() {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");

		// when
		architecturalModelBuilder.addPortInterface(mockRequiredPortInterface.getPortInterfaceIdentifier(),
				mockProvidedPortInterface.getPortInterfaceCommunicationType(), mockRequiredPortInterface.getPortInterfaceCommunicationSynchronizationType());

		// then
		assertFalse(architecturalModelBuilder.getArchitecturalModel().getModelProvidedPortInterfacesMap().keySet().isEmpty());
		assertFalse(architecturalModelBuilder.getArchitecturalModel().getModelRequiredPortInterfacesMap().keySet().isEmpty());

	}
	
	@Test
	public void modelContainsAConstraintWhenAConstraintIsAddedToTheModel() {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");

		// when
		architecturalModelBuilder.addModelConstraint(mockConstraint);

		// then
		assertFalse(architecturalModelBuilder.getArchitecturalModel().getModelConstraints().isEmpty());

	}

	@Test
	public void modelContainsACommunicationLinkWhenACommunicationLinkIsAddedToTheModel() throws ComponentNotFoundException,
			PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");
		architecturalModelBuilder.addPortInterface(mockProvidedPortInterface.getPortInterfaceIdentifier(),
				mockProvidedPortInterface.getPortInterfaceCommunicationType(), mockProvidedPortInterface.getPortInterfaceCommunicationSynchronizationType());
		HashSet<ProvidedPortInterface> ppi = new HashSet<ProvidedPortInterface>();
		ppi.add(mockProvidedPortInterface);
		HashSet<RequiredPortInterface> rpi = new HashSet<RequiredPortInterface>();
		rpi.add(mockRequiredPortInterface);
		architecturalModelBuilder.addComponent(mockComponent.getComponentIdentifier(), null, mockComponent.getComponentClasses(), ppi, rpi,
				mockComponent.getComponentTypes(), mockComponent.getComponentConstraints(), false);
		
		// when
		architecturalModelBuilder.addCommunicationLink(mockComponent.getComponentIdentifier(), mockComponent.getComponentIdentifier(), null,
				mockProvidedPortInterface.getPortInterfaceIdentifier());

		// then
		assertFalse(architecturalModelBuilder.getArchitecturalModel().getCommunicationLinks().isEmpty());

	}

	@Test(expected = ComponentNotFoundException.class)
	public void whenParentComponentIsNotDefinedInTheModeThenAComponentNotFoundExceptionShouldBeThrown() throws ComponentNotFoundException,
			PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");

		architecturalModelBuilder.addComponent(mockComponent.getComponentIdentifier(), mockComponent.getComponentIdentifier(),
				mockComponent.getComponentClasses(), null, null, mockComponent.getComponentTypes(), mockComponent.getComponentConstraints(), false);

		// then
		// ComponentNotFoundException must be thrown
	}

	@Test(expected = PortInterfaceNotFoundException.class)
	public void whenPortInterfaceIsNotDefinedInTheModeThenAPortInterfaceNotFoundExceptionShouldBeThrown() throws PortInterfaceNotFoundException {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");
		
		//when
		architecturalModelBuilder.getProvidedPortInterface(mockProvidedPortInterface.getPortInterfaceIdentifier());

		// then
		// PortInterfaceNotFoundException must be thrown
	}
	
	@Test(expected = PortInterfaceNotDefinedInComponentException.class)
	public void whenProvidedPortInterfaceIsNotDefinedAsPartOfTheProvidingComponentInTheModeThenAPortInterfaceNotDefinedInComponentExceptionShouldBeThrownWhenCommunicationLinkIsDeclared() throws PortInterfaceNotDefinedInComponentException, ComponentNotFoundException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");
		architecturalModelBuilder.addPortInterface(mockProvidedPortInterface.getPortInterfaceIdentifier(),
				mockProvidedPortInterface.getPortInterfaceCommunicationType(), mockProvidedPortInterface.getPortInterfaceCommunicationSynchronizationType());
		HashSet<ProvidedPortInterface> ppi = new HashSet<ProvidedPortInterface>();
		ppi.add(mockProvidedPortInterface);
		HashSet<RequiredPortInterface> rpi = new HashSet<RequiredPortInterface>();
		rpi.add(mockRequiredPortInterface);
		architecturalModelBuilder.addComponent(mockComponent.getComponentIdentifier(), null, mockComponent.getComponentClasses(), null, rpi,
				mockComponent.getComponentTypes(), mockComponent.getComponentConstraints(), false);
		
		
		//when
		architecturalModelBuilder.addCommunicationLink(mockComponent.getComponentIdentifier(), mockComponent.getComponentIdentifier(), null,
				mockProvidedPortInterface.getPortInterfaceIdentifier());

		// then
		// PortInterfaceNotDefinedInComponentException must be thrown
	}
	
	@Test(expected = PortInterfaceNotDefinedInComponentException.class)
	public void whenRequiredPortInterfaceIsNotDefinedAsPartOfTheRequiringComponentInTheModeThenAPortInterfaceNotDefinedInComponentExceptionShouldBeThrownWhenCommunicationLinkIsDeclared() throws PortInterfaceNotDefinedInComponentException, ComponentNotFoundException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");
		architecturalModelBuilder.addPortInterface(mockProvidedPortInterface.getPortInterfaceIdentifier(),
				mockProvidedPortInterface.getPortInterfaceCommunicationType(), mockProvidedPortInterface.getPortInterfaceCommunicationSynchronizationType());
		HashSet<ProvidedPortInterface> ppi = new HashSet<ProvidedPortInterface>();
		ppi.add(mockProvidedPortInterface);
		HashSet<RequiredPortInterface> rpi = new HashSet<RequiredPortInterface>();
		rpi.add(mockRequiredPortInterface);
		architecturalModelBuilder.addComponent(mockComponent.getComponentIdentifier(), null, mockComponent.getComponentClasses(), ppi, null,
				mockComponent.getComponentTypes(), mockComponent.getComponentConstraints(), false);
		
		
		//when
		architecturalModelBuilder.addCommunicationLink(mockComponent.getComponentIdentifier(), mockComponent.getComponentIdentifier(), null,
				mockProvidedPortInterface.getPortInterfaceIdentifier());

		// then
		// PortInterfaceNotDefinedInComponentException must be thrown
	}
	
	@Test
	public void initializeArchitecturalComponentsMethodIsCalledExactlyOnce() throws IOException, NotFoundException, VerificationException, IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException, ComponentNotFoundException, PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException, BrokenConstraintException, ArchitecturalStyleException, BlackBoxCommunicationLinkException {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");
		architecturalModelBuilder.buildArchitecturalModel(mockInitializer);
		
		// then
		Mockito.verify(mockInitializer, times(1)).initializeModelArchitecturalComponents();
		
	}
	
	@Test
	public void initializeModelComponentsCommunicationLinksMethodIsCalledExactlyOnce() throws IOException, NotFoundException, VerificationException, IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException, ComponentNotFoundException, PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException, BrokenConstraintException, ArchitecturalStyleException, BlackBoxCommunicationLinkException {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");
		architecturalModelBuilder.buildArchitecturalModel(mockInitializer);
		
		// then
		Mockito.verify(mockInitializer, times(1)).initializeModelComponentsCommunicationLinks();
		
	}
	
	@Test
	public void initializeModelPortInterfacesMethodIsCalledExactlyOnce() throws IOException, NotFoundException, VerificationException, IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException, ComponentNotFoundException, PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException, BrokenConstraintException, ArchitecturalStyleException, BlackBoxCommunicationLinkException {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");
		architecturalModelBuilder.buildArchitecturalModel(mockInitializer);
		
		// then
		Mockito.verify(mockInitializer, times(1)).initializeModelPortInterfaces();
		
	}
	
	@Test
	public void addStylesToModelMethodIsCalledExactlyOnce() throws IOException, NotFoundException, VerificationException, IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException, ComponentNotFoundException, PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException, BrokenConstraintException, ArchitecturalStyleException, BlackBoxCommunicationLinkException {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");
		architecturalModelBuilder.buildArchitecturalModel(mockInitializer);
		
		// then
		Mockito.verify(mockInitializer, times(1)).addStylesToModel();;
		
	}
	
	@Test
	public void addConstraintsToModelMethodIsCalledExactlyOnce() throws IOException, NotFoundException, VerificationException, IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException, ComponentNotFoundException, PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException, BrokenConstraintException, ArchitecturalStyleException, BlackBoxCommunicationLinkException {
		// given
		architecturalModelBuilder = new ArchitecturalModelBuilder("testBuilder");
		architecturalModelBuilder.buildArchitecturalModel(mockInitializer);
		
		// then
		Mockito.verify(mockInitializer, times(1)).addConstraintsToModel();
		
	}
	
	
	
}
