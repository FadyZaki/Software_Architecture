
package com.architecture.specification.util.methodfilter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javassist.CtMethod;

public final class AndMethodFilter implements JavassistMethodFilter {

	private List<JavassistMethodFilter> filters = new LinkedList<JavassistMethodFilter>();

	public AndMethodFilter() {
	}

	/**
	 * Construct a new <tt>AndJavassistClassFilter</tt> with a set of contained filters.
	 * Additional filters may be added later, via calls to the {@link #addFilter
	 * addFilter()} method.
	 *
	 * @param filters
	 *            filters to add
	 */
	public AndMethodFilter(JavassistMethodFilter... filters) {
		for (JavassistMethodFilter filter : filters)
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
	public AndMethodFilter addFilter(JavassistMethodFilter filter) {
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
	public void removeFilter(JavassistMethodFilter filter) {
		filters.remove(filter);
	}

	/**
	 * Get the contained filters, as an unmodifiable collection.
	 *
	 * @return the unmodifable <tt>Collection</tt>
	 */
	public Collection<JavassistMethodFilter> getFilters() {
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
	public boolean accept(CtMethod ctMethod) {
		boolean accepted = true;

		for (JavassistMethodFilter methodFilter : filters) {
			accepted = methodFilter.accept(ctMethod);
			if (!accepted)
				break;
		}

		return accepted;
	}
}
