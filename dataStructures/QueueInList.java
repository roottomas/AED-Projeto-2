package dataStructures;

import dataStructures.exceptions.*;

public class QueueInList<E> implements Queue<E> {

    // Memory of the queue: a list.
    private List<E> list;

    public QueueInList() {
        list = new SinglyLinkedList<E>();
    }

    /**
     * Returns true iff the queue contains no elements.
     *
     * @return true iff the queue contains no elements.
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements in the queue.
     *
     * @return the number of elements in the queue.
     */
    @Override
    public int size() {
        return list.size();
    }

    /**
     * Inserts the specified element at the rear of the queue.
     *
     * @param element
     */
    @Override
    public void enqueue(E element) {
        list.addLast(element);
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return the element at the front of the queue.
     * @throws EmptyQueueException
     */
    @Override
    public E dequeue() {
        if (list.isEmpty()) {
            throw new EmptyQueueException();
        }
        return list.removeFirst();
    }

    /**
     * Returns the element at the front of the queue without removing it.
     *
     * @return the element at the front of the queue without removing it.
     * @throws EmptyQueueException
     */
    @Override
    public E peek() {
        if (list.isEmpty()) {
            throw new EmptyQueueException();
        }
        return list.getFirst();
    }
}
