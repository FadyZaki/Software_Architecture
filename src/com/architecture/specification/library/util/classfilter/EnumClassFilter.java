package com.architecture.specification.library.util.classfilter;

import javassist.CtClass;

public class EnumClassFilter implements JavassistClassFilter {

	@Override
	public boolean accept(CtClass ctClass) {
		return ctClass.isEnum();
	}

}
