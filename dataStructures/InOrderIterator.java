package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * In-order Binary Tree iterator.
 * Iterates the elements of a binary tree in-order (left-root-right).
 *
 * @param <E> Generic element type
 * @author AED Team
 * @version 1.0
 */
public class InOrderIterator<E> implements Iterator<E> {

    /** Node with the current element */
    private BTNode<E> next;

    /** Root node of the tree */
    private BTNode<E> root;

    /**
     * Constructor with root node.
     *
     * @param root root of the binary tree to iterate
     * Time complexity: O(h) where h is the height of the tree (to find leftmost element)
     */
    public InOrderIterator(BTNode<E> root) {
        this.root = root;
        rewind();
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * Time complexity: O(1)
     * @return true if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return next != null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * Time complexity: O(1) amortized per element
     * @return the next element in the iteration
     * @throws NoSuchElementException if there is no next element
     */
    @Override
    public E next() {
        if (!hasNext())
            throw new NoSuchElementException();
        E elem = next.getElement();
        advance();
        return elem;
    }

    /**
     * Advances the iterator to the next element in in-order traversal.
     *
     * Time complexity: O(h) worst-case per advance, where h is the height of the tree
     */
    private void advance() {
        if (next.getRightChild() != null) {
            // Move to the leftmost node in the right subtree
            next = (BTNode<E>) next.getRightChild();
            while (next.getLeftChild() != null) {
                next = (BTNode<E>) next.getLeftChild();
            }
        } else {
            // Move up until we find a node that is left child of its parent
            BTNode<E> parent = (BTNode<E>) next.getParent();
            while (parent != null && next == parent.getRightChild()) {
                next = parent;
                parent = (BTNode<E>) parent.getParent();
            }
            next = parent;
        }
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     *
     * Time complexity: O(h), where h = height of the tree
     */
    public void rewind() {
        if (root == null)
            next = null;
        else
            next = root.furtherLeftElement();
    }
}
