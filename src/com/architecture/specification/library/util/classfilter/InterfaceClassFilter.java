package com.architecture.specification.library.util.classfilter;

import javassist.CtClass;

public class InterfaceClassFilter implements JavassistClassFilter {

	@Override
	public boolean accept(CtClass ctClass) {
		return ctClass.isInterface();
	}

}
