package com.architecture.specification.util.methodfilter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javassist.CtMethod;

public class OrMethodFilter implements JavassistMethodFilter {
	
	private List<JavassistMethodFilter> filters = new LinkedList<JavassistMethodFilter>();

    /*----------------------------------------------------------------------*\
                            Constructor
    \*----------------------------------------------------------------------*/

    /**
     * Construct a new <tt>OrClassFilter</tt> with no contained filters.
     */
    public OrMethodFilter()
    {
        // Nothing to do
    }

    /**
     * Construct a new <tt>OrClassFilter</tt> with two contained filters.
     * Additional filters may be added later, via calls to the
     * {@link #addFilter addFilter()} method.
     *
     * @param filters  filters to add
     */
    public OrMethodFilter (JavassistMethodFilter... filters)
    {
        for (JavassistMethodFilter filter : filters)
            addFilter (filter);
    }

    /*----------------------------------------------------------------------*\
                              Public Methods
    \*----------------------------------------------------------------------*/

    /**
     * Add a filter to the set of contained filters.
     *
     * @param filter the <tt>ClassFilter</tt> to add.
     *
     * @return this object, to permit chained calls.
     *
     * @see #removeFilter
     */
    public OrMethodFilter addFilter (JavassistMethodFilter filter)
    {
        filters.add (filter);
        return this;
    }

    /**
     * Remove a filter from the set of contained filters.
     *
     * @param filter the <tt>ClassFilter</tt> to remove.
     *
     * @see #addFilter
     */
    public void removeFilter (JavassistMethodFilter filter)
    {
        filters.remove (filter);
    }

    /**
     * Get the contained filters, as an unmodifiable collection.
     *
     * @return the unmodifable <tt>Collection</tt>
     */
    public Collection<JavassistMethodFilter> getFilters()
    {
        return Collections.unmodifiableCollection (filters);
    }

    /**
     * Get the total number of contained filter objects (not counting any
     * filter objects <i>they</i>, in turn, contain).
     *
     * @return the total
     */
    public int getTotalFilters()
    {
        return filters.size();
    }

    /**
     * <p>Determine whether a class name is to be accepted or not, based on
     * the contained filters. The class name name is accepted if any
     * one of the contained filters accepts it. This method stops
     * looping over the contained filters as soon as it encounters one
     * whose {@link ClassFilter#accept accept()} method returns
     * <tt>true</tt> (implementing a "short-circuited OR" operation.)</p>
     *
     * <p>If the set of contained filters is empty, then this method
     * returns <tt>true</tt>.</p>
     *
     * @param classInfo   the {@link ClassInfo} object to test
     * @param classFinder the invoking {@link ClassFinder} object
     *
     * @return <tt>true</tt> if the name matches, <tt>false</tt> if it doesn't
     */
	@Override
	public boolean accept(CtMethod ctMethod) {
		boolean accepted = false;

        if (filters.size() == 0)
            accepted = true;

        else
        {
            for (JavassistMethodFilter filter : filters)
            {
                accepted = filter.accept(ctMethod);
                if (accepted)
                    break;
            }
        }

        return accepted;
	}

}
