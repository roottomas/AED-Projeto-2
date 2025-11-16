package dataStructures;

import java.io.Serializable;

/**
 * Singly List Node Implementation
 * Node used in a singly linked list.
 * @param <E> Generic Element
 */
class SinglyListNode<E> implements Serializable {

    /** Serial Version UID of the Class */
    static final long serialVersionUID = 0L;

    /** Element stored in the node */
    private E element;

    /** Reference to the next node */
    private SinglyListNode<E> next;

    /**
     * Constructor with element and next node reference
     * Time complexity: O(1)
     * @param theElement The element to store
     * @param theNext The next node in the list
     */
    public SinglyListNode(E theElement, SinglyListNode<E> theNext) {
        element = theElement;
        next = theNext;
    }

    /**
     * Constructor with only element (next is null)
     * Time complexity: O(1)
     * @param theElement The element to store
     */
    public SinglyListNode(E theElement) {
        this(theElement, null);
    }

    /**
     * Returns the element contained in this node
     * Time complexity: O(1)
     * @return element in node
     */
    public E getElement() {
        return element;
    }

    /**
     * Returns the next node in the list
     * Time complexity: O(1)
     * @return reference to next node
     */
    public SinglyListNode<E> getNext() {
        return next;
    }

    /**
     * Updates the element stored in this node
     * Time complexity: O(1)
     * @param newElement New element to store
     */
    public void setElement(E newElement) {
        element = newElement;
    }

    /**
     * Updates the reference to the next node
     * Time complexity: O(1)
     * @param newNext New next node reference
     */
    public void setNext(SinglyListNode<E> newNext) {
        next = newNext;
    }
}
