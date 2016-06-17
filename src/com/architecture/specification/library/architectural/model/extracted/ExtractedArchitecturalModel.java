package com.architecture.specification.library.architectural.model.extracted;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.architecture.specification.library.architectural.model.extracted.metadata.ArchitecturalComponentImplementationMetaData;
import com.architecture.specification.library.architectural.model.extracted.metadata.ClassMetaData;

public class ExtractedArchitecturalModel {

	
	HashMap<String, ArchitecturalComponentImplementationMetaData> actualImplementedComponents;
	
	Set<String> implementedClassesNotIntended;
	Set<String> methodCallsToBlackBoxClassesNotPartOfAnyComponent;

	public ExtractedArchitecturalModel() {
		super();
		actualImplementedComponents = new HashMap<String, ArchitecturalComponentImplementationMetaData>();
		implementedClassesNotIntended = new HashSet<String>();
		methodCallsToBlackBoxClassesNotPartOfAnyComponent = new HashSet<String>();
	}
	
	public Set<String> getImplementedClassesNotIntended() {
		return implementedClassesNotIntended;
	}

	public Set<String> getMethodCallsToBlackBoxClassesNotPartOfAnyComponent() {
		return methodCallsToBlackBoxClassesNotPartOfAnyComponent;
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
