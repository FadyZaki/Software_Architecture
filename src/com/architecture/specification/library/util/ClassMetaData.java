package com.architecture.specification.library.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ClassMetaData {

	private String classFullyQualifiedName;
	private HashSet<String> providedMethods;
	private HashSet<String> requiredMethods;
	
	public ClassMetaData(String classFullyQualifiedName, HashSet<String> providedMethods,
			HashSet<String> requiredMethods) {
		this.classFullyQualifiedName = classFullyQualifiedName;
		this.providedMethods = providedMethods;
		this.requiredMethods = requiredMethods;
	}
	
	public String getClassFullyQualifiedName() {
		return classFullyQualifiedName;
	}

	public HashSet<String> getProvidedMethods() {
		return (HashSet<String>) Collections.unmodifiableSet(providedMethods);
	}

	public HashSet<String> getRequiredMethods() {
		return (HashSet<String>) Collections.unmodifiableSet(requiredMethods);
	}
	
}
