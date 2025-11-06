package dataStructures;
/**
 * AVL Tree Sorted Map
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class AVLSortedMap <K extends Comparable<K>,V> extends AdvancedBSTree<K,V>{
    /**
     * 
     * @param key
     * @param value
     * @return
     */
    public V insert(K key, V value) {
        if (root == null) {
            root = new AVLNode<>(new Entry<>(key, value));
            return null;
        }

        AVLNode<Entry<K, V>> current = (AVLNode<Entry<K, V>>) root;
        AVLNode<Entry<K, V>> parent = null;
        int cmp = 0;

        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.getElement().key());
            if (cmp == 0) {
                // Key exists, replace value
                V oldValue = current.getElement().value();
                current.setElement(new Entry<>(key, value));
                return oldValue;
            }
            current = (cmp < 0) ? (AVLNode<Entry<K, V>>) current.getLeftChild()
                    : (AVLNode<Entry<K, V>>) current.getRightChild();
        }

        AVLNode<Entry<K, V>> newNode = new AVLNode<>(new Entry<>(key, value), parent);
        if (cmp < 0) {
            parent.setLeftChild(newNode);
        } else {
            parent.setRightChild(newNode);
        }

        restructure(newNode);
        return null;
    }

    /**
     *
     * @param key whose entry is to be removed from the map
     * @return
     */
    public V remove(K key) {
        AVLNode<Entry<K, V>> node = locateNode(key);
        if (node == null) return null;

        V oldValue = node.getElement().value();
        super.remove(key);  // Remove the node via BST logic

        AVLNode<Entry<K, V>> parent = (AVLNode<Entry<K, V>>) node.getParent();
        restructure(parent);

        return oldValue;
    }

    /**
     * Helper: locate node with given key starting at root (returns null if not found).
     */
    private AVLNode<Entry<K, V>> locateNode(K key) {
        AVLNode<Entry<K, V>> curr = (AVLNode<Entry<K, V>>) root;
        while (curr != null) {
            int cmp = key.compareTo(curr.getElement().key());
            if (cmp == 0) return curr;
            curr = (cmp < 0) ? (AVLNode<Entry<K, V>>) curr.getLeftChild()
                    : (AVLNode<Entry<K, V>>) curr.getRightChild();
        }
        return null;
    }
}