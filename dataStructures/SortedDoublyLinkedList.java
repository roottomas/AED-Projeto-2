package dataStructures;

import dataStructures.exceptions.*;

/**
 * Sorted Doubly linked list Implementation
 * @author
 * @version 2.0
 * @param <E> Generic Element
 */
public class SortedDoublyLinkedList<E> implements SortedList<E> {

    /**
     * Node at the head of the list.
     */
    private DoublyListNode<E> head;

    /**
     * Node at the tail of the list.
     */
    private DoublyListNode<E> tail;

    /**
     * Number of elements in the list.
     */
    private int currentSize;

    /**
     * Comparator of elements.
     */
    private final Comparator<E> comparator;

    /**
     * Constructor of an empty sorted double linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
    public SortedDoublyLinkedList(Comparator<E> comparator) {
        this.head = null;
        this.tail = null;
        this.currentSize = 0;
        this.comparator = comparator;
    }

    /**
     * Returns true iff the list contains no elements.
     * @return true if list is empty
     *
     * Time complexity: O(1)
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Returns the number of elements in the list.
     * @return number of elements in the list
     *
     * Time complexity: O(1)
     */
    public int size() {
        return currentSize;
    }

    /**
     * Returns an iterator of the elements in the list (in proper sequence).
     * @return Iterator of the elements in the list
     *
     * Time complexity: O(1)
     */
    public Iterator<E> iterator() {
        return new DoublyIterator<>(head);
    }

    /**
     * Returns the first element of the list.
     * @return first element in the list
     * @throws NoSuchElementException - if size() == 0
     *
     * Time complexity: O(1)
     */
    public E getMin() {
        if (isEmpty())
            throw new NoSuchElementException();
        return head.getElement();
    }

    /**
     * Returns the last element of the list.
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     *
     * Time complexity: O(1)
     */
    public E getMax() {
        if (isEmpty())
            throw new NoSuchElementException();
        return tail.getElement();
    }

    /**
     * Returns the first occurrence of the element equals to the given element in the list.
     * @return element in the list or null
     *
     * Time complexity: O(n) — n being the number of elements
     */
    public E get(E element) {
        DoublyListNode<E> current = head;
        while (current != null) {
            if (comparator.compare(current.getElement(), element) == 0) {
                return current.getElement();
            }
            current = current.getNext();
        }
        return null;
    }

    /**
     * Returns true iff the element exists in the list.
     * @param element to be found
     * @return true iff the element exists in the list.
     *
     * Time complexity: O(n) — n being the number of elements
     */
    public boolean contains(E element) {
        return get(element) != null;
    }

    /**
     * Inserts the specified element at the list, according to the comparator.
     * If there is an equal element, the new element is inserted after it.
     * @param element to be inserted
     *
     * Time complexity: O(n) — n being the number of elements
     */
    public void add(E element) {
        DoublyListNode<E> newNode = new DoublyListNode<>(element);

        if (isEmpty()) {
            head = tail = newNode;
        } else if (comparator.compare(element, head.getElement()) < 0) {
            // Insert before head
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        } else if (comparator.compare(element, tail.getElement()) >= 0) {
            // Insert after tail
            newNode.setPrevious(tail);
            tail.setNext(newNode);
            tail = newNode;
        } else {
            // Insert in middle
            DoublyListNode<E> current = head;
            while (current != null && comparator.compare(element, current.getElement()) >= 0) {
                current = current.getNext();
            }

            DoublyListNode<E> prev = current.getPrevious();
            newNode.setPrevious(prev);
            newNode.setNext(current);
            prev.setNext(newNode);
            current.setPrevious(newNode);
        }
        currentSize++;
    }

    /**
     * Removes and returns the first occurrence of the element equals to the given element in the list.
     * @return element removed from the list or null if !belongs(element)
     *
     * Time complexity: O(n) — n being the number of elements
     */
    public E remove(E element) {
        DoublyListNode<E> current = head;
        while (current != null && comparator.compare(current.getElement(), element) != 0) {
            current = current.getNext();
        }

        if (current == null) {
            return null;
        }

        if (current == head) {
            head = head.getNext();
            if (head != null) {
                head.setPrevious(null);
            } else {
                tail = null;
            }
        } else if (current == tail) {
            tail = tail.getPrevious();
            if (tail != null) {
                tail.setNext(null);
            } else {
                head = null;
            }
        } else {
            DoublyListNode<E> prev = current.getPrevious();
            DoublyListNode<E> next = current.getNext();
            prev.setNext(next);
            next.setPrevious(prev);
        }

        currentSize--;
        return current.getElement();
    }
}
