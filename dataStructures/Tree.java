package dataStructures;

import java.io.Serializable;

/**
 * Abstract Tree
 *
 * @author AED Team
 * @version 1.1
 * @param <E> Generic element
 */
abstract class Tree<E> implements Serializable {

    /**
     * Root node of the tree
     */
    protected transient Node<E> root;

    /**
     * Number of elements in the tree
     */
    protected transient int currentSize;

    /**
     * Constructor of an empty tree
     * Initializes root as null and currentSize as 0
     *
     * Time complexity: O(1)
     */
    public Tree() {
        root = null;
        currentSize = 0;
    }

    /**
     * Returns true iff the tree contains no entries.
     *
     * Time complexity: O(1)
     *
     * @return true if tree is empty
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Returns the number of entries in the tree.
     *
     * Time complexity: O(1)
     *
     * @return number of elements in the tree
     */
    public int size() {
        return currentSize;
    }

    /**
     * Returns the root node of the tree
     *
     * Time complexity: O(1)
     *
     * @return root node
     */
    Node<E> root() {
        return root;
    }
}
