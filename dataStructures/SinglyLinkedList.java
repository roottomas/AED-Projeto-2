package dataStructures;

import dataStructures.exceptions.*;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serial;

/**
 * Singly linked list implementation.
 * @param <E> Type of elements stored in the list
 */
public class SinglyLinkedList<E> implements List<E>, Serializable {

    /** Node at the head of the list */
    private transient SinglyListNode<E> head;

    /** Node at the tail of the list */
    private transient SinglyListNode<E> tail;

    /** Number of elements in the list */
    private transient int currentSize;

    /** Constructor for empty list */
    public SinglyLinkedList() {
        head = null;
        tail = null;
        currentSize = 0;
    }

    /**
     * Returns true iff the list contains no elements
     *
     * Time complexity: O(1)
     * @return true if the list is empty
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Returns number of elements in the list
     *
     * Time complexity: O(1)
     * @return number of elements in the list
     */
    public int size() {
        return currentSize;
    }

    /**
     * Returns iterator over elements
     *
     * Time complexity: O(1)
     * @return iterator over the elements
     */
    public Iterator<E> iterator() {
        return new SinglyIterator<>(head);
    }

    /**
     * Returns first element
     *
     * Time complexity: O(1)
     * @return first element in the list
     * @throws NoSuchElementException if list is empty
     */
    @Override
    public E getFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        return head.getElement();
    }

    /**
     * Returns last element
     *
     * Time complexity: O(1)
     * @return last element in the list
     * @throws NoSuchElementException if list is empty
     */
    @Override
    public E getLast() {
        if (isEmpty()) throw new NoSuchElementException();
        return tail.getElement();
    }

    /**
     * Returns element at given position
     *
     * Time complexity: O(n)
     * @param position position of the element
     * @return element at the given position
     * @throws InvalidPositionException if position is invalid
     */
    @Override
    public E get(int position) {
        if (position < 0 || position >= currentSize) throw new InvalidPositionException();
        if (position == 0) return getFirst();
        if (position == currentSize - 1) return getLast();

        SinglyListNode<E> curr = head;
        for (int i = 0; i < position; i++) curr = curr.getNext();
        return curr.getElement();
    }

    /**
     * Returns index of first occurrence of element
     *
     * Time complexity: O(n)
     * @param element element to search for
     * @return index of element or -1 if not found
     */
    @Override
    public int indexOf(E element) {
        SinglyListNode<E> curr = head;
        int idx = 0;
        while (curr != null) {
            E el = curr.getElement();
            if ((el == null && element == null) || (el != null && el.equals(element))) return idx;
            curr = curr.getNext();
            idx++;
        }
        return -1;
    }

    /**
     * Adds element at beginning
     *
     * Time complexity: O(1)
     * @param element element to add
     */
    @Override
    public void addFirst(E element) {
        SinglyListNode<E> node = new SinglyListNode<>(element, head);
        head = node;
        if (tail == null) tail = node;
        currentSize++;
    }

    /**
     * Adds element at end
     *
     * Time complexity: O(1)
     * @param element element to add
     */
    @Override
    public void addLast(E element) {
        SinglyListNode<E> node = new SinglyListNode<>(element, null);
        if (isEmpty()) head = tail = node;
        else {
            tail.setNext(node);
            tail = node;
        }
        currentSize++;
    }

    /**
     * Adds element at given position
     *
     * Time complexity: O(n)
     * @param position position to insert element
     * @param element element to insert
     * @throws InvalidPositionException if position is invalid
     */
    @Override
    public void add(int position, E element) {
        if (position < 0 || position > currentSize) throw new InvalidPositionException();
        if (position == 0) { addFirst(element); return; }
        if (position == currentSize) { addLast(element); return; }

        SinglyListNode<E> prev = head;
        for (int i = 1; i < position; i++) prev = prev.getNext();
        SinglyListNode<E> node = new SinglyListNode<>(element, prev.getNext());
        prev.setNext(node);
        currentSize++;
    }

    /**
     * Removes first element
     *
     * Time complexity: O(1)
     * @return element removed from the beginning
     * @throws NoSuchElementException if list is empty
     */
    @Override
    public E removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        E old = head.getElement();
        head = head.getNext();
        currentSize--;
        if (head == null) tail = null;
        return old;
    }

    /**
     * Removes last element
     *
     * Time complexity: O(n)
     * @return element removed from the end
     * @throws NoSuchElementException if list is empty
     */
    public E removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        if (currentSize == 1) return removeFirst();

        SinglyListNode<E> prev = head;
        while (prev.getNext() != tail) prev = prev.getNext();
        E old = tail.getElement();
        prev.setNext(null);
        tail = prev;
        currentSize--;
        return old;
    }

    /**
     * Removes element at given position
     *
     * Time complexity: O(n)
     * @param position position to remove
     * @return element removed
     * @throws InvalidPositionException if position is invalid
     */
    @Override
    public E remove(int position) {
        if (position < 0 || position >= currentSize) throw new InvalidPositionException();
        if (position == 0) return removeFirst();
        if (position == currentSize - 1) return removeLast();

        SinglyListNode<E> prev = head;
        for (int i = 1; i < position; i++) prev = prev.getNext();
        SinglyListNode<E> curr = prev.getNext();
        E old = curr.getElement();
        prev.setNext(curr.getNext());
        currentSize--;
        return old;
    }

    /** Serialization: writes elements explicitly */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        SinglyListNode<E> savedHead = head;
        SinglyListNode<E> savedTail = tail;
        int savedSize = currentSize;
        try {
            head = tail = null;
            currentSize = 0;
            out.defaultWriteObject();
            out.writeInt(savedSize);
            SinglyListNode<E> curr = savedHead;
            while (curr != null) {
                out.writeObject(curr.getElement());
                curr = curr.getNext();
            }
        } finally {
            head = savedHead;
            tail = savedTail;
            currentSize = savedSize;
        }
    }

    /** Serialization: reads elements explicitly */
    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int n = in.readInt();
        head = tail = null;
        currentSize = 0;
        for (int i = 0; i < n; i++) addLast((E) in.readObject());
    }

}
