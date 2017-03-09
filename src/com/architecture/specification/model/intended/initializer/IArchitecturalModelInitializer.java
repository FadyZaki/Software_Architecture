package com.architecture.specification.model.intended.initializer;

import com.architecture.specification.exceptions.ArchitecturalStyleException;
import com.architecture.specification.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.exceptions.ComponentNotFoundException;
import com.architecture.specification.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.exceptions.PortInterfaceNotFoundException;

/**
 * defines all methods required to be implemented by a custom initializer to build an intended model
 * @author FadyShining
 *
 */
public interface IArchitecturalModelInitializer {

	public void initializeModelPortInterfaces();
	
	public void initializeModelArchitecturalComponents() throws ComponentNotFoundException, PortInterfaceNotFoundException;

	public void initializeModelComponentsCommunicationLinks() throws ComponentNotFoundException, PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException;
	
	public void addConstraintsToModel();
	
	public void addStylesToModel() throws ArchitecturalStyleException;
}
