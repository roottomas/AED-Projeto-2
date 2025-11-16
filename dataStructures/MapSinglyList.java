package dataStructures;

import dataStructures.exceptions.NoSuchElementException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serial;

/**
 * Map with a singly linked list with head and size
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
class MapSinglyList<K,V> implements Map<K, V> {


    private transient SinglyListNode<Entry<K,V>> head;

    private transient int size;

    public MapSinglyList() {
        head = null;
        size = 0;
    }

    /**
     * Returns true iff the dictionary contains no entries.
     *
     * @return true if dictionary is empty
     */

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of entries in the dictionary.
     *
     * @return number of elements in the dictionary
     */
    @Override
    public int size() {
        return size;
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
        if (key == null) return null;
        SinglyListNode<Entry<K,V>> curr = head;
        while (curr != null) {
            Entry<K,V> e = curr.getElement();
            if (e.key().equals(key)) return e.value();
            curr = curr.getNext();
        }
        return null;
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

    public V put(K key, V value) {
        SinglyListNode<Entry<K,V>> curr = head;
        while (curr != null) {
            Entry<K,V> e = curr.getElement();
            if (e.key().equals(key)) {
                V old = e.value();
                curr.setElement(new Entry<>(key, value));
                return old;
            }
            curr = curr.getNext();
        }
        head = new SinglyListNode<>(new Entry<>(key, value), head);
        size++;
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
    public V remove(K key) throws NoSuchElementException {
        if (key == null) throw new NoSuchElementException();
        SinglyListNode<Entry<K,V>> curr = head;
        SinglyListNode<Entry<K,V>> prev = null;
        while (curr != null) {
            Entry<K,V> e = curr.getElement();
            if (e.key().equals(key)) {
                V old = e.value();
                if (prev == null) {
                    head = curr.getNext();
                } else {
                    prev.setNext(curr.getNext());
                }
                size--;
                return old;
            }
            prev = curr;
            curr = curr.getNext();
        }
        return null;
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    public Iterator<Entry<K, V>> iterator() {
        return new SinglyIterator<>(head);
    }

    /**
     * Returns an iterator of the values in the dictionary.
     *
     * @return iterator of the values in the dictionary
     */

    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<V> values() {
        return (Iterator<V>) new ValuesIterator(iterator());
    }

    /**
     * Returns an iterator of the keys in the dictionary.
     *
     * @return iterator of the keys in the dictionary
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<K> keys() {
        return (Iterator<K>) new KeysIterator(iterator());
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        // save current state
        SinglyListNode<Entry<K,V>> savedHead = head;
        int savedSize = size;

        try {
            // avoid serializing the recursive node graph
            head = null;
            size = 0;
            out.defaultWriteObject();

            // write logical contents explicitly (preserve order from head -> tail)
            out.writeInt(savedSize);
            SinglyListNode<Entry<K,V>> curr = savedHead;
            while (curr != null) {
                Entry<K,V> e = curr.getElement();
                out.writeObject(e.key());
                out.writeObject(e.value());
                curr = curr.getNext();
            }
        } finally {
            // restore runtime state
            head = savedHead;
            size = savedSize;
        }
    }

    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        // read number of entries and rebuild the singly list preserving original order
        int n = in.readInt();
        head = null;
        SinglyListNode<Entry<K,V>> tail = null;
        size = 0;

        for (int i = 0; i < n; i++) {
            K key = (K) in.readObject();
            V value = (V) in.readObject();
            Entry<K,V> entry = new Entry<>(key, value);
            SinglyListNode<Entry<K,V>> node = new SinglyListNode<>(entry, null);
            if (head == null) {
                head = tail = node;
            } else {
                tail.setNext(node);
                tail = node;
            }
            size++;
        }
    }
}
