
package com.architecture.specification.library.util.classfilter;

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
