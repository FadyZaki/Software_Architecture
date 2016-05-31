package com.architecture.specification.library.util.methodfilter;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import javassist.CtClass;
import javassist.CtMethod;

public class PublicMethodFilter implements JavassistMethodFilter {

	@Override
	public boolean accept(CtMethod ctMethod) {
		return (ctMethod.getModifiers() & Modifier.PUBLIC) != 0;
	}

}
