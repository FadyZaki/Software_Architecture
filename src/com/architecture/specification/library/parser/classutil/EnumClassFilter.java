package com.architecture.specification.library.parser.classutil;

import org.objectweb.asm.Opcodes;

public class EnumClassFilter extends ClassModifiersClassFilter {

	/**
	 * Construct a new <tt>InterfaceOnlyClassFilter</tt> that will accept only
	 * classes that are interfaces.
	 */
	public EnumClassFilter() {
		super(Opcodes.ACC_ENUM);
	}
}
