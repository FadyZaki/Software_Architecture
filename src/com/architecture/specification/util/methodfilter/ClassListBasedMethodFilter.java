package com.architecture.specification.util.methodfilter;

import java.util.List;

import javassist.CtClass;
import javassist.CtMethod;

public class ClassListBasedMethodFilter implements JavassistMethodFilter {

	private List<CtClass> classList;

	public ClassListBasedMethodFilter(List<CtClass> classList) {
		this.classList = classList;
	}

	@Override
	public boolean accept(CtMethod ctMethod) {
		return classList.contains(ctMethod.getDeclaringClass());
	}

}
