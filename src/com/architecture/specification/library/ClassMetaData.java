package com.architecture.specification.library;

import java.util.ArrayList;
import java.util.List;

public class ClassMetaData {

	private List<String> providedMethods;
	private List<String> requiredMethods;
	private boolean isInnerClass;
	
	public ClassMetaData() {
		super();
		providedMethods = new ArrayList<String>();
		requiredMethods = new ArrayList<String>();
		isInnerClass = false;
	}

	public List<String> getProvidedMethods() {
		return providedMethods;
	}

	public List<String> getRequiredMethods() {
		return requiredMethods;
	}

	public boolean isInnerClass() {
		return isInnerClass;
	}

	public void setInnerClass(boolean isInnerClass) {
		this.isInnerClass = isInnerClass;
	}
	
	
	
	
}
