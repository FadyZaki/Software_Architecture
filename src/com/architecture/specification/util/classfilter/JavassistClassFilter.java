
package com.architecture.specification.util.classfilter;

import javassist.CtClass;

public interface JavassistClassFilter
{
	public boolean accept (CtClass ctClass);
}
