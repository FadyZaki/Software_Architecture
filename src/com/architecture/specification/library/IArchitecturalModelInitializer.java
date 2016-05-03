package com.architecture.specification.library;

import com.architecture.specification.library.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.library.exceptions.ComponentNotFoundException;
import com.architecture.specification.library.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.library.exceptions.PortInterfaceNotFoundException;

public interface IArchitecturalModelInitializer {

	public void initializeModelPortInterfaces();

	public void initializeModelArchitecturalComponents() throws ComponentNotFoundException, PortInterfaceNotFoundException;

	public void initializeModelComponentsCommunicationLinks() throws ComponentNotFoundException, PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException;

	public void initializeModelConcurrentComponentsMap();
	
}
