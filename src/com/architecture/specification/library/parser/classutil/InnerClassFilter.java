package com.architecture.specification.library.parser.classutil;

public class InnerClassFilter implements ClassFilter {

	public InnerClassFilter() {
	}

	/**
	 * Perform the acceptance test on the loaded <tt>Class</tt> object.
	 *
	 * @param classInfo
	 *            the {@link ClassInfo} object to test
	 * @param classFinder
	 *            the invoking {@link ClassFinder} object
	 *
	 * @return <tt>true</tt> if the class name matches, <tt>false</tt> if it
	 *         doesn't
	 */
	public boolean accept(ClassInfo classInfo, ClassFinder classFinder) {
		return (classInfo.getOuterClassName() != null);
	}
}
