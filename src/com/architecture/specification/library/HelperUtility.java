package com.architecture.specification.library;

import java.util.Collections;
import java.util.List;

public class HelperUtility {

	public static List safeList( List other ) {
	    return other == null ? Collections.emptyList() : other;
	}
}
