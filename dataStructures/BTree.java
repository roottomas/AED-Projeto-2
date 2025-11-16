package dataStructures;

/**
 * Abstract Binary Tree
 * Provides basic binary tree operations.
 *
 * @author AED Team
 * @version 1.0
 * @param <E> Generic Element
 */
abstract class BTree<E> extends Tree<E> {

    /**
     * Returns the height of the tree.
     *
     * @return height of the tree, 0 if empty
     * Time complexity: O(n) for a general tree
     */
    public int getHeight() {
        if (isEmpty())
            return 0;
        return ((BTNode<E>) root).getHeight();
    }

    /**
     * Returns the leftmost node of the tree.
     *
     * @return leftmost node, or null if tree is empty
     * Time complexity: O(h), h = height of tree
     */
    BTNode<E> furtherLeftElement() {
        if (isEmpty()) return null;
        BTNode<E> node = (BTNode<E>) root;
        while (node.getLeftChild() != null) {
            node = (BTNode<E>) node.getLeftChild();
        }
        return node;
    }

    /**
     * Returns the rightmost node of the tree.
     *
     * @return rightmost node, or null if tree is empty
     * Time complexity: O(h), h = height of tree
     */
    BTNode<E> furtherRightElement() {
        if (isEmpty()) return null;
        BTNode<E> node = (BTNode<E>) root;
        while (node.getRightChild() != null) {
            node = (BTNode<E>) node.getRightChild();
        }
        return node;
    }

    /**
     * Returns the leftmost node starting from a given node.
     *
     * @param start node to start from
     * @return leftmost descendant from start, or null if start is null
     * Time complexity: O(h), h = height of subtree
     */
    protected BTNode<E> furtherLeftElement(BTNode<E> start) {
        if (start == null) return null;
        BTNode<E> node = start;
        while (node.getLeftChild() != null) {
            node = (BTNode<E>) node.getLeftChild();
        }
        return node;
    }

    /**
     * Returns the rightmost node starting from a given node.
     *
     * @param start node to start from
     * @return rightmost descendant from start, or null if start is null
     * Time complexity: O(h), h = height of subtree
     */
    protected BTNode<E> furtherRightElement(BTNode<E> start) {
        if (start == null) return null;
        BTNode<E> node = start;
        while (node.getRightChild() != null) {
            node = (BTNode<E>) node.getRightChild();
        }
        return node;
    }

    /**
     * Replaces subtree rooted at u with subtree rooted at v.
     *
     * @param u node to replace
     * @param v replacement subtree (can be null)
     * Time complexity: O(1)
     */
    protected void transplant(BTNode<E> u, Node<E> v) {
        BTNode<E> uParent = (BTNode<E>) u.getParent();
        if (u.isRoot()) {
            // v becomes root (v can be null)
            root = v;
            if (v != null) ((BTNode<E>) v).setParent(null);
        } else {
            if (uParent.getLeftChild() == u) {
                uParent.setLeftChild(v);
            } else {
                uParent.setRightChild(v);
            }
            if (v != null) ((BTNode<E>) v).setParent(uParent);
        }
    }
}
