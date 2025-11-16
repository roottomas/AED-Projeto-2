package dataStructures;

/**
 * Binary Tree Node
 * Represents a node in a binary tree.
 *
 * @author AED Team
 * @version 1.0
 * @param <E> Generic Element
 */
class BTNode<E> implements Node<E> {
    private E element;
    private Node<E> parent;
    private Node<E> leftChild;
    private Node<E> rightChild;

    /**
     * Constructor with element only.
     * @param elem element to store in the node
     * Time complexity: O(1)
     */
    BTNode(E elem){
        this(elem, null, null, null);
    }

    /**
     * Constructor with element and parent.
     * @param elem element
     * @param parent parent node
     * Time complexity: O(1)
     */
    BTNode(E elem, BTNode<E> parent) {
        this(elem, parent, null, null);
    }

    /**
     * Full constructor with element, parent, left child, and right child.
     * @param elem element
     * @param parent parent node
     * @param leftChild left child
     * @param rightChild right child
     * Time complexity: O(1)
     */
    BTNode(E elem, BTNode<E> parent, BTNode<E> leftChild, BTNode<E> rightChild){
        this.element = elem;
        this.parent = parent;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        if(leftChild != null) ((BTNode<E>) leftChild).setParent(this);
        if(rightChild != null) ((BTNode<E>) rightChild).setParent(this);
    }

    /** Returns the element stored in this node. Time complexity: O(1) */
    public E getElement() { return element; }

    /** Returns the left child node. Time complexity: O(1) */
    public Node<E> getLeftChild() { return leftChild; }

    /** Returns the right child node. Time complexity: O(1) */
    public Node<E> getRightChild() { return rightChild; }

    /** Returns the parent node. Time complexity: O(1) */
    public Node<E> getParent() { return parent; }

    /** Returns true if node has no children. Time complexity: O(1) */
    boolean isLeaf() { return leftChild == null && rightChild == null; }

    /** Updates the element stored in this node. Time complexity: O(1) */
    public void setElement(E elem) { element = elem; }

    /** Updates the left child and its parent pointer. Time complexity: O(1) */
    public void setLeftChild(Node<E> node) {
        leftChild = node;
        if (node != null) ((BTNode<E>) node).setParent(this);
    }

    /** Updates the right child and its parent pointer. Time complexity: O(1) */
    public void setRightChild(Node<E> node) {
        rightChild = node;
        if (node != null) ((BTNode<E>) node).setParent(this);
    }

    /** Updates the parent pointer. Time complexity: O(1) */
    public void setParent(Node<E> node) { parent = node; }

    /** Returns true if this node is the root. Time complexity: O(1) */
    boolean isRoot() { return parent == null; }

    /**
     * Returns the height of the subtree rooted at this node.
     * Height of a leaf node is 1.
     * Time complexity: O(n) for a general binary tree
     *
     * @return height of subtree
     */
    public int getHeight() {
        int leftH = (leftChild != null) ? ((BTNode<E>) leftChild).getHeight() : 0;
        int rightH = (rightChild != null) ? ((BTNode<E>) rightChild).getHeight() : 0;
        return 1 + Math.max(leftH, rightH);
    }

    /**
     * Returns the leftmost descendant of this node.
     * Time complexity: O(h), h = height of subtree
     *
     * @return furthest left node
     */
    BTNode<E> furtherLeftElement() {
        BTNode<E> node = this;
        while (node.getLeftChild() != null) node = (BTNode<E>) node.getLeftChild();
        return node;
    }

    /**
     * Returns the rightmost descendant of this node.
     * Time complexity: O(h), h = height of subtree
     *
     * @return furthest right node
     */
    BTNode<E> furtherRightElement() {
        BTNode<E> node = this;
        while (node.getRightChild() != null) node = (BTNode<E>) node.getRightChild();
        return node;
    }

    // Additional methods (like subtree size, traversal helpers) can be added here.
}
