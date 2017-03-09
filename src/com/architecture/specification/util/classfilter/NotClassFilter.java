
package com.architecture.specification.util.classfilter;

import javassist.CtClass;

public class NotClassFilter implements JavassistClassFilter {

	private JavassistClassFilter filter;

	public NotClassFilter(JavassistClassFilter filter) {
		this.filter = filter;
	}

	public boolean accept(CtClass ctClass) {
		return !this.filter.accept(ctClass);
	}
}
