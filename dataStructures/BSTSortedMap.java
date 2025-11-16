package dataStructures;

import dataStructures.exceptions.EmptyMapException;

/**
 * Binary Search Tree Sorted Map
 * @author AED Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class BSTSortedMap<K extends Comparable<K>, V> extends BTree<Map.Entry<K, V>> implements SortedMap<K, V> {

    /**
     * Constructor
     */
    public BSTSortedMap() {
        super();
    }

    /**
     * Returns the entry with the smallest key in the dictionary.
     * @throws EmptyMapException if the tree is empty
     * Time complexity: O(log n)
     * @return Entry with the smallest key
     */
    @Override
    public Entry<K, V> minEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherLeftElement().getElement();
    }

    /**
     * Returns the entry with the largest key in the dictionary.
     * @throws EmptyMapException if the tree is empty
     * Time complexity: O(log n)
     * @return Entry with the largest key
     */
    @Override
    public Entry<K, V> maxEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherRightElement().getElement();
    }

    /**
     * Returns the value associated with the specified key, or null if not present.
     * @param key whose associated value is to be returned
     * Time complexity: O(log n)
     * @return value of entry in the dictionary whose key is the specified key, or null if not found
     */
    @Override
    public V get(K key) {
        BTNode<Map.Entry<K, V>> node = getNode((BTNode<Map.Entry<K, V>>) root, key);
        return node == null ? null : node.getElement().value();
    }

    private BTNode<Map.Entry<K, V>> getNode(BTNode<Map.Entry<K, V>> node, K key) {
        while (node != null) {
            int cmp = key.compareTo(node.getElement().key());
            if (cmp == 0) return node;
            node = (cmp < 0) ? (BTNode<Map.Entry<K, V>>) node.getLeftChild()
                    : (BTNode<Map.Entry<K, V>>) node.getRightChild();
        }
        return null;
    }

    /**
     * Create a new node for the BST.
     * @param entry Entry to store
     * @param parent Parent of the new node
     * Time complexity: O(1)
     * @return the newly created node
     */
    protected BTNode<Entry<K, V>> createNode(Entry<K, V> entry, BTNode<Entry<K, V>> parent) {
        return new BTNode<>(entry, parent);
    }

    /**
     * Inserts a key-value pair or updates the value if key exists.
     * @param key with which the specified value is to be associated
     * @param value to be associated with the specified key
     * Time complexity: O(log n)
     * @return previous value associated with key, or null if not present
     */
    @Override
    public V put(K key, V value) {
        Map.Entry<K, V> newEntry = new Map.Entry<>(key, value);

        if (isEmpty()) {
            root = createNode(newEntry, null);
            return null;
        }

        BTNode<Entry<K, V>> parent = null;
        BTNode<Entry<K, V>> curr = (BTNode<Entry<K, V>>) root;
        int cmp = 0;

        while (curr != null) {
            parent = curr;
            K currKey = curr.getElement().key();
            cmp = key.compareTo(currKey);
            if (cmp == 0) {
                // key exists -> replace value and return old
                V old = curr.getElement().value();
                curr.setElement(new Map.Entry<>(key, value));
                return old;
            } else if (cmp < 0) {
                curr = (BTNode<Entry<K, V>>) curr.getLeftChild();
            } else {
                curr = (BTNode<Entry<K, V>>) curr.getRightChild();
            }
        }

        // insert as child of parent
        BTNode<Entry<K, V>> newNode = createNode(newEntry, parent);
        if (cmp < 0) parent.setLeftChild(newNode);
        else parent.setRightChild(newNode);

        return null;
    }

    /**
     * Removes the entry associated with the specified key.
     * @param key whose entry is to be removed from the map
     * Time complexity: O(log n)
     * @return previous value associated with key, or null if not found
     */
    @Override
    public V remove(K key) {
        BTNode<Entry<K, V>> node = getNode((BTNode<Entry<K, V>>) root, key);
        if (node == null) return null;
        V old = node.getElement().value();

        // no left child
        if (node.getLeftChild() == null) {
            transplant(node, node.getRightChild());
        }
        // no right child
        else if (node.getRightChild() == null) {
            transplant(node, node.getLeftChild());
        }
        // two children
        else {
            BTNode<Entry<K, V>> succ = furtherLeftElement((BTNode<Entry<K, V>>) node.getRightChild());
            if (succ.getParent() != node) {
                transplant(succ, succ.getRightChild());
                succ.setRightChild(node.getRightChild());
                ((BTNode<Entry<K, V>>) succ.getRightChild()).setParent(succ);
            }
            transplant(node, succ);
            succ.setLeftChild(node.getLeftChild());
            ((BTNode<Entry<K, V>>) succ.getLeftChild()).setParent(succ);
        }

        return old;
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     * Time complexity: O(1) to create, O(n) to iterate fully
     * @return iterator of the entries in the dictionary
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new InOrderIterator<>((BTNode<Entry<K, V>>) root);
    }

    /**
     * Returns an iterator of the values in the dictionary.
     * Time complexity: O(1) to create, O(n) to iterate fully
     * @return iterator of the values in the dictionary
     */
    @Override
    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<V> values() {
        return new ValuesIterator(iterator());
    }

    /**
     * Returns an iterator of the keys in the dictionary.
     * Time complexity: O(1) to create, O(n) to iterate fully
     * @return iterator of the keys in the dictionary
     */
    @Override
    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<K> keys() {
        return new KeysIterator(iterator());
    }
}
