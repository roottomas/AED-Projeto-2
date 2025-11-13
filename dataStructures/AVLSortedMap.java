package dataStructures;

import dataStructures.exceptions.NoSuchElementException;
import java.io.Serial;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * AVL Tree Sorted Map
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class AVLSortedMap <K extends Comparable<K>,V> extends AdvancedBSTree<K,V>{

    public AVLSortedMap(){
        super();
    }

    @Override
    public V put(K key, V value) {
        V old = super.put(key, value);
        // locate the node we just inserted/updated (safe traversal from root)
        AVLNode<Entry<K,V>> node = locateNode((AVLNode<Entry<K,V>>) root, key);
        rebalanceUp(node);
        currentSize++;
        return old;
    }

    @Override
    public V remove(K key) throws NoSuchElementException {
        // locate node before removal so we can have the parent to start rebalancing
        AVLNode<Entry<K,V>> node = locateNode((AVLNode<Entry<K,V>>) root, key);
        if (node == null) throw new NoSuchElementException();
        AVLNode<Entry<K,V>> parent = (AVLNode<Entry<K,V>>) node.getParent();

        V old = super.remove(key);

        // rebalance from parent (or root)
        if (parent != null) rebalanceUp(parent);
        else if (root != null) rebalanceUp((AVLNode<Entry<K,V>>) root);
        currentSize--;
        return old;
    }

    /**
     * Locate node by key starting at startNode (BTNode) and return it as AVLNode (or null).
     */
    private AVLNode<Entry<K,V>> locateNode(AVLNode<Entry<K,V>> startNode, K key) {
        AVLNode<Entry<K,V>> current = startNode;
        while (current != null) {
            int cmp = key.compareTo(current.getElement().key());
            if (cmp == 0) return current;
            current = (cmp < 0) ? (AVLNode<Entry<K,V>>) current.getLeftChild()
                    : (AVLNode<Entry<K,V>>) current.getRightChild();
        }
        return null;
    }

    private AVLNode<Entry<K, V>> tallerChild(AVLNode<Entry<K, V>> n) {
        AVLNode<Entry<K, V>> left = (AVLNode<Entry<K, V>>) n.getLeftChild();
        AVLNode<Entry<K, V>> right = (AVLNode<Entry<K, V>>) n.getRightChild();
        if(left == null && right == null) return n;
        if(left == null) return right;
        if(right == null) return left;
        if (left.getHeight() >= right.getHeight()) return left;
        else return right;
    }

    /**
     * Walk up from node, update heights, and when |balance|>1 perform tri-node restructure.
     */
    private void rebalanceUp(AVLNode<Entry<K,V>> node) {
        AVLNode<Entry<K, V>> cur = node;
        while (cur != null) {
            // recompute height for cur (children might have changed)
            cur.updateHeight();
            int bf = cur.getBalanceFactor();
            if (Math.abs(bf) > 1) {
                // find y = taller child of cur (z), x = taller child of y
                AVLNode<Entry<K, V>> y = tallerChild(cur);
                AVLNode<Entry<K, V>> x = tallerChild(y);
                AVLNode<Entry<K, V>> newRoot;
                newRoot = (AVLNode<Entry<K, V>>) restructure(x);

                // update heights in the rotated subtree: children then root
                AVLNode<Entry<K, V>> newRootAVL = newRoot;
                AVLNode<Entry<K, V>> left = (AVLNode<Entry<K, V>>) newRootAVL.getLeftChild();
                AVLNode<Entry<K, V>> right = (AVLNode<Entry<K, V>>) newRootAVL.getRightChild();
                if (left != null) left.updateHeight();
                if (right != null) right.updateHeight();
                newRootAVL.updateHeight();

                // continue from parent of the subtree root (could be null -> done)
                cur = (AVLNode<Entry<K, V>>) newRootAVL.getParent();
            } else {
                // no imbalance here: move to parent
                cur = (AVLNode<Entry<K, V>>) cur.getParent();
            }
        }
    }

    @Override
    protected BTNode<Map.Entry<K,V>> createNode(Map.Entry<K,V> entry, BTNode<Map.Entry<K,V>> parent) {
        return new AVLNode<>(entry, (AVLNode<Map.Entry<K,V>>) parent);
    }

    private void writeEntries(ObjectOutputStream out, BTNode<Map.Entry<K,V>> node) throws IOException {
        if (node == null) return;
        // left
        writeEntries(out, (BTNode<Entry<K,V>>) node.getLeftChild());
        // node element
        Map.Entry<K,V> e = node.getElement();
        out.writeObject(e.key());
        out.writeObject(e.value());
        // right
        writeEntries(out, (BTNode<Map.Entry<K,V>>) node.getRightChild());
    }

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
     * Custom serialization: avoid serializing the recursive node graph via defaultWriteObject
     * by temporarily nulling `root`. Then serialize logical contents (size + key/value pairs).
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
     * Custom deserialization: read non-tree fields, then rebuild the AVL tree by reading entries
     * and inserting them via put(...)
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int n = in.readInt();
        readEntries(in, n);
    }
}