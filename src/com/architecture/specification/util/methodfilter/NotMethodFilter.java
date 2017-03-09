
package com.architecture.specification.util.methodfilter;

import javassist.CtMethod;

public class NotMethodFilter implements JavassistMethodFilter {

	private JavassistMethodFilter filter;

	public NotMethodFilter(JavassistMethodFilter filter) {
		this.filter = filter;
	}

	public boolean accept(CtMethod ctMethod) {
		return !this.filter.accept(ctMethod);
	}
}
