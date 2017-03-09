package com.architecture.specification.model.extracted.metadata;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import com.architecture.specification.model.intended.component.ArchitecturalComponent;

/**
 * Metadata related to class access
 * @author FadyShining
 *
 */
public class ClassMetaData {

	private String fullyQualifiedName;
	private HashSetValuedHashMap<String, MethodDeclarationMetaData> declaredMethodsMap;
	private HashSetValuedHashMap<String, MethodDeclarationMetaData> providedMethodsMap;
	private HashSet<String> implementedInterfaces;
	private HashSet<String> superClasses;

	public ClassMetaData(String classFullyQualifiedName, HashSetValuedHashMap<String, MethodDeclarationMetaData> declaredMethodsMap,
			HashSetValuedHashMap<String, MethodDeclarationMetaData> providedMethodsMap, HashSet<String> implementedInterfaces, HashSet<String> superClasses) {
		this.fullyQualifiedName = classFullyQualifiedName;
		this.declaredMethodsMap = declaredMethodsMap;
		this.providedMethodsMap = providedMethodsMap;
		this.implementedInterfaces = implementedInterfaces;
		this.superClasses = superClasses;
	}

	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	public HashSetValuedHashMap<String, MethodDeclarationMetaData> getProvidedMethodsMap() {
		return providedMethodsMap;
	}

	public HashSetValuedHashMap<String, MethodDeclarationMetaData> getDeclaredMethodsMap() {
		return declaredMethodsMap;
	}

	public HashSetValuedHashMap<String, MethodCallMetaData> getMethodCallsMap() {
		HashSetValuedHashMap<String, MethodCallMetaData> methodCallsMap = new HashSetValuedHashMap<String, MethodCallMetaData>();
		for (MethodDeclarationMetaData methodDeclaration : getDeclaredMethodsMap().values()) {
			methodCallsMap.putAll(methodDeclaration.getMethodCallsMap());
		}
		
		return methodCallsMap;
	}

	public HashSet<String> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public HashSet<String> getSuperClasses() {
		return superClasses;
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
		return this.getFullyQualifiedName().equals(cmd.getFullyQualifiedName());
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder(this.getFullyQualifiedName());
		buff.append("\n[Declared]");
		buff.append(this.declaredMethodsMap);
		buff.append("\n[Provided]");
		buff.append(this.getProvidedMethodsMap());
		return buff.toString();
	}

}
