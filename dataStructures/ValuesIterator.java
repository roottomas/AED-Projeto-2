package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * Iterator over values of a map or dictionary.
 * @author AED Team
 * @version 1.0
 * @param <E> Generic element
 */
class ValuesIterator<E> implements Iterator<E> {

    /**
     * Underlying iterator of map entries.
     */
    private final Iterator<Map.Entry<?, E>> it;

    /**
     * Constructor
     * @param it iterator over Map.Entry objects
     *
     * Time complexity: O(1)
     */
    public ValuesIterator(Iterator<Map.Entry<?, E>> it) {
        this.it = it;
    }

    /**
     * Returns true if next() would return an element rather than throwing an exception.
     * Time complexity: O(1)
     *
     * @return true iff the iteration has more elements
     *
     */
    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    /**
     * Returns the next value in the iteration.
     * Time complexity: O(1)
     *
     * @return next value in the iteration
     * @throws NoSuchElementException if there are no more elements
     *
     */
    @Override
    public E next() {
        if (!hasNext())
            throw new NoSuchElementException();
        return it.next().value(); // returns only the value, ignores the key
    }

    /**
     * Restarts the iteration.
     * After rewind(), next() will return the first element if iteration is not empty.
     *
     * Time complexity: O(1)
     */
    @Override
    public void rewind() {
        it.rewind();
    }
}
