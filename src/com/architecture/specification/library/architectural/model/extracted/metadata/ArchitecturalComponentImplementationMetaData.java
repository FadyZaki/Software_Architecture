package com.architecture.specification.library.architectural.model.extracted.metadata;

import java.util.HashMap;

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;


/**
 * Implemented Component Metadata
 * @author FadyShining
 *
 */
public class ArchitecturalComponentImplementationMetaData {

	private String componentIdentifier;
	private HashMap<String, ClassMetaData> componentImplementedClasses;
	private HashMap<String, ArchitecturalComponentImplementationMetaData> componentChildren;

	public ArchitecturalComponentImplementationMetaData(String componentIdentifier) {
		this.componentIdentifier = componentIdentifier;
		this.componentImplementedClasses = new HashMap<String, ClassMetaData>();
		this.componentChildren = new HashMap<String, ArchitecturalComponentImplementationMetaData>();
	}

	public String getComponentIdentifier() {
		return componentIdentifier;
	}

	public HashMap<String, ClassMetaData> getComponentImplementedClasses() {
		return componentImplementedClasses;
	}

	public HashMap<String, ArchitecturalComponentImplementationMetaData> getComponentChildren() {
		return componentChildren;
	}

	public void addClassToComponent(ClassMetaData cmd) {
		componentImplementedClasses.put(cmd.getFullyQualifiedName(), cmd);
	}

	/**
	 * 
	 * @return all methods declared by a component
	 */
	public HashSetValuedHashMap<String, MethodDeclarationMetaData> getComponentDeclaredMethods() {
		HashSetValuedHashMap<String, MethodDeclarationMetaData> componentDeclaredMethods = new HashSetValuedHashMap<String, MethodDeclarationMetaData>();
		for (ClassMetaData cmd : this.getComponentImplementedClasses().values()) {
			componentDeclaredMethods.putAll(cmd.getDeclaredMethodsMap());
		}
		return componentDeclaredMethods;
	}

	/**
	 * 
	 * @return all methods provided by a component
	 */
	public HashSetValuedHashMap<String, MethodDeclarationMetaData> getComponentProvidedMethods() {
		HashSetValuedHashMap<String, MethodDeclarationMetaData> componentProvidedMethods = new HashSetValuedHashMap<String, MethodDeclarationMetaData>();
		for (ClassMetaData cmd : this.getComponentImplementedClasses().values()) {
			componentProvidedMethods.putAll(cmd.getProvidedMethodsMap());
		}
		return componentProvidedMethods;
	}

	/**
	 * 
	 * @return all methods required by a component
	 */
	public HashSetValuedHashMap<String, MethodCallMetaData> getComponentRequiredMethods() {
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
	
	public HashSetValuedHashMap<String, MethodCallMetaData> getComponentMessagePassingMethods() {
		HashSetValuedHashMap<String, MethodCallMetaData> methodCallsMap = new HashSetValuedHashMap<String, MethodCallMetaData>();
		for (ClassMetaData cmd : getComponentImplementedClasses().values()) {
			for (MethodCallMetaData mc : cmd.getMethodCallsMap().values()) {
				if (mc.getMethodIdentifier().startsWith("sendMessageThrough") || mc.getMethodIdentifier().startsWith("receiveMessageThrough"))
					methodCallsMap.put(mc.getMethodIdentifier(), mc);
			}
		}
		return methodCallsMap;
	}

	/**
	 * 
	 * @return all classes related to a specific component
	 */
	public HashMap<String, ClassMetaData> getAllComponentImplementedClasses() {
		HashMap<String, ClassMetaData> allComponentImplementedClasses = getComponentImplementedClasses();
		for (ArchitecturalComponentImplementationMetaData ac : this.getComponentChildren().values()) {
			allComponentImplementedClasses.putAll(ac.getAllComponentImplementedClasses());
		}
		return allComponentImplementedClasses;
	}

	public HashSetValuedHashMap<String, MethodDeclarationMetaData> getAllComponentDeclaredMethods() {
		HashSetValuedHashMap<String, MethodDeclarationMetaData> allComponentDeclaredMethods = getComponentDeclaredMethods();
		for (ArchitecturalComponentImplementationMetaData ac : this.getComponentChildren().values()) {
			allComponentDeclaredMethods.putAll(ac.getAllComponentDeclaredMethods());
		}
		return allComponentDeclaredMethods;
	}

	/**
	 * 
	 * @return all provided methods including the ones inherited from subcomponents
	 */
	public HashSetValuedHashMap<String, MethodDeclarationMetaData> getAllComponentProvidedMethods() {
		HashSetValuedHashMap<String, MethodDeclarationMetaData> allComponentProvidedMethods = getComponentProvidedMethods();
		for (ArchitecturalComponentImplementationMetaData ac : this.getComponentChildren().values()) {
			allComponentProvidedMethods.putAll(ac.getAllComponentProvidedMethods());
		}
		return allComponentProvidedMethods;
	}

	/**
	 * 
	 * @return all required methods including the ones inherited from subcomponents
	 */
	public HashSetValuedHashMap<String, MethodCallMetaData> getAllComponentRequiredMethods() {
		HashSetValuedHashMap<String, MethodCallMetaData> allComponentMethodCallsMap = getComponentRequiredMethods();
		for (ArchitecturalComponentImplementationMetaData ac : this.getComponentChildren().values()) {
			allComponentMethodCallsMap.putAll(ac.getAllComponentRequiredMethods());
		}
		return allComponentMethodCallsMap;
	}

	public HashSetValuedHashMap<String, MethodCallMetaData> getAllComponentMessagePassingMethods() {
		HashSetValuedHashMap<String, MethodCallMetaData> allComponentMethodCallsMap = getComponentMessagePassingMethods();
		for (ArchitecturalComponentImplementationMetaData ac : this.getComponentChildren().values()) {
			allComponentMethodCallsMap.putAll(ac.getAllComponentMessagePassingMethods());
		}
		return allComponentMethodCallsMap;
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
