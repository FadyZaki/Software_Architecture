package com.architecture.specification.util.classfilter;

import javassist.CtClass;

public class InterfaceClassFilter implements JavassistClassFilter {

	@Override
	public boolean accept(CtClass ctClass) {
		return ctClass.isInterface();
	}

}
