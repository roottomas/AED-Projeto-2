package dataStructures;

import dataStructures.exceptions.*;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serial;

public class SinglyLinkedList<E> implements List<E>, Serializable {
    /**
     *  Node at the head of the list.
     */
    private SinglyListNode<E> head;
    /**
     * Node at the tail of the list.
     */
    private SinglyListNode<E> tail;
    /**
     * Number of elements in the list.
     */
    private int currentSize;
    /**
     * Constructor of an empty singly linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
    public SinglyLinkedList( ) {
        head = null;
        tail = null;
        currentSize = 0;
    }

    /**
     * Returns true iff the list contains no elements.
     * @return true if list is empty
     */
    public boolean isEmpty() {
        return currentSize==0;
    }

    /**
     * Returns the number of elements in the list.
     * @return number of elements in the list
     */

    public int size() {
        return currentSize;
    }

    /**
     * Returns an iterator of the elements in the list (in proper sequence).
     * @return Iterator of the elements in the list
     */
    public Iterator<E> iterator() {
        return new SinglyIterator<>(head);
    }

    /**
     * Returns the first element of the list.
     *
     * @return first element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    @Override
    public E getFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        return head.getElement();
    }

    /**
     * Returns the last element of the list.
     *
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    @Override
    public E getLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        return tail.getElement();
    }

    /**
     * Returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, get corresponds to getFirst.
     * If the specified position is size()-1, get corresponds to getLast.
     *
     * @param position - position of element to be returned
     * @return element at position
     * @throws InvalidPositionException if position is not valid in the list
     */
    @Override
    public E get(int position) {
        if (position < 0 || position >= currentSize)
            throw new InvalidPositionException();
        if (position == 0) return getFirst();
        if (position == currentSize - 1) return getLast();

        SinglyListNode<E> curr = head;
        for (int i = 0; i < position; i++) {
            curr = curr.getNext();
        }
        return curr.getElement();
    }

    /**
     * Returns the position of the first occurrence of the specified element
     * in the list, if the list contains the element.
     * Otherwise, returns -1.
     *
     * @param element - element to be searched in list
     * @return position of the first occurrence of the element in the list (or -1)
     */
    @Override
    public int indexOf(E element) {
        SinglyListNode<E> curr = head;
        int idx = 0;
        while (curr != null) {
            E el = curr.getElement();
            if ( (el == null && element == null) || (el != null && el.equals(element)) )
                return idx;
            curr = curr.getNext();
            idx++;
        }
        return -1;
    }

    /**
     * Inserts the specified element at the first position in the list.
     *
     * @param element to be inserted
     */
    @Override
    public void addFirst(E element) {
        SinglyListNode<E> node = new SinglyListNode<>(element, head);
        head = node;
        if (tail == null) tail = node;
        currentSize++;
    }

    /**
     * Inserts the specified element at the last position in the list.
     *
     * @param element to be inserted
     */
    @Override
    public void addLast(E element) {
        SinglyListNode<E> node = new SinglyListNode<>(element, null);
        if (isEmpty()) {
            head = tail = node;
        } else {
            tail.setNext(node);
            tail = node;
        }
        currentSize++;
    }

    /**
     * Inserts the specified element at the specified position in the list.
     * Range of valid positions: 0, ..., size().
     * If the specified position is 0, add corresponds to addFirst.
     * If the specified position is size(), add corresponds to addLast.
     *
     * @param position - position where to insert element
     * @param element  - element to be inserted
     * @throws InvalidPositionException - if position is not valid in the list
     */
    @Override
    public void add(int position, E element) {
        if ( position < 0 || position > currentSize )
            throw new InvalidPositionException();

        if (position == 0) {
            addFirst(element);
            return;
        }
        if (position == currentSize) {
            addLast(element);
            return;
        }

        SinglyListNode<E> prev = head;
        for (int i = 1; i < position; i++) {
            prev = prev.getNext();
        }
        SinglyListNode<E> node = new SinglyListNode<>(element, prev.getNext());
        prev.setNext(node);
        currentSize++;
    }

    /**
     * Removes and returns the element at the first position in the list.
     *
     * @return element removed from the first position of the list
     * @throws NoSuchElementException - if size() == 0
     */
    @Override
    public E removeFirst() {
        if (this.isEmpty())
            throw new NoSuchElementException();
        E old = head.getElement();
        head = head.getNext();
        currentSize--;
        if (head == null) tail = null;
        return old;
    }

    /**
     * Removes and returns the element at the last position in the list.
     *
     * @return element removed from the last position of the list
     * @throws NoSuchElementException - if size() == 0
     */

    public E removeLast() {
        if ( this.isEmpty() )
            throw new NoSuchElementException();
        if (currentSize == 1) {
            return removeFirst();
        }
        SinglyListNode<E> prev = head;
        while (prev.getNext() != tail) {
            prev = prev.getNext();
        }
        E old = tail.getElement();
        prev.setNext(null);
        tail = prev;
        currentSize--;
        return old;
    }

    /**
     * Removes and returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, remove corresponds to removeFirst.
     * If the specified position is size()-1, remove corresponds to removeLast.
     *
     * @param position - position of element to be removed
     * @return element removed at position
     * @throws InvalidPositionException - if position is not valid in the list
     */
    @Override
    public E remove(int position) {
        if ( position < 0 || position >= currentSize )
            throw new InvalidPositionException();

        if (position == 0) return removeFirst();
        if (position == currentSize - 1) return removeLast();

        SinglyListNode<E> prev = head;
        for (int i = 1; i < position; i++) {
            prev = prev.getNext();
        }
        SinglyListNode<E> curr = prev.getNext();
        E old = curr.getElement();
        prev.setNext(curr.getNext());
        currentSize--;
        return old;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        // save current state
        SinglyListNode<E> savedHead = head;
        SinglyListNode<E> savedTail = tail;
        int savedSize = currentSize;

        try {
            // avoid serializing the recursive node graph
            head = null;
            tail = null;
            currentSize = 0;
            out.defaultWriteObject();          // writes non-node fields (now head/tail/currentSize are "empty")

            // write logical contents explicitly
            out.writeInt(savedSize);
            SinglyListNode<E> curr = savedHead;
            while (curr != null) {
                out.writeObject(curr.getElement());
                curr = curr.getNext();
            }
        } finally {
            // restore in-memory state so object remains usable after serialization
            head = savedHead;
            tail = savedTail;
            currentSize = savedSize;
        }
    }

    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // read non-node fields (head/tail/currentSize will be whatever was written by defaultWriteObject(),
        // but we wrote head/tail as null and currentSize as 0)
        in.defaultReadObject();

        // rebuild list from the serialized elements
        int n = in.readInt();
        head = tail = null;
        currentSize = 0;
        for (int i = 0; i < n; i++) {
            E elem = (E) in.readObject();
            addLast(elem); // will update head/tail/currentSize
        }
    }

}
