package dataStructures;

import dataStructures.exceptions.NoSuchElementException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serial;

/**
 * Map implemented with a singly linked list.
 * Each node stores an Entry<K,V> (key, value).
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class MapSinglyList<K,V> implements Map<K, V> {

    /** Head of the singly linked list */
    private transient SinglyListNode<Entry<K,V>> head;

    /** Number of elements in the map */
    private transient int size;

    /**
     * Empty constructor.
     * Initializes an empty list.
     *
     * Time complexity: O(1)
     */
    public MapSinglyList() {
        head = null;
        size = 0;
    }

    /**
     * Returns true if the map is empty.
     *
     * Time complexity: O(1)
     * @return true if map has no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of entries in the map.
     *
     * Time complexity: O(1)
     * @return number of elements
     */
    public int size() {
        return size;
    }

    /**
     * Returns the value associated with the specified key.
     * Returns null if the key does not exist or is null.
     *
     * Time complexity: O(n) worst case
     * @param key key to search
     * @return value associated with key or null
     */
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
     * Inserts or updates a (key, value) pair in the map.
     * If the key exists, replaces the value and returns the old value.
     * If not, inserts at the head and returns null.
     *
     * Time complexity:
     * - Update: O(n)
     * - Insertion: O(1)
     * @param key key
     * @param value value
     * @return old value if key existed, null otherwise
     */
    public V put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

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

        // Insert at head
        head = new SinglyListNode<>(new Entry<>(key, value), head);
        size++;
        return null;
    }

    /**
     * Removes the entry with the specified key.
     * Returns the removed value or null if not found.
     *
     * Time complexity: O(n)
     * @param key key to remove
     * @return removed value or null
     */
    public V remove(K key) {
        if (key == null) return null;

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
     * Returns an iterator over the map entries.
     *
     * Time complexity: O(1) to create, O(n) to iterate all elements
     * @return iterator over Entry<K,V>
     */
    public Iterator<Entry<K, V>> iterator() {
        return new SinglyIterator<>(head);
    }

    /**
     * Returns an iterator over the map values.
     *
     * Time complexity: O(1) to create, O(n) to iterate all elements
     * @return iterator over V
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<V> values() {
        return (Iterator<V>) new ValuesIterator(iterator());
    }

    /**
     * Returns an iterator over the map keys.
     *
     * Time complexity: O(1) to create, O(n) to iterate all elements
     * @return iterator over K
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<K> keys() {
        return (Iterator<K>) new KeysIterator(iterator());
    }

    /**
     * Custom serialization to preserve the linked list.
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        SinglyListNode<Entry<K,V>> savedHead = head;
        int savedSize = size;

        try {
            head = null;
            size = 0;
            out.defaultWriteObject();

            out.writeInt(savedSize);
            SinglyListNode<Entry<K,V>> curr = savedHead;
            while (curr != null) {
                Entry<K,V> e = curr.getElement();
                out.writeObject(e.key());
                out.writeObject(e.value());
                curr = curr.getNext();
            }
        } finally {
            head = savedHead;
            size = savedSize;
        }
    }

    /**
     * Custom deserialization to reconstruct the linked list.
     */
    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

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
