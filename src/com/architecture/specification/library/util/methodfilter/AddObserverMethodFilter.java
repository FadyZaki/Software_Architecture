package com.architecture.specification.library.util.methodfilter;

import javassist.CtMethod;

public class AddObserverMethodFilter implements JavassistMethodFilter {

	@Override
	public boolean accept(CtMethod ctMethod) {
		return (ctMethod.getName().equals("addObserver") && ctMethod.getDeclaringClass().getName().equals("java.util.Observable"));
	}

}
