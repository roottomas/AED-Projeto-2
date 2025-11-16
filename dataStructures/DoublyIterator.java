package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * Two-way iterator for Doubly Linked List (DLList)
 *
 * @author AED Team
 * @version 1.0
 * @param <E> Generic Element
 */
class DoublyIterator<E> implements Iterator<E> {

    /** Node with the first element in the iteration */
    private final DoublyListNode<E> firstNode;

    /** Node with the next element to be returned */
    private DoublyListNode<E> nextToReturn;

    /**
     * Constructor: initializes the iterator to start at the given node.
     *
     * Time complexity: O(1)
     * @param first Node with the first element of the iteration
     */
    public DoublyIterator(DoublyListNode<E> first) {
        this.firstNode = first;
        this.nextToReturn = first;
    }

    /**
     * Returns the next element in the iteration.
     *
     * Time complexity: O(1)
     * @return the next element in the iteration
     * @throws NoSuchElementException if there is no next element
     */
    @Override
    public E next() {
        if (!hasNext())
            throw new NoSuchElementException();
        E elem = nextToReturn.getElement();
        nextToReturn = nextToReturn.getNext();
        return elem;
    }

    /**
     * Restarts the iteration to the first element.
     *
     * Time complexity: O(1)
     */
    @Override
    public void rewind() {
        nextToReturn = firstNode;
    }

    /**
     * Returns true if the iteration has more elements.
     *
     * Time complexity: O(1)
     * @return true iff the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return nextToReturn != null;
    }
}
