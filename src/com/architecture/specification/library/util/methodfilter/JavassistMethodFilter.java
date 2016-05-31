package com.architecture.specification.library.util.methodfilter;


import javassist.CtMethod;

public interface JavassistMethodFilter {

	public boolean accept (CtMethod tMethod);
	
}
