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

    protected BTNode<Entry<K,V>> createNode(Entry<K,V> entry, BTNode<Entry<K,V>> parent) {
        return new BTNode<>(entry, parent);
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
            root = createNode(newEntry, null);
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
        BTNode<Entry<K,V>> newNode = createNode(newEntry, null);
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
        V old = node.getElement().value();

        // case: no left child => replace node with right child
        if (node.getLeftChild() == null) {
            transplant(node, node.getRightChild());
        }
        // case: no right child => replace node with left child
        else if (node.getRightChild() == null) {
            transplant(node, node.getLeftChild());
        } else {
            // two children: get successor (leftmost of right subtree)
            BTNode<Entry<K,V>> succ = furtherLeftElement((BTNode<Entry<K,V>>) node.getRightChild());
            if (succ.getParent() != node) {
                transplant(succ, succ.getRightChild());
                succ.setRightChild(node.getRightChild());
                ((BTNode<Entry<K,V>>) succ.getRightChild()).setParent(succ);
            }
            transplant(node, succ);
            succ.setLeftChild(node.getLeftChild());
            ((BTNode<Entry<K,V>>) succ.getLeftChild()).setParent(succ);
        }
        return old;
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