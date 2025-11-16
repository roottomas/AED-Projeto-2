package dataStructures;

import dataStructures.exceptions.*;

/**
 * Hash table with separate chaining collision resolution.
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class SepChainHashTable<K,V> extends HashTable<K,V> {

    // Load factors
    static final float IDEAL_LOAD_FACTOR = 0.75f;
    static final float MAX_LOAD_FACTOR = 0.9f;

    /** Array of buckets, each bucket is a Map based on a list */
    private Map<K,V>[] table;

    /** Default constructor */
    public SepChainHashTable() {
        super(DEFAULT_CAPACITY);
        table = makeTable((int) (DEFAULT_CAPACITY * IDEAL_LOAD_FACTOR));
    }

    /** Constructor with initial capacity */
    public SepChainHashTable(int capacity) {
        super(capacity);
        table = makeTable((int) (capacity * IDEAL_LOAD_FACTOR));
    }

    /** Creates a generic Map array */
    @SuppressWarnings("unchecked")
    private static <K,V> Map<K,V>[] makeTable(int length) {
        return (Map<K,V>[]) java.lang.reflect.Array.newInstance(Map.class, length);
    }

    /** Returns the hash index for a key */
    protected int hash(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    /**
     * Returns the value associated with the key, or null if it does not exist.
     *
     * Time complexity (average): O(1 + α), where α is the load factor
     * @return value associated with the key or null
     */
    public V get(K key) {
        if (key == null) throw new NoSuchElementException();
        int idx = hash(key);
        Map<K,V> bucket = table[idx];
        if (bucket == null) return null;
        return bucket.get(key);
    }

    /**
     * Inserts or updates the key with the given value.
     *
     * Time complexity (average): O(1 + α)
     * @param key the key to insert/update
     * @param value the value to associate with the key
     * @return previous value associated with the key, or null if none
     */
    public V put(K key, V value) {
        if (isFull()) rehash();

        int idx = hash(key);
        Map<K,V> bucket = table[idx];
        if (bucket == null) {
            bucket = new MapSinglyList<>();
            table[idx] = bucket;
        }

        V old = bucket.put(key, value);
        if (old == null) currentSize++;
        return old;
    }

    /** Rehashes the table when the maximum load factor is reached */
    private void rehash() {
        int oldCapacity = (table == null ? 0 : table.length);
        int proposed = oldCapacity <= 1 ? 3 : oldCapacity * 2;
        int newCapacity = HashTable.nextPrime(proposed);
        if (newCapacity == 0) newCapacity = proposed;

        @SuppressWarnings("unchecked")
        Map<K,V>[] newTable = makeTable(newCapacity);

        for (Map<K, V> bucket : table) {
            if (bucket == null) continue;
            Iterator<Map.Entry<K, V>> it = bucket.iterator();
            while (it.hasNext()) {
                Map.Entry<K, V> e = it.next();
                int idx = Math.abs(e.key().hashCode()) % newTable.length;
                Map<K,V> nb = newTable[idx];
                if (nb == null) {
                    nb = new MapSinglyList<>();
                    newTable[idx] = nb;
                }
                nb.put(e.key(), e.value());
            }
        }

        table = newTable;
        this.maxSize = (int) (table.length * MAX_LOAD_FACTOR);
    }

    /**
     * Removes the value associated with the key.
     *
     * Time complexity (average): O(1 + α)
     * @param key key to remove
     * @return value previously associated with the key, or null if none
     */
    public V remove(K key) {
        if (key == null) throw new NoSuchElementException();
        int idx = hash(key);
        Map<K,V> bucket = table[idx];
        if (bucket == null) return null;
        V old = bucket.remove(key);
        if (old != null) currentSize--;
        return old;
    }

    /**
     * Returns an iterator over all (key, value) pairs in the table.
     *
     * Time complexity: O(n), n = number of elements
     * @return iterator over all entries
     */
    public Iterator<Entry<K, V>> iterator() {
        return new SepChainHashTableIterator<>(table);
    }
}
