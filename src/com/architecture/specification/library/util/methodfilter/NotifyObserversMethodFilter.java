package com.architecture.specification.library.util.methodfilter;

import javassist.CtMethod;

public class NotifyObserversMethodFilter implements JavassistMethodFilter {

	@Override
	public boolean accept(CtMethod ctMethod) {
		if(ctMethod.getName().equals("notifyObservers"))
			System.out.println("esgn");
		return (ctMethod.getName().equals("notifyObservers") && ctMethod.getDeclaringClass().getName().equals("java.util.Observable"));
	}

}
