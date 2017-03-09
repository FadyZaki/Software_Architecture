package com.architecture.specification.util.classfilter;

import javassist.CtClass;
import javassist.NotFoundException;

public class InnerClassFilter implements JavassistClassFilter {

	@Override
	public boolean accept(CtClass ctClass) {
		try {
			return ctClass.getEnclosingBehavior() != null;
		} catch (NotFoundException e) {
			System.out.println("Class was not found");
			return false;
		}
	}

}
