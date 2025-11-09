package dataStructures;
/**
 * AVL Tree Node
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
class AVLNode<E> extends BTNode<E> {
    // Height of the node
    protected int height;

    public AVLNode(E elem) {
        super(elem);
        height=0;
    }
    
    public AVLNode( E element, AVLNode<E> parent,
                    AVLNode<E> left, AVLNode<E> right ){
        super(element,parent,left,right);
        int leftH = height(left);
        int rightH = height(right);
        this.height = 1 + Math.max(leftH, rightH);
    }
    public AVLNode( E element, AVLNode<E> parent){
        super(element, parent,null, null);
        height= 0;
    }

    private int height(AVLNode<E> no) {
        if (no==null)	return -1;
        return no.getHeight();
    }
    public int getHeight() {
        return height;
    }

    /**
     * Update the left child and height
     * @param node
     */
    public void setLeftChild(AVLNode<E> node) {
        super.setLeftChild(node);
        // maintain parent pointer of child
        if (node != null) node.setParent(this);
        // recalculate this node height
        updateHeight();
    }

    /**
     * Update the right child and height
     * @param node
     */
    public void setRightChild(AVLNode<E> node) {
        // update pointer in superclass
        super.setRightChild(node);
        // maintain parent pointer of child
        if (node != null) node.setParent(this);
        // recalculate this node height
        updateHeight();
    }

    /**
     * Recomputes this node height from its children.
     */
    public void updateHeight() {
        AVLNode<E> left = (AVLNode<E>) getLeftChild();
        AVLNode<E> right = (AVLNode<E>) getRightChild();
        int leftH = height(left);
        int rightH = height(right);
        this.height = 1 + Math.max(leftH, rightH);
    }

    /**
     * Balance factor = height(left) - height(right)
     * positive => left heavier, negative => right heavier
     * @return balance factor
     */
    public int getBalanceFactor() {
        AVLNode<E> left = (AVLNode<E>) getLeftChild();
        AVLNode<E> right = (AVLNode<E>) getRightChild();
        return height(left) - height(right);
    }
// others public methods
//TODO: Left as an exercise.
}
