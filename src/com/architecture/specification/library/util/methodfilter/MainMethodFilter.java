package com.architecture.specification.library.util.methodfilter;

import java.lang.reflect.Modifier;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class MainMethodFilter implements JavassistMethodFilter {

	@Override
	public boolean accept(CtMethod ctMethod) {
		try {

			return (ctMethod.getName().equals("main") && Modifier.isStatic(ctMethod.getModifiers()) && Modifier.isPublic(ctMethod.getModifiers())
					&& ctMethod.getReturnType().equals(CtClass.voidType) && ctMethod.getParameterTypes().length == 1
					&& ctMethod.getParameterTypes()[0].getName().equals("java.lang.String[]"));
		} catch (NotFoundException e) {
			System.out.println("Compilation error");
		}
		return false;
	}

}
