package com.architecture.specification.library;

public interface IArchitecturalModelInitializer {

	public void initializeArchitecturalComponents();

	public void initializeComponentsCommunicationLinks();

	public void initializeConcurrentComponentsMap();
}
