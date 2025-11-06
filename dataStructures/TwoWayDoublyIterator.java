package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * Implementation of Two Way Iterator for DLList 
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
class TwoWayDoublyIterator<E> extends DoublyIterator<E>
        implements TwoWayIterator<E> {

    /**
     * Node with the last element in the iteration.
     */
    private DoublyListNode<E> lastNode;
    /**
     * Node with the previous element in the iteration.
     */
    DoublyListNode<E> prevToReturn;

    /**
     * DoublyLLIterator constructor
     *
     * @param first - Node with the first element of the iteration
     * @param last  - Node with the last element of the iteration
     */
    public TwoWayDoublyIterator(DoublyListNode<E> first, DoublyListNode<E> last) {
        super(first);
        this.lastNode = last;
        this.prevToReturn = null;
    }

    /**
     * Returns true if previous would return an element
     * rather than throwing an exception.
     * @return true iff the iteration has more elements in the reverse direction
     *
     * Time complexity: O(1)
     */
    public boolean hasPrevious( ) {
        return prevToReturn != null;
    }

    /**
     * Returns the next element in the iteration.
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     *
     * Time complexity: O(1)
     */
    public E next( ){
        if (!hasNext())
            throw new NoSuchElementException();

        E elem = nextToReturn.getElement();
        prevToReturn = nextToReturn;
        nextToReturn = nextToReturn.getNext();
        return elem;
    }

    /**
     * Returns the previous element in the iteration.
     * @return previous element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     *
     * Time complexity: O(1)
     */
    public E previous( ) {
        if (!hasPrevious())
            throw new NoSuchElementException();

        E elem = prevToReturn.getElement();
        nextToReturn = prevToReturn;
        prevToReturn = prevToReturn.getPrevious();
        return elem;
    }

    /**
     * Restarts the iteration in the reverse direction.
     * After fullForward, if iteration is not empty,
     * previous will return the last element
     *
     * Time complexity: O(1)
     */
    public void fullForward() {
        nextToReturn = null;
        prevToReturn = lastNode;
    }

    /**
     * Restart the iterator
     *
     * Time complexity: O(1)
     */
    public void rewind() {
        super.rewind();
        prevToReturn = null;
    }
}
