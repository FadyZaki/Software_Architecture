
package com.architecture.specification.library.util.classfilter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javassist.CtClass;

public final class AndClassFilter implements JavassistClassFilter {

	private List<JavassistClassFilter> filters = new LinkedList<JavassistClassFilter>();

	public AndClassFilter() {
	}

	/**
	 * Construct a new <tt>AndJavassistClassFilter</tt> with a set of contained filters.
	 * Additional filters may be added later, via calls to the {@link #addFilter
	 * addFilter()} method.
	 *
	 * @param filters
	 *            filters to add
	 */
	public AndClassFilter(JavassistClassFilter... filters) {
		for (JavassistClassFilter filter : filters)
			addFilter(filter);
	}

	/*----------------------------------------------------------------------*\
	                          Public Methods
	\*----------------------------------------------------------------------*/

	/**
	 * Add a filter to the set of contained filters.
	 *
	 * @param filter
	 *            the <tt>JavassistClassFilter</tt> to add.
	 *
	 * @return this object, to permit chained calls.
	 *
	 * @see #removeFilter
	 */
	public AndClassFilter addFilter(JavassistClassFilter filter) {
		filters.add(filter);
		return this;
	}

	/**
	 * Remove a filter from the set of contained filters.
	 *
	 * @param filter
	 *            the <tt>JavassistClassFilter</tt> to remove.
	 *
	 * @see #addFilter
	 */
	public void removeFilter(JavassistClassFilter filter) {
		filters.remove(filter);
	}

	/**
	 * Get the contained filters, as an unmodifiable collection.
	 *
	 * @return the unmodifable <tt>Collection</tt>
	 */
	public Collection<JavassistClassFilter> getFilters() {
		return Collections.unmodifiableCollection(filters);
	}

	/**
	 * Get the total number of contained filter objects (not counting any filter
	 * objects <i>they</i>, in turn, contain).
	 *
	 * @return the total
	 */
	public int getTotalFilters() {
		return filters.size();
	}

	@Override
	public boolean accept(CtClass ctClass) {
		boolean accepted = true;

		for (JavassistClassFilter classFilter : filters) {
			accepted = classFilter.accept(ctClass);
			if (!accepted)
				break;
		}

		return accepted;
	}
}
