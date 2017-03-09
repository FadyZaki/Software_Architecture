package com.architecture.specification.util.classfilter;

import javassist.CtClass;

public class EnumClassFilter implements JavassistClassFilter {

	@Override
	public boolean accept(CtClass ctClass) {
		return ctClass.isEnum();
	}

}
