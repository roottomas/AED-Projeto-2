package dataStructures;
/**
 * Advanced Binary Search Tree
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
abstract class AdvancedBSTree <K extends Comparable<K>,V> extends BSTSortedMap<K,V>{
      /**
 	* Performs a single left rotation rooted at z node.
 	* Node y was a  right  child  of z before the  rotation,
 	* then z becomes the left child of y after the rotation.
 	* @param z - root of the rotation
	 * @pre: z has a right child
 	*/
	protected void rotateLeft( BTNode<Entry<K,V>> z){
        BTNode<Entry<K,V>> y = (BTNode<Entry<K,V>>) z.getRightChild();
        BTNode<Entry<K,V>> zParent = (BTNode<Entry<K, V>>) z.getParent();
        BTNode<Entry<K,V>> yLeft = (BTNode<Entry<K, V>>) y.getLeftChild();
        if (zParent == null) {
            // z was root
            root = y;
            y.setParent(null);
        } else {
            // attach y to zParent appropriately
            if (zParent.getLeftChild() == z) {
                zParent.setLeftChild(y);
            } else {
                zParent.setRightChild(y);
            }
            y.setParent(zParent);
        }

        y.setLeftChild(z);
        z.setParent(y);

        z.setRightChild(yLeft);
        if (yLeft != null) yLeft.setParent(z);
	}

     /**
     * Performs a single right rotation rooted at z node.
     * Node y was a left  child  of z before the  rotation,
     * then z becomes the right child of y after the rotation.
     * @param z - root of the rotation
     * @pre: z has a left child
     */
    protected void rotateRight( BTNode<Entry<K,V>> z){
        BTNode<Entry<K,V>> y = (BTNode<Entry<K,V>>) z.getLeftChild();
        BTNode<Entry<K,V>> zParent = (BTNode<Entry<K, V>>) z.getParent();
        BTNode<Entry<K,V>> yRight = (BTNode<Entry<K, V>>) y.getRightChild();
        if (zParent == null) {
            // z was root
            root = y;
            y.setParent(null);
        } else {
            // attach y to zParent appropriately
            if (zParent.getLeftChild() == z) {
                zParent.setLeftChild(y);
            } else {
                zParent.setRightChild(y);
            }
            y.setParent(zParent);
        }

        // rotate
        y.setRightChild(z);
        z.setParent(y);

        z.setLeftChild(yRight);
        if (yRight != null) yRight.setParent(z);
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
     * @return the new root of the restructured subtree
     */
    protected BTNode<Entry<K,V>> restructure (BTNode<Entry<K,V>> x) {
        BTNode<Entry<K,V>> y = (BTNode<Entry<K,V>>) x.getParent();
        BTNode<Entry<K,V>> z = (BTNode<Entry<K,V>>) y.getParent();

        // Determine configuration: which child is y of z, and which child is x of y
        boolean yIsLeft = (z.getLeftChild() == y);
        boolean xIsLeft = (y.getLeftChild() == x);

        if (yIsLeft && xIsLeft) {
            // left-left case: single right rotation at z
            rotateRight(z);
            return y;
        } else if (!yIsLeft && !xIsLeft) {
            // right-right case: single left rotation at z
            rotateLeft(z);
            return y;
        } else if (yIsLeft) {
            // left-right case: double rotation (left at y, then right at z)
            rotateLeft(y);
            rotateRight(z);
            return x;
        } else {
            // right-left case: double rotation (right at y, then left at z)
            rotateRight(y);
            rotateLeft(z);
            return x;
        }
    }
}
