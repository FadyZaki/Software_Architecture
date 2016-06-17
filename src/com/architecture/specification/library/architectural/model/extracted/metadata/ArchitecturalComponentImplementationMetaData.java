package com.architecture.specification.library.architectural.model.extracted.metadata;

import java.util.HashMap;

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

public class ArchitecturalComponentImplementationMetaData {

	private String componentIdentifier;
	private HashMap<String, ClassMetaData> componentImplementedClasses;

	public ArchitecturalComponentImplementationMetaData(String componentIdentifier) {
		this.componentIdentifier = componentIdentifier;
		this.componentImplementedClasses = new HashMap<String, ClassMetaData>();
	}

	public String getComponentIdentifier() {
		return componentIdentifier;
	}

	public HashMap<String, ClassMetaData> getComponentImplementedClasses() {
		return componentImplementedClasses;
	}

	public void addClassToComponent(ClassMetaData cmd) {
		componentImplementedClasses.put(cmd.getFullyQualifiedName(), cmd);
	}

	public HashSetValuedHashMap<String, MethodDeclarationMetaData> getComponentDeclaredMethods() {
		HashSetValuedHashMap<String, MethodDeclarationMetaData> componentDeclaredMethods = new HashSetValuedHashMap<String, MethodDeclarationMetaData>();
		for (ClassMetaData cmd : this.getComponentImplementedClasses().values()) {
			componentDeclaredMethods.putAll(cmd.getDeclaredMethodsMap());
		}
		return componentDeclaredMethods;
	}

	public HashSetValuedHashMap<String, MethodDeclarationMetaData> getComponentProvidedMethods() {
		HashSetValuedHashMap<String, MethodDeclarationMetaData> componentProvidedMethods = new HashSetValuedHashMap<String, MethodDeclarationMetaData>();
		for (ClassMetaData cmd : this.getComponentImplementedClasses().values()) {
			componentProvidedMethods.putAll(cmd.getProvidedMethodsMap());
		}
		return componentProvidedMethods;
	}

	public HashSetValuedHashMap<String, MethodCallMetaData> getComponentRequiredMethodsMap() {
		HashSetValuedHashMap<String, MethodCallMetaData> methodCallsMap = new HashSetValuedHashMap<String, MethodCallMetaData>();
		for (ClassMetaData cmd : getComponentImplementedClasses().values()) {
			for (MethodCallMetaData mc : cmd.getMethodCallsMap().values()) {
				// Ignore method calls for classes within the same component
				if (!this.getComponentImplementedClasses().containsKey(mc.getMethodDeclaringClass()))
					methodCallsMap.put(mc.getMethodIdentifier(), mc);
			}
		}
		return methodCallsMap;
	}

	@Override
	public int hashCode() {
		return this.getComponentIdentifier().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ArchitecturalComponentImplementationMetaData))
			return false;
		if (obj == this)
			return true;

		ArchitecturalComponentImplementationMetaData imd = (ArchitecturalComponentImplementationMetaData) obj;
		return this.getComponentIdentifier().equals(imd.getComponentIdentifier());
	}

}
