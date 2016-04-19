package com.architecture.specification.library;

import com.architecture.specification.architectural.model.CustomArchitecturalModelInitializer;
import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;
import com.architecture.specification.library.exceptions.UnusedComponentException;
import com.architecture.specification.library.exceptions.UnusedRequiredPortInterfaceException;

public interface IArchitecturalModelBuilder {

	public void buildArchitecturalModel(CustomArchitecturalModelInitializer customArchitecturalModelInitializer)
			throws IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException;
	
}
