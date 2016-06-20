package com.architecture.specification.library.architectural.model.extracted.metadata;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

/**
 * Metadata related to methods' declarations
 * @author FadyShining
 *
 */
public class MethodDeclarationMetaData {

	private String methodIdentifier;
	private String methodDeclaringClass;
	private String methodReturnType;
	private List<String> methodParameterTypes;
	private HashSetValuedHashMap<String, MethodCallMetaData> methodCallsMap;
	private HashMap<String, FieldAccessMetaData> fieldAccessMap;

	public MethodDeclarationMetaData(String methodIdentifier, String methodDeclaringClass, String methodReturnType, List<String> methodParameterTypes,
			HashSetValuedHashMap<String, MethodCallMetaData> methodCallsMap, HashMap<String, FieldAccessMetaData> fieldAccessMap) {
		this.methodIdentifier = methodIdentifier;
		this.methodDeclaringClass = methodDeclaringClass;
		this.methodReturnType = methodReturnType;
		this.methodParameterTypes = methodParameterTypes;
		this.methodCallsMap = methodCallsMap;
		this.fieldAccessMap = fieldAccessMap;
	}

	public String getMethodIdentifier() {
		return methodIdentifier;
	}

	public String getMethodDeclaringClass() {
		return methodDeclaringClass;
	}

	public String getMethodReturnType() {
		return methodReturnType;
	}

	public List<String> getMethodParameterTypes() {
		return methodParameterTypes;
	}

	public HashSetValuedHashMap<String, MethodCallMetaData> getMethodCallsMap() {
		return methodCallsMap;
	}

	public HashMap<String, FieldAccessMetaData> getFieldAccessMap() {
		return fieldAccessMap;
	}
	
	public boolean equivalentToMethodCallMetaData(MethodCallMetaData methodCallMetaData){
		return this.getMethodIdentifier().equals(methodCallMetaData.getMethodIdentifier()) && this.getMethodDeclaringClass().equals(methodCallMetaData.getMethodDeclaringClass())
				&& this.getMethodParameterTypes().equals(methodCallMetaData.getMethodParameterTypes()) && this.getMethodReturnType().equals(methodCallMetaData.getMethodReturnType());
	}

	public int hashCode() {
		return (this.methodIdentifier + this.methodDeclaringClass + this.methodReturnType + this.methodParameterTypes).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MethodDeclarationMetaData))
			return false;
		if (obj == this)
			return true;

		MethodDeclarationMetaData mmd = (MethodDeclarationMetaData) obj;
		return this.getMethodIdentifier().equals(mmd.getMethodIdentifier()) && this.getMethodDeclaringClass().equals(mmd.getMethodDeclaringClass())
				&& this.getMethodParameterTypes().equals(mmd.getMethodParameterTypes()) && this.getMethodReturnType().equals(mmd.getMethodReturnType());
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder(this.getMethodIdentifier());
		buff.append("\n[MethodCalls]");
		buff.append(this.getMethodCallsMap());

		return buff.toString();
	}

}
