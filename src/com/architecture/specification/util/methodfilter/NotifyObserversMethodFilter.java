package com.architecture.specification.util.methodfilter;

import javassist.CtMethod;

public class NotifyObserversMethodFilter implements JavassistMethodFilter {

	@Override
	public boolean accept(CtMethod ctMethod) {
		return (ctMethod.getName().equals("notifyObservers") && ctMethod.getDeclaringClass().getName().equals("java.util.Observable"));
	}

}
