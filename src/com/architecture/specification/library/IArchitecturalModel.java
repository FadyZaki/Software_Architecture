package com.architecture.specification.library;

import com.architecture.specification.library.exceptions.IncompatiblePortInterfacesException;

public interface IArchitecturalModel {

	public void buildArchitecturalModel() throws IncompatiblePortInterfacesException;
	
}
