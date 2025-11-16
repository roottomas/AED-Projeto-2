package dataStructures;

/**
 * AVL Tree Node
 * @author AED Team
 * @version 1.0
 * @param <E> Generic Element
 */
class AVLNode<E> extends BTNode<E> {
    // Height of the node
    protected int height;

    public AVLNode(E elem) {
        super(elem);
        height = 0;
    }

    public AVLNode(E element, AVLNode<E> parent,
                   AVLNode<E> left, AVLNode<E> right) {
        super(element, parent, left, right);
        int leftH = height(left);
        int rightH = height(right);
        this.height = 1 + Math.max(leftH, rightH);
    }

    public AVLNode(E element, AVLNode<E> parent) {
        super(element, parent, null, null);
        height = 0;
    }

    /**
     * Computes the height of a given node.
     * @param no the node whose height is requested
     * Time complexity: O(1)
     * @return height of node, -1 if null
     */
    private int height(AVLNode<E> no) {
        if (no == null) return -1;
        return no.getHeight();
    }

    /**
     * Returns the height of this node.
     * Time complexity: O(1)
     * @return height of this node
     */
    public int getHeight() {
        return height;
    }

    /**
     * Update the left child and height
     * @param node new left child
     * Time complexity: O(1)
     */
    public void setLeftChild(AVLNode<E> node) {
        super.setLeftChild(node);
        if (node != null) node.setParent(this);
        updateHeight();
    }

    /**
     * Update the right child and height
     * @param node new right child
     * Time complexity: O(1)
     */
    public void setRightChild(AVLNode<E> node) {
        super.setRightChild(node);
        if (node != null) node.setParent(this);
        updateHeight();
    }

    /**
     * Recomputes this node height from its children.
     * Time complexity: O(1)
     */
    public void updateHeight() {
        AVLNode<E> left = (AVLNode<E>) getLeftChild();
        AVLNode<E> right = (AVLNode<E>) getRightChild();
        int leftH = height(left);
        int rightH = height(right);
        this.height = 1 + Math.max(leftH, rightH);
    }

    /**
     * Balance factor = height(left) - height(right).
     * Positive => left subtree heavier; negative => right subtree heavier.
     * Time complexity: O(1)
     * @return balance factor
     */
    public int getBalanceFactor() {
        AVLNode<E> left = (AVLNode<E>) getLeftChild();
        AVLNode<E> right = (AVLNode<E>) getRightChild();
        return height(left) - height(right);
    }
}
