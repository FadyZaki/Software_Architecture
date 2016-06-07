package com.architecture.specification.library.architectural.implementation.metadata;

import java.util.HashMap;
import java.util.HashSet;

public class ArchitecturalModelImplementation {

	
	HashMap<String, ArchitecturalComponentImplementationMetaData> actualImplementedComponents;

	public ArchitecturalModelImplementation() {
		super();
		actualImplementedComponents = new HashMap<String, ArchitecturalComponentImplementationMetaData>();
	}
	
	public HashMap<String, ArchitecturalComponentImplementationMetaData> getActualImplementedComponents() {
		return actualImplementedComponents;
	}

	public void addActualImplementedComponent (ArchitecturalComponentImplementationMetaData componentMetaData) {
		actualImplementedComponents.put(componentMetaData.getComponentIdentifier(), componentMetaData);
	}
	
	public HashSet<String> getComponentsClassesNames() {
		HashSet<String> classNamesSet = new HashSet<String>();
		for (ArchitecturalComponentImplementationMetaData componentMetaData : actualImplementedComponents.values()) {
			for (ClassMetaData cmd : componentMetaData.getComponentImplementedClasses().values()) {
				classNamesSet.add(cmd.getFullyQualifiedName());
			}
		}		
		return classNamesSet;
	}
}
