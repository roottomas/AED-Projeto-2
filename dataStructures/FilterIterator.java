/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */

package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * Iterator Abstract Data Type with Filter
 * Includes description of general methods for one way iterator.
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public class FilterIterator<E> implements Iterator<E> {

    /**
     *  Iterator of elements to filter.
     */
    Iterator<E> iterator;

    /**
     *  Filter.
     */
    Predicate<E> filter;

    /**
     * Node with the next element in the iteration.
     */
    E nextToReturn;

    /**
     *
     * @param list to be iterated
     * @param filter
     */
    public FilterIterator(Iterator<E> list, Predicate<E> filter) {
        iterator = list;
        this.filter = filter;
        rewind();
    }

    /**
     * Returns true if next would return an element
     *
     * @return true iff the iteration has more elements
     *
     * Time complexity: O(1) per returned element.
     * Worst-case (when scanning many non-matching elements): O(n) where n is number
     * of underlying elements scanned until a match is found.
     */
    public boolean hasNext() {
        while (nextToReturn == null && iterator.hasNext()) {
            E elem = iterator.next();
            if (filter.check(elem)) {
                nextToReturn = elem;
                return true;
            }
        }
        return nextToReturn != null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     *
     * Time complexity: O(1)
     */
    public E next() {
        if (nextToReturn == null) throw new NoSuchElementException();
        E current = nextToReturn;
        nextToReturn = null;
        return current;
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     *
     * Time complexity: O(1)
     */
    public void rewind() {
        iterator.rewind();
        nextToReturn = null;
    }
}