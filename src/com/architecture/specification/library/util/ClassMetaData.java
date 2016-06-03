package com.architecture.specification.library.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import com.architecture.specification.library.architectural.model.component.ArchitecturalComponent;

public class ClassMetaData {

	private String fullyQualifiedName;
	private HashSet<String> providedMethods;
	private HashSetValuedHashMap<String, String> requiredMethodsCommunicationsMap;
	
	public ClassMetaData(String classFullyQualifiedName, HashSet<String> providedMethods,
			HashSetValuedHashMap<String, String> classMethodsCommunicationsMap) {
		this.fullyQualifiedName = classFullyQualifiedName;
		this.providedMethods = providedMethods;
		this.requiredMethodsCommunicationsMap = classMethodsCommunicationsMap;
	}
	
	public String getClassFullyQualifiedName() {
		return fullyQualifiedName;
	}

	public HashSet<String> getProvidedMethods() {
		return providedMethods;
	}
	
	public HashSetValuedHashMap<String, String> getRequiredMethodsCommunicationsMap() {
		return requiredMethodsCommunicationsMap;
	}

	@Override
	public int hashCode() {
		return this.fullyQualifiedName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ClassMetaData))
			return false;
		if (obj == this)
			return true;

		ClassMetaData cmd = (ClassMetaData) obj;
		return this.getClassFullyQualifiedName().equals(cmd.getClassFullyQualifiedName());
	}
	
}
