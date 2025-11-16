package dataStructures;

import dataStructures.exceptions.*;

/**
 * Stack implemented using a List in Array
 *
 * @author AED team
 * @version 1.1
 *
 * @param <E> Generic Element
 */
public class StackWithListInArray<E> implements Stack<E> {

    static final int DEFAULT_CAPACITY = 1000;
    static final int EMPTY = -1;

    private List<E> array;
    private int capacity;

    public StackWithListInArray(int capacity) {
        array = new ListInArray<>(capacity);
        this.capacity = capacity;
    }

    public StackWithListInArray() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Returns true iff the stack contains no elements.
     *
     * Time complexity: O(1)
     *
     * @return true iff the stack is empty
     */
    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    /**
     * Returns the number of elements in the stack.
     *
     * Time complexity: O(1)
     *
     * @return number of elements in the stack
     */
    @Override
    public int size() {
        return array.size();
    }

    /**
     * Returns the element at the top of the stack.
     *
     * Time complexity: O(1)
     *
     * @return element at top of stack
     * @throws EmptyStackException when size = 0
     */
    @Override
    public E top() {
        if (isEmpty())
            throw new EmptyStackException();
        return array.getLast();
    }

    /**
     * Inserts the specified element onto the top of the stack.
     *
     * Time complexity: O(1)
     *
     * @param element element to be inserted onto the stack
     * @throws FullStackException when size = capacity
     */
    @Override
    public void push(E element) {
        if (size() == capacity) {
            throw new FullStackException();
        }
        array.addLast(element);
    }

    /**
     * Removes and returns the element at the top of the stack.
     *
     * Time complexity: O(1)
     *
     * @return element removed from top of stack
     * @throws EmptyStackException when size = 0
     */
    @Override
    public E pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return array.removeLast();
    }
}
