package dataStructures;
/**
 * Binary Tree
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
abstract class BTree<E> extends Tree<E> {

    /**
     * Returns the height of the tree.
     */
    public int getHeight() {
        if(isEmpty())
            return 0;
        return ((BTNode<E>)root).getHeight();
    }

    /**
     * Return the further left node of the tree
     * @return further left node of the tree
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
     * Return the further right node of the tree
     * @return further right node of the tree
     */
    BTNode<E> furtherRightElement() {
        if (isEmpty()) return null;

        BTNode<E> node = (BTNode<E>) root;
        while (node.getRightChild() != null) {
            node = (BTNode<E>) node.getRightChild();
        }
        return node;
    }

   //new methods: Left as an exercise.
}
