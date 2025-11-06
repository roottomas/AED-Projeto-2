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
            root = new BTNode<>(new Entry<>(key, value));
            return null;
        }
        BTNode<Entry<K, V>> current = (BTNode<Entry<K, V>>) root;
        BTNode<Entry<K, V>> parent = null;
        int cmp = 0;

        while (current != null) {
            parent = current;
            cmp = key.compareTo(current.getElement().key());
            if (cmp == 0) {
                // Key exists, update value
                V oldValue = current.getElement().value();
                current.setElement(new Entry<>(key, value));
                return oldValue;
            } else if (cmp < 0) {
                current = (BTNode<Entry<K, V>>) current.getLeftChild();
            } else {
                current = (BTNode<Entry<K, V>>) current.getRightChild();
            }
        }

        BTNode<Entry<K, V>> newNode = new BTNode<>(new Entry<>(key, value), parent);
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
        BTNode<Entry<K,V>> node = locateNode(key);
        BTNode<Entry<K,V>> parent = (BTNode<Entry<K,V>>) node.getParent();

        V old = super.remove(key);

        BTNode<Entry<K,V>> a = parent;
        while (a != null) {
            int leftH = (a.getLeftChild() == null) ? 0 : ((BTNode<Entry<K,V>>) a.getLeftChild()).getHeight();
            int rightH = (a.getRightChild() == null) ? 0 : ((BTNode<Entry<K,V>>) a.getRightChild()).getHeight();

            if (Math.abs(leftH - rightH) <= 1) {
                a = (BTNode<Entry<K,V>>) a.getParent();
                continue;
            }

            BTNode<Entry<K,V>> y = (leftH > rightH) ? (BTNode<Entry<K,V>>) a.getLeftChild()
                    : (BTNode<Entry<K,V>>) a.getRightChild();

            // pick x as taller child of y (tie-breaking prefers left)
            BTNode<Entry<K,V>> yLeft = (y.getLeftChild() == null) ? null : (BTNode<Entry<K,V>>) y.getLeftChild();
            BTNode<Entry<K,V>> yRight = (y.getRightChild() == null) ? null : (BTNode<Entry<K,V>>) y.getRightChild();
            int yLeftH = (yLeft == null) ? 0 : yLeft.getHeight();
            int yRightH = (yRight == null) ? 0 : yRight.getHeight();
            BTNode<Entry<K,V>> x = (yLeftH >= yRightH) ? yLeft : yRight;
            if (x == null) x = y;

            BTNode<Entry<K,V>> newRoot = restructure(x);

            a = (BTNode<Entry<K,V>>) ((newRoot != null) ? newRoot.getParent() : null);
        }

        return old;
    }

    /**
     * Helper: locate node with given key starting at root (returns null if not found).
     * We only traverse — we do NOT re-implement insertion/removal logic here.
     */
    private BTNode<Entry<K,V>> locateNode(K key) {
        BTNode<Entry<K,V>> curr = (BTNode<Entry<K,V>>) root;
        while (curr != null) {
            int cmp = key.compareTo(curr.getElement().key());
            if (cmp == 0) return curr;
            curr = (cmp < 0) ? (BTNode<Entry<K,V>>) curr.getLeftChild() : (BTNode<Entry<K,V>>) curr.getRightChild();
        }
        return null;
    }
}