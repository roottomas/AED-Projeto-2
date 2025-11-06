package dataStructures;

import dataStructures.exceptions.EmptyMapException;
/**
 * Binary Search Tree Sorted Map
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class BSTSortedMap<K extends Comparable<K>,V> extends BTree<Map.Entry<K,V>> implements SortedMap<K,V>{

    /**
     * Constructor
     */
    public BSTSortedMap(){
        super();
    }
    /**
     * Returns the entry with the smallest key in the dictionary.
     *
     * @return
     * @throws EmptyMapException
     */
    @Override
    public Entry<K, V> minEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherLeftElement().getElement();
    }

    /**
     * Returns the entry with the largest key in the dictionary.
     *
     * @return
     * @throws EmptyMapException
     */
    @Override
    public Entry<K, V> maxEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherRightElement().getElement();
    }


    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * returns its value; otherwise, returns null.
     *
     * @param key whose associated value is to be returned
     * @return value of entry in the dictionary whose key is the specified key,
     * or null if the dictionary does not have an entry with that key
     */
    @Override
    public V get(K key) {
        Node<Entry<K,V>> node=getNode((BTNode<Entry<K,V>>)root,key);
        if (node!=null)
            return node.getElement().value();
        return null;
    }

    private BTNode<Entry<K,V>> getNode(BTNode<Entry<K,V>> node, K key) {
        if (node == null) return null;
        K nodeKey = node.getElement().key();
        int cmp = key.compareTo(nodeKey);
        if (cmp == 0) return node;
        if (cmp < 0)
            return getNode((BTNode<Entry<K,V>>) node.getLeftChild(), key);
        else
            return getNode((BTNode<Entry<K,V>>) node.getRightChild(), key);
    }

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * replaces its value by the specified value and returns the old value;
     * otherwise, inserts the entry (key, value) and returns null.
     *
     * @param key   with which the specified value is to be associated
     * @param value to be associated with the specified key
     * @return previous value associated with key,
     * or null if the dictionary does not have an entry with that key
     */
    @Override
    public V put(K key, V value) {
        Map.Entry<K,V> newEntry = new Map.Entry<>(key, value);

        if (isEmpty()) {
            root = new BTNode<>(newEntry);
            return null;
        }

        BTNode<Entry<K,V>> parent = null;
        BTNode<Entry<K,V>> curr = (BTNode<Entry<K,V>>) root;
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
                curr = (BTNode<Entry<K,V>>) curr.getLeftChild();
            } else {
                curr = (BTNode<Entry<K,V>>) curr.getRightChild();
            }
        }

        // insert as child of parent
        BTNode<Entry<K,V>> newNode = new BTNode<>(newEntry, parent);
        if (cmp < 0) {
            parent.setLeftChild(newNode);
        } else {
            parent.setRightChild(newNode);
        }
        return null;
    }


    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * removes it from the dictionary and returns its value;
     * otherwise, returns null.
     *
     * @param key whose entry is to be removed from the map
     * @return previous value associated with key,
     * or null if the dictionary does not an entry with that key
     */
    @Override
    public V remove(K key) {
        BTNode<Entry<K,V>> node = getNode((BTNode<Entry<K,V>>) root, key);
        if (node == null) return null;

        V oldValue = node.getElement().value();

        // case 1: node is leaf
        if (node.isLeaf()) {
            if (node.isRoot()) {
                root = null;
            } else {
                BTNode<Entry<K, V>> parent = (BTNode<Entry<K, V>>) node.getParent();
                if (parent.getLeftChild() == node) parent.setLeftChild(null);
                else parent.setRightChild(null);
            }
            return oldValue;
        }

        // case 2: node has only one child
        Node<Entry<K,V>> left = node.getLeftChild();
        Node<Entry<K,V>> right = node.getRightChild();
        if (left == null || right == null) {
            Node<Entry<K,V>> child = (left != null) ? left : right;
            if (node.isRoot()) {
                root = child;
                ((BTNode<Entry<K,V>>) child).setParent(null);
            } else {
                BTNode<Entry<K,V>> parent = (BTNode<Entry<K,V>>) node.getParent();
                if (parent.getLeftChild() == node) parent.setLeftChild(child);
                else parent.setRightChild(child);
            }
            return oldValue;
        }

        // case 3: node has two children -> find successor (leftmost of right subtree)
        BTNode<Entry<K,V>> successor = ((BTNode<Entry<K,V>>) node.getRightChild()).furtherLeftElement();
        // copy successor's entry to current node
        node.setElement(successor.getElement());

        // now remove successor (successor has at most right child)
        BTNode<Entry<K,V>> succParent = (BTNode<Entry<K,V>>) successor.getParent();
        Node<Entry<K,V>> succRight = successor.getRightChild();

        if (succParent.getLeftChild() == successor) {
            succParent.setLeftChild(succRight);
        } else {
            succParent.setRightChild(succRight);
        }
        if (succRight != null) {
            ((BTNode<Entry<K,V>>) succRight).setParent(succParent);
        }
        return oldValue;
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new InOrderIterator((BTNode<Entry<K,V>>) root);
    }

    /**
     * Returns an iterator of the values in the dictionary.
     *
     * @return iterator of the values in the dictionary
     */
    @Override
    public Iterator<V> values() {
        return new ValuesIterator(iterator());
    }

    /**
     * Returns an iterator of the keys in the dictionary.
     *
     * @return iterator of the keys in the dictionary
     */
    @Override
    public Iterator<K> keys() {
        return new KeysIterator(iterator());
    }
}