package dataStructures;

import dataStructures.exceptions.*;

/**
 * Closed Hash Table (Open Addressing)
 *
 * @author AED Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class ClosedHashTable<K,V> extends HashTable<K,V> {

    static final float IDEAL_LOAD_FACTOR = 0.5f;
    static final float MAX_LOAD_FACTOR = 0.8f;
    static final int NOT_FOUND = -1;
    static final Entry<?,?> REMOVED_CELL = new Entry<>(null, null);

    /** Array of entries */
    private Entry<K,V>[] table;

    /**
     * Default constructor
     * Time complexity: O(n)
     */
    public ClosedHashTable() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor with capacity
     *
     * @param capacity initial capacity
     * Time complexity: O(n)
     */
    @SuppressWarnings("unchecked")
    public ClosedHashTable(int capacity) {
        super(capacity);
        int arraySize = HashTable.nextPrime((int) (capacity / IDEAL_LOAD_FACTOR));
        table = (Entry<K,V>[]) new Entry[arraySize];
        for (int i = 0; i < arraySize; i++)
            table[i] = null;
        maxSize = (int) (arraySize * MAX_LOAD_FACTOR);
    }

    /**
     * Computes the hash for a given key and probing step
     *
     * @param key key to hash
     * @param i probing step
     * @return hash position
     * Time complexity: O(1)
     */
    int hash(K key, int i) {
        return Math.abs(key.hashCode() + i) % table.length;
    }

    /**
     * Searches for a key using linear probing
     *
     * @param key key to search
     * @return index in table or NOT_FOUND
     * Time complexity: O(n)
     */
    int searchLinearProving(K key) {
        for (int i = 0; i < table.length; i++) {
            int idx = hash(key, i);
            Entry<K,V> e = table[idx];
            if (e == null) return NOT_FOUND;
            if (e == REMOVED_CELL) continue;
            if (e.key().equals(key)) return idx;
        }
        return NOT_FOUND;
    }

    /**
     * Returns the value associated with a key, or null if not found
     *
     * @param key key to search
     * @return value associated with key or null
     * Time complexity: O(n)
     */
    @Override
    public V get(K key) {
        int idx = searchLinearProving(key);
        if (idx == NOT_FOUND) return null;
        return table[idx].value();
    }

    /**
     * Inserts or updates a key-value pair
     *
     * @param key key to insert/update
     * @param value value to associate
     * @return old value if replaced, or null
     * Time complexity: O(n)
     */
    @Override
    public V put(K key, V value) {
        if (isFull()) rehash();

        int firstRemoved = NOT_FOUND;
        for (int i = 0; i < table.length; i++) {
            int idx = hash(key, i);
            Entry<K,V> e = table[idx];

            if (e == null) {
                int insertIdx = (firstRemoved != NOT_FOUND) ? firstRemoved : idx;
                table[insertIdx] = new Entry<>(key, value);
                currentSize++;
                return null;
            }
            if (e == REMOVED_CELL) {
                if (firstRemoved == NOT_FOUND) firstRemoved = idx;
                continue;
            }
            if (e.key().equals(key)) {
                V old = e.value();
                table[idx] = new Entry<>(key, value);
                return old;
            }
        }

        if (firstRemoved != NOT_FOUND) {
            table[firstRemoved] = new Entry<>(key, value);
            currentSize++;
            return null;
        }

        return put(key, value);
    }

    /**
     * Rehashes the table by expanding and reinserting all entries
     * Time complexity: O(n)
     */
    private void rehash() {
        int oldLen = table.length;
        int proposed = Math.max(3, oldLen * 2);
        int newLen = HashTable.nextPrime(proposed);
        if (newLen == 0) newLen = proposed;

        @SuppressWarnings("unchecked")
        Entry<K,V>[] newTable = (Entry<K,V>[]) new Entry[newLen];

        for (Entry<K, V> e : table) {
            if (e == null || e == REMOVED_CELL) continue;
            K key = e.key();
            for (int j = 0; j < newTable.length; j++) {
                int idx = Math.abs(key.hashCode() + j) % newTable.length;
                if (newTable[idx] == null) {
                    newTable[idx] = new Entry<>(key, e.value());
                    break;
                }
            }
        }

        table = newTable;
        maxSize = (int) (table.length * MAX_LOAD_FACTOR);
    }

    /**
     * Removes the entry with the specified key
     *
     * @param key key to remove
     * @return old value associated with key, or null
     * Time complexity: O(n)
     */
    @Override
    public V remove(K key) {
        int idx = searchLinearProving(key);
        if (idx == NOT_FOUND) return null;

        V old = table[idx].value();
        @SuppressWarnings("unchecked")
        Entry<K,V> removedMarker = (Entry<K,V>) REMOVED_CELL;
        table[idx] = removedMarker;
        currentSize--;
        return old;
    }

    /**
     * Returns an iterator over the entries
     *
     * @return iterator of entries
     * Time complexity: O(1)
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new ClosedHashTableIterator();
    }

    /**
     * Iterator for ClosedHashTable
     */
    private class ClosedHashTableIterator implements Iterator<Entry<K,V>> {

        private int index;

        /**
         * Constructor: rewinds to first valid entry
         * Time complexity: O(n)
         */
        public ClosedHashTableIterator() {
            rewind();
        }

        /**
         * Returns true if there is a next entry
         *
         * @return true if more elements exist
         * Time complexity: O(1)
         */
        @Override
        public boolean hasNext() {
            return index < table.length;
        }

        /**
         * Returns the next valid entry
         *
         * @return next entry
         * @throws NoSuchElementException if none available
         * Time complexity: O(1)
         */
        @Override
        public Entry<K,V> next() {
            while (index < table.length) {
                Entry<K,V> e = table[index++];
                if (e != null && e != REMOVED_CELL)
                    return e;
            }
            throw new NoSuchElementException();
        }

        /**
         * Rewinds the iterator to the first valid entry
         * Time complexity: O(n)
         */
        @Override
        public void rewind() {
            index = 0;
            while (index < table.length && (table[index] == null || table[index] == REMOVED_CELL))
                index++;
        }
    }
}
