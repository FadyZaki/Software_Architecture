
package com.architecture.specification.library.util.classfilter;

import javassist.CtClass;

public interface JavassistClassFilter
{
	public boolean accept (CtClass ctClass);
}
