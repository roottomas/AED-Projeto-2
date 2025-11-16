package dataStructures;

import dataStructures.exceptions.*;

/**
 * Queue implemented over a singly linked list.
 * Enqueue at the end and dequeue from the front.
 *
 * @param <E> Type of elements stored in the queue
 */
public class QueueInList<E> implements Queue<E> {

    /** Memory structure of the queue */
    private List<E> list;

    /** Constructor: initializes an empty queue */
    public QueueInList() {
        list = new SinglyLinkedList<E>();
    }

    /**
     * Returns true if the queue is empty.
     *
     * Time complexity: O(1)
     * @return true if there are no elements
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements in the queue.
     *
     * Time complexity: O(1)
     * @return size of the queue
     */
    @Override
    public int size() {
        return list.size();
    }

    /**
     * Inserts an element at the end of the queue.
     *
     * Time complexity:
     * - O(1) if the list supports fast addition at the end (like ListInArray with counter or SinglyLinkedList with tail)
     * - Otherwise O(n)
     * @param element element to enqueue
     */
    @Override
    public void enqueue(E element) {
        list.addLast(element);
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * Time complexity: O(1)
     * @return element removed from the front of the queue
     * @throws EmptyQueueException if the queue is empty
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
     * Time complexity: O(1)
     * @return element at the front of the queue
     * @throws EmptyQueueException if the queue is empty
     */
    @Override
    public E peek() {
        if (list.isEmpty()) {
            throw new EmptyQueueException();
        }
        return list.getFirst();
    }
}
