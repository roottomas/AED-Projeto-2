package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * Array Iterator
 * @author AED Team
 * @version 1.0
 * @param <E> Generic Element
 */
class ArrayIterator<E> implements Iterator<E> {
    private E[] elems;
    private int counter;
    private int current;

    public ArrayIterator(E[] elems, int counter) {
        this.elems = elems;
        this.counter = counter;
        rewind();
    }

    /**
     * Reset the iterator to the beginning.
     * Time complexity: O(1)
     */
    @Override
    public void rewind() {
        current = 0;
    }

    /**
     * Tests if the iteration has more elements.
     * Time complexity: O(1)
     * @return true if there are more elements, false otherwise
     */
    @Override
    public boolean hasNext() {
        return current < counter;
    }

    /**
     * Returns the next element in the iteration.
     * Time complexity: O(1)
     * @return the next element of type E
     */
    @Override
    public E next() {
        if (!hasNext())
            throw new NoSuchElementException();
        return elems[current++];
    }
}
