package dataStructures;

import dataStructures.exceptions.NoSuchElementException;
import java.io.Serial;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * AVL Tree Sorted Map
 * @author AED Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class AVLSortedMap<K extends Comparable<K>, V> extends AdvancedBSTree<K, V> {

    public AVLSortedMap() {
        super();
    }

    /**
     * Insert or update a key-value pair in the map.
     * @param key Key to insert or update
     * @param value Value to insert
     * Time complexity: O(log n)
     * @return The old value if the key already existed, or null otherwise
     */
    @Override
    public V put(K key, V value) {
        V old = super.put(key, value);
        if (old == null) {
            currentSize++;
        }
        @SuppressWarnings("unchecked")
        AVLNode<Entry<K, V>> node = (AVLNode<Entry<K, V>>) super.locateNode(root, key);
        if (node != null) rebalanceUp(node);
        return old;
    }

    /**
     * Remove an entry by key from the map.
     * @param key whose entry is to be removed from the map
     * Time complexity: O(log n)
     * @return The removed value if the key existed, or null otherwise
     */
    @Override
    public V remove(K key) throws NoSuchElementException {
        @SuppressWarnings("unchecked")
        AVLNode<Entry<K, V>> node = (AVLNode<Entry<K, V>>) super.locateNode(root, key);
        if (node == null) throw new NoSuchElementException();

        @SuppressWarnings("unchecked")
        AVLNode<Entry<K, V>> parent = (AVLNode<Entry<K, V>>) node.getParent();
        V old = super.remove(key);
        if (parent != null) rebalanceUp(parent);
        else if (root != null) rebalanceUp((AVLNode<Entry<K, V>>) root);
        currentSize--;
        return old;
    }

    /**
     * Returns the child of a node with greater height.
     * @param n parent node
     * Time complexity: O(1)
     * @return left or right child, whichever is taller
     */
    private AVLNode<Entry<K, V>> tallerChild(AVLNode<Entry<K, V>> n) {
        AVLNode<Entry<K, V>> left = (AVLNode<Entry<K, V>>) n.getLeftChild();
        AVLNode<Entry<K, V>> right = (AVLNode<Entry<K, V>>) n.getRightChild();
        if (left == null && right == null) return n;
        if (left == null) return right;
        if (right == null) return left;
        return (left.getHeight() >= right.getHeight()) ? left : right;
    }

    /**
     * Walk upward from a node, updating heights and restoring AVL balance when needed.
     * @param node starting point for rebalancing
     * Time complexity: O(log n)
     */
    private void rebalanceUp(AVLNode<Entry<K, V>> node) {
        AVLNode<Entry<K, V>> cur = node;
        while (cur != null) {
            cur.updateHeight();
            int bf = cur.getBalanceFactor();
            if (Math.abs(bf) > 1) {
                AVLNode<Entry<K, V>> y = tallerChild(cur);
                AVLNode<Entry<K, V>> x = tallerChild(y);
                AVLNode<Entry<K, V>> newRoot = (AVLNode<Entry<K, V>>) restructure(x);

                AVLNode<Entry<K, V>> left = (AVLNode<Entry<K, V>>) newRoot.getLeftChild();
                AVLNode<Entry<K, V>> right = (AVLNode<Entry<K, V>>) newRoot.getRightChild();
                if (left != null) left.updateHeight();
                if (right != null) right.updateHeight();
                newRoot.updateHeight();

                cur = (AVLNode<Entry<K, V>>) newRoot.getParent();
            } else {
                cur = (AVLNode<Entry<K, V>>) cur.getParent();
            }
        }
    }

    /**
     * Create a new AVL node for the map.
     * @param entry key-value pair
     * @param parent parent node
     * Time complexity: O(1)
     * @return newly created AVLNode
     */
    @Override
    protected BTNode<Map.Entry<K, V>> createNode(Map.Entry<K, V> entry, BTNode<Map.Entry<K, V>> parent) {
        return new AVLNode<>(entry, (AVLNode<Map.Entry<K, V>>) parent);
    }

    /**
     * Writes all entries of the subtree rooted at node in-order.
     * @param out output stream
     * @param node root of subtree
     * @throws IOException if writing fails
     * Time complexity: O(n)
     */
    private void writeEntries(ObjectOutputStream out, BTNode<Map.Entry<K, V>> node) throws IOException {
        if (node == null) return;
        writeEntries(out, (BTNode<Entry<K, V>>) node.getLeftChild());
        Map.Entry<K, V> e = node.getElement();
        out.writeObject(e.key());
        out.writeObject(e.value());
        writeEntries(out, (BTNode<Map.Entry<K, V>>) node.getRightChild());
    }

    /**
     * Reads n entries from a stream and inserts them into the AVL.
     * @param in input stream
     * @param n number of entries to read
     * @throws IOException
     * @throws ClassNotFoundException
     * Time complexity: O(n log n)
     */
    private void readEntries(ObjectInputStream in, int n) throws IOException, ClassNotFoundException {
        K key;
        V value;
        root = null;
        currentSize = 0;
        for (int i = 0; i < n; i++) {
            key = (K) in.readObject();
            value = (V) in.readObject();
            put(key, value);
        }
    }

    /**
     * Custom serialization: writes map size and all entries.
     * @param out output stream
     * @throws IOException
     * Time complexity: O(n)
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        BTNode<Map.Entry<K, V>> savedRoot = (BTNode<Entry<K, V>>) root;
        try {
            root = null;
            out.defaultWriteObject();
        } finally {
            root = savedRoot;
        }
        out.writeInt(currentSize);
        writeEntries(out, savedRoot);
    }

    /**
     * Custom deserialization: rebuilds the AVL tree from serialized entries.
     * @param in input stream
     * @throws IOException
     * @throws ClassNotFoundException
     * Time complexity: O(n log n)
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int n = in.readInt();
        readEntries(in, n);
    }
}
