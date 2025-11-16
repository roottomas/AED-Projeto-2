package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * Iterator of keys.
 * Wraps an iterator of Map.Entry and exposes only the keys.
 *
 * @param <E> Generic element type
 * @author AED Team
 * @version 1.0
 */
class KeysIterator<E> implements Iterator<E> {

    /** Underlying iterator of Map.Entry objects */
    private final Iterator<Map.Entry<E, ?>> it;

    /**
     * Constructor wrapping an existing iterator of Map.Entry.
     *
     * @param it iterator of Map.Entry<E, ?>
     * Time complexity: O(1)
     */
    public KeysIterator(Iterator<Map.Entry<E,?>> it) {
        this.it = it;
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * Time complexity: O(1)
     * @return true if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    /**
     * Returns the next key in the iteration.
     *
     * Time complexity: O(1)
     * @return the next key
     * @throws NoSuchElementException if there is no next element
     */
    @Override
    public E next() {
        if (!hasNext()) throw new NoSuchElementException();
        return it.next().key();
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first key.
     *
     * Time complexity: O(1) assuming the underlying iterator supports rewind efficiently
     */
    @Override
    public void rewind() {
        it.rewind();
    }
}
