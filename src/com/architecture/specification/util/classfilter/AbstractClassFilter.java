package com.architecture.specification.util.classfilter;

import java.lang.reflect.Modifier;

import javassist.CtClass;

public class AbstractClassFilter implements JavassistClassFilter {

	@Override
	public boolean accept(CtClass ctClass) {
		return (ctClass.getModifiers() & Modifier.ABSTRACT) != 0;
	}

}
