package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * Iterator for a singly linked list (Singly Linked List)
 * Iterates over all elements in the list starting from the first node.
 *
 * @param <E> Type of element stored in the list
 *
 * Author: AED Team
 * Version: 1.0
 */
class SinglyIterator<E> implements Iterator<E> {

    /** First node of the list (start of iteration) */
    private final SinglyListNode<E> first;

    /** Node containing the next element to return */
    private SinglyListNode<E> nextToReturn;

    /**
     * Constructor of the iterator
     *
     * Time complexity: O(1)
     * @param first Node containing the first element of the list
     */
    public SinglyIterator(SinglyListNode<E> first) {
        this.first = first;
        this.nextToReturn = first;
    }

    /**
     * Returns true if there is a next element
     *
     * Time complexity: O(1)
     * @return true if the list has more elements to iterate
     */
    @Override
    public boolean hasNext() {
        return nextToReturn != null;
    }

    /**
     * Returns the next element in the list
     *
     * Time complexity: O(1)
     * @return the next element in the list
     * @throws NoSuchElementException if there is no next element
     */
    @Override
    public E next() {
        if (!hasNext())
            throw new NoSuchElementException();
        E element = nextToReturn.getElement();
        nextToReturn = nextToReturn.getNext();
        return element;
    }

    /**
     * Restarts the iteration to the beginning of the list
     *
     * Time complexity: O(1)
     */
    @Override
    public void rewind() {
        nextToReturn = first;
    }
}
