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
     * Construct a FilterIterator over a given list using a given filter.
     *
     * Time complexity: O(1)
     *
     * @param list to be iterated
     * @param filter filter to apply to each element
     */
    public FilterIterator(Iterator<E> list, Predicate<E> filter) {
        this.iterator = list;
        this.filter = filter;
        rewind();
    }

    /**
     * Returns true if next would return an element.
     *
     * Time complexity: O(1) amortized per returned element.
     * Worst-case: O(n) if many non-matching elements must be scanned.
     *
     * @return true iff the iteration has more elements
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
     * Time complexity: O(1)
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public E next() {
        if (nextToReturn == null)
            throw new NoSuchElementException();

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
