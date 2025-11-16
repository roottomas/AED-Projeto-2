package dataStructures;

/**
 * Advanced Binary Search Tree
 * @author AED Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
abstract class AdvancedBSTree <K extends Comparable<K>,V> extends BSTSortedMap<K,V>{

    /**
     * Performs a single left rotation rooted at z node.
     * Node y was a right child of z before the rotation,
     * then z becomes the left child of y after the rotation.
     * Time complexity: O(1)
     * @param z - root of the rotation
     * @pre: z has a right child
     */
    protected void rotateLeft( BTNode<Entry<K,V>> z){
        BTNode<Entry<K,V>> y = (BTNode<Entry<K,V>>) z.getRightChild();
        BTNode<Entry<K,V>> zParent = (BTNode<Entry<K, V>>) z.getParent();
        BTNode<Entry<K,V>> yLeft = (BTNode<Entry<K, V>>) y.getLeftChild();
        rotateHelper(z, y, zParent);

        y.setLeftChild(z);
        z.setParent(y);

        z.setRightChild(yLeft);
        if (yLeft != null) yLeft.setParent(z);
    }

    /**
     * Performs a single right rotation rooted at z node.
     * Node y was a left child of z before the rotation,
     * then z becomes the right child of y after the rotation.
     * Time complexity: O(1)
     * @param z - root of the rotation
     * @pre: z has a left child
     */
    protected void rotateRight( BTNode<Entry<K,V>> z){
        BTNode<Entry<K,V>> y = (BTNode<Entry<K,V>>) z.getLeftChild();
        BTNode<Entry<K,V>> zParent = (BTNode<Entry<K, V>>) z.getParent();
        BTNode<Entry<K,V>> yRight = (BTNode<Entry<K, V>>) y.getRightChild();
        rotateHelper(z, y, zParent);

        y.setRightChild(z);
        z.setParent(y);

        z.setLeftChild(yRight);
        if (yRight != null) yRight.setParent(z);
    }

    /**
     * Auxiliary rotation attachment helper.
     * Attaches node y in place of node z in the tree, updating the parent
     * references accordingly. Used internally by rotateLeft and rotateRight.
     * Time complexity: O(1)
     * @param z the original root of the rotation
     * @param y the node that replaces z as subtree root
     * @param zParent the parent of z before the rotation
     */
    private void rotateHelper(BTNode<Entry<K, V>> z, BTNode<Entry<K, V>> y, BTNode<Entry<K, V>> zParent) {
        if (zParent == null) {
            root = y;
            y.setParent(null);
        } else {
            if (zParent.getLeftChild() == z) {
                zParent.setLeftChild(y);
            } else {
                zParent.setRightChild(y);
            }
            y.setParent(zParent);
        }
    }

    /**
     * Performs a tri-node restructuring (a single or double rotation rooted at X node).
     * Assumes the nodes are in one of following configurations:
     *
     * @param x - root of the rotation
     * <pre>
     *          z=c       z=c        z=a         z=a
     *          /  \      /  \       /  \        /  \
     *        y=b  t4   y=a  t4    t1  y=c     t1  y=b
     *       /  \      /  \           /  \         /  \
     *     x=a  t3    t1 x=b        x=b  t4       t2 x=c
     *    /  \          /  \       /  \             /  \
     *   t1  t2        t2  t3     t2  t3           t3  t4
     * </pre>
     * Time complexity: O(1)
     * @return the new root of the restructured subtree
     */
    protected BTNode<Entry<K,V>> restructure (BTNode<Entry<K,V>> x) {
        BTNode<Entry<K,V>> y = (BTNode<Entry<K,V>>) x.getParent();
        BTNode<Entry<K,V>> z = (BTNode<Entry<K,V>>) y.getParent();

        boolean yIsLeft = (z.getLeftChild() == y);
        boolean xIsLeft = (y.getLeftChild() == x);

        if (yIsLeft && xIsLeft) {
            rotateRight(z);
            return y;
        } else if (!yIsLeft && !xIsLeft) {
            rotateLeft(z);
            return y;
        } else if (yIsLeft) {
            rotateLeft(y);
            rotateRight(z);
            return x;
        } else {
            rotateRight(y);
            rotateLeft(z);
            return x;
        }
    }
}
