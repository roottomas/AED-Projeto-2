/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */

package dataStructures;
import dataStructures.exceptions.NoSuchElementException;

/**
 * Array Iterator
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
class ArrayIterator<E> implements Iterator<E> {
    private E[] elems;
    private int counter;
    private int current;

    /**
     * Create a new iterator over the provided array view.
     *
     * Time complexity: O(1)
     *
     * @param elems array containing elements
     * @param counter number of valid elements in the array (upper bound)
     */
    public ArrayIterator(E[] elems, int counter) {
        this.elems = elems;
        this.counter = counter;
        rewind();
    }

    /**
     * Reset iterator to the first element.
     *
     * Time complexity: O(1)
     */
    @Override
    public void rewind() {
        current = 0;
    }

    /**
     * Returns true if there are remaining elements to iterate.
     *
     * Time complexity: O(1)
     *
     * @return true if next() would return an element
     */
    @Override
    public boolean hasNext() {
        return current < counter;
    }

    /**
     * Return the next element and advance the iterator.
     *
     * Time complexity: O(1)
     *
     * @return next element
     * @throws NoSuchElementException if no more elements are available
     */
    @Override
    public E next() {
        if (!hasNext())
            throw new NoSuchElementException();
        return elems[current++];
    }

}
