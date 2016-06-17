package com.architecture.specification.library.architectural.model.extracted.metadata;

import java.util.List;

public class MethodCallMetaData {

	private String methodIdentifier;
	private String methodDeclaringClass;
	private String methodReturnType;
	private List<String> methodParameterTypes;
	private MethodDeclarationMetaData callerMethod;

	public MethodCallMetaData(String methodIdentifier, String methodDeclaringClass, String methodReturnType, List<String> methodParameterTypes) {
		this.methodIdentifier = methodIdentifier;
		this.methodDeclaringClass = methodDeclaringClass;
		this.methodReturnType = methodReturnType;
		this.methodParameterTypes = methodParameterTypes;
	}

	public void setCallerMethod(MethodDeclarationMetaData callerMethod) {
		this.callerMethod = callerMethod;
	}

	public String getMethodIdentifier() {
		return methodIdentifier;
	}

	public String getMethodDeclaringClass() {
		return methodDeclaringClass;
	}

	public List<String> getMethodParameterTypes() {
		return methodParameterTypes;
	}

	public String getMethodReturnType() {
		return methodReturnType;
	}

	public MethodDeclarationMetaData getCallerMethod() {
		return callerMethod;
	}

	public int hashCode() {
		return (this.methodIdentifier + this.methodDeclaringClass + this.methodReturnType + this.getMethodParameterTypes()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MethodCallMetaData))
			return false;
		if (obj == this)
			return true;

		MethodCallMetaData mmd = (MethodCallMetaData) obj;
		return this.getMethodIdentifier().equals(mmd.getMethodIdentifier()) && this.getMethodDeclaringClass().equals(mmd.getMethodDeclaringClass())
				&& this.getMethodParameterTypes().equals(mmd.getMethodParameterTypes()) && this.getMethodReturnType().equals(mmd.methodReturnType);
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder(this.getMethodIdentifier());
		buff.append("\n[MethodDeclaringClass]");
		buff.append(this.getMethodDeclaringClass());
		buff.append("\n[MethodReturnType]");
		buff.append(this.getMethodReturnType());

		return buff.toString();
	}

}
