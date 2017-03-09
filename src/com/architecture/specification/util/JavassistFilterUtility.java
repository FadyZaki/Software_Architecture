package com.architecture.specification.util;

import java.util.ArrayList;
import java.util.List;

import com.architecture.specification.util.classfilter.JavassistClassFilter;
import com.architecture.specification.util.methodfilter.JavassistMethodFilter;

import javassist.CtClass;
import javassist.CtMethod;

public class JavassistFilterUtility {

	public static List<CtClass> filterClasses(JavassistClassFilter classFilter, List<CtClass> classesToBeFiltered) {

		if (classFilter == null)
			return classesToBeFiltered;
		else {
			List<CtClass> filteredClasses = new ArrayList<CtClass>();
			for (CtClass ctClass : classesToBeFiltered)
				if (classFilter.accept(ctClass))
					filteredClasses.add(ctClass);
			return filteredClasses;
		}

	}

	public static List<CtMethod> filterMethods(JavassistMethodFilter methodFilter, List<CtMethod> methodsToBeFiltered) {
		if (methodFilter == null)
			return methodsToBeFiltered;
		else {
			List<CtMethod> filteredMethods = new ArrayList<CtMethod>();
			for (CtMethod ctMethod : methodsToBeFiltered)
				if (methodFilter.accept(ctMethod))
					filteredMethods.add(ctMethod);
			return filteredMethods;
		}
	}

}
