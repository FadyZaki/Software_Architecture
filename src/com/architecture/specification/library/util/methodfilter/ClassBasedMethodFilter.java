package com.architecture.specification.library.util.methodfilter;

import java.util.Arrays;

import javassist.CtClass;
import javassist.CtMethod;

public class ClassBasedMethodFilter implements JavassistMethodFilter {

	private CtClass filteredClass;
	
	public ClassBasedMethodFilter(CtClass filteredClass) {
		this.filteredClass = filteredClass;
	}

	@Override
	public boolean accept(CtMethod ctMethod) {
		return Arrays.asList(filteredClass.getMethods()).contains(ctMethod);
	}

}
