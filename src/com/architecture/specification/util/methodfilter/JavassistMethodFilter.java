package com.architecture.specification.util.methodfilter;


import javassist.CtMethod;

public interface JavassistMethodFilter {

	public boolean accept (CtMethod ctMethod);
	
}
