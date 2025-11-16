/**
 * @author Tomás Silvestre 68594 tm.silvestre@campus.fct.unl.pt
 * @author Ricardo Laur 68342 r.laur@campus.fct.unl.pt
 */

package dataStructures;

import dataStructures.exceptions.InvalidPositionException;
import dataStructures.exceptions.NoSuchElementException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

/**
 * Implementation of Doubly Linked List
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public class DoublyLinkedList<E> implements TwoWayList<E> {
    /**
     *  Node at the head of the list.
     */
    private transient DoublyListNode<E> head;
    /**
     * Node at the tail of the list.
     */
    private transient DoublyListNode<E> tail;
    /**
     * Number of elements in the list.
     */
    private transient int currentSize;

    /**
     * Constructor of an empty double linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     *
     * Time complexity: O(1)
     */
    public DoublyLinkedList( ) {
        head = null;
        tail = null;
        currentSize = 0;
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
     * Returns a two-way iterator of the elements in the list.
     *
     * @return Two-Way Iterator of the elements in the list
     *
     * Time complexity: O(1)
     */
    public TwoWayIterator<E> twoWayiterator() {
        return new TwoWayDoublyIterator<>(head, tail);
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
     * Inserts the element at the first position in the list.
     * @param element - Element to be inserted
     *
     * Time complexity: O(1)
     */
    public void addFirst( E element ) {
        DoublyListNode<E> newNode = new DoublyListNode<>(element,null,head);
        if(isEmpty()){
            head = tail = newNode;
        }
        else{
            head.setPrevious(newNode);
            head = newNode;
        }
        currentSize++;
    }

    /**
     * Inserts the element at the last position in the list.
     * @param element - Element to be inserted
     *
     * Time complexity: O(1)
     */
    public void addLast( E element ) {
        DoublyListNode<E> newNode = new DoublyListNode<>(element,tail, null);
        if(isEmpty()){
            head = tail = newNode;
        }
        else{
            tail.setNext(newNode);
            tail = newNode;
        }
        currentSize++;
    }

    /**
     * Returns the first element of the list.
     * @return first element in the list
     * @throws NoSuchElementException - if size() == 0
     *
     * Time complexity: O(1)
     */
    public E getFirst( ) {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    /**
     * Returns the last element of the list.
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     *
     * Time complexity: O(1)
     */
    public E getLast( ) {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    /**
     * Returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, get corresponds to getFirst.
     * If the specified position is size()-1, get corresponds to getLast.
     * @param position - position of element to be returned
     * @return element at position
     * @throws InvalidPositionException if position is not valid in the list
     *
     * Time complexity: O(n) in the worst case. This implementation optimizes by
     * starting from head or tail depending on position, so actual work is O(min(position, n-position)).
     */
    public E get( int position ) {
        if(position < 0 || position >= currentSize){
            throw new InvalidPositionException();
        }
        DoublyListNode<E> current;
        if(position <= currentSize / 2){
            current = head;
            for(int i = 0; i<position; i++){
                current = current.getNext();
            }
        }
        else{
            current = tail;
            for(int i = currentSize -1; i> position; i--){
                current = current.getPrevious();
            }
        }
        return current.getElement();
    }

    /**
     * Returns the position of the first occurrence of the specified element
     * in the list, if the list contains the element.
     * Otherwise, returns -1.
     * @param element - element to be searched in list
     * @return position of the first occurrence of the element in the list (or -1)
     *
     * Time complexity: O(n)
     */
    public int indexOf( E element ) {
        DoublyListNode<E> current = head;
        int index = 0;
        while(current != null){
            if((element == null && current.getElement() == null) || (current.getElement().equals(element))){
                return index;
            }
            current = current.getNext();
            index++;
        }
        return -1;
    }

    /**
     * Inserts the specified element at the specified position in the list.
     * Range of valid positions: 0, ..., size().
     * If the specified position is 0, add corresponds to addFirst.
     * If the specified position is size(), add corresponds to addLast.
     * @param position - position where to insert element
     * @param element - element to be inserted
     * @throws InvalidPositionException - if position is not valid in the list
     *
     * Time complexity: O(n) in the worst case (requires traversal to position).
     * If position==0 or position==size() complexity is O(1).
     */
    public void add( int position, E element ) {
        if (position < 0 || position > currentSize)
            throw new InvalidPositionException();

        if (position == 0)
            addFirst(element);
        else if (position == currentSize)
            addLast(element);
        else {
            DoublyListNode<E> current = head;
            for (int i = 0; i < position; i++)
                current = current.getNext();

            DoublyListNode<E> newNode =
                    new DoublyListNode<>(element, current.getPrevious(), current);
            current.getPrevious().setNext(newNode);
            current.setPrevious(newNode);

            currentSize++;
        }
    }

    /**
     * Removes and returns the element at the first position in the list.
     * @return element removed from the first position of the list
     * @throws NoSuchElementException - if size() == 0
     *
     * Time complexity: O(1)
     */
    public E removeFirst( ) {
        if (isEmpty())
            throw new NoSuchElementException();
        E element = head.getElement();
        if(head == tail){
            head = null;
            tail = null;
        }
        else {
            head = head.getNext();
            head.setPrevious(null);
        }
        currentSize--;
        return element;
    }

    /**
     * Removes and returns the element at the last position in the list.
     * @return element removed from the last position of the list
     * @throws NoSuchElementException - if size() == 0
     *
     * Time complexity: O(1)
     */
    public E removeLast( ) {
        if (isEmpty())
            throw new NoSuchElementException();
        E element = tail.getElement();
        if(head == tail){
            head = null;
            tail = null;
        }
        else {
            tail = tail.getPrevious();
            tail.setNext(null);
        }
        currentSize--;
        return element;
    }

    /**
     *  Removes and returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, remove corresponds to removeFirst.
     * If the specified position is size()-1, remove corresponds to removeLast.
     * @param position - position of element to be removed
     * @return element removed at position
     * @throws InvalidPositionException - if position is not valid in the list
     *
     * Time complexity: O(n) in the worst case (requires traversal to position).
     * If position==0 or position==size()-1 complexity is O(1).
     */
    public E remove( int position ) {
        if (position < 0 || position >= currentSize)
            throw new InvalidPositionException();

        if (position == 0)
            return removeFirst();
        else if (position == currentSize - 1)
            return removeLast();
        else {
            DoublyListNode<E> current = head;
            for (int i = 0; i < position; i++)
                current = current.getNext();

            E elem = current.getElement();
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());

            currentSize--;
            return elem;
        }
    }

    /**
     * Custom serialization: write the number of elements followed by each element
     * in list order. This avoids serializing the internal node objects and their
     * pointers (which may lead to deep recursion).
     *
     * @param oos stream to write to
     * @throws IOException on IO error or if an element is not serializable at runtime
     */
    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        // write size
        oos.writeInt(currentSize);

        // write each element in order
        DoublyListNode<E> current = head;
        while (current != null) {
            oos.writeObject(current.getElement());
            current = current.getNext();
        }
    }

    /**
     * Custom deserialization: read the number of elements and reconstruct the
     * internal linked nodes by calling addLast for each element read.
     *
     * @param ois stream to read from
     * @throws IOException if IO error occurs
     * @throws ClassNotFoundException if an element's class is not found
     */
    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        // initialize structure first (object fields are default-initialized before this call)
        head = null;
        tail = null;
        currentSize = 0;

        int size = ois.readInt();
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) ois.readObject();
            // reuse existing addLast to rebuild internal nodes
            this.addLast(element);
        }
    }
}