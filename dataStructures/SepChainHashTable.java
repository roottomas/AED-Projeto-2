package dataStructures;
/**
 * SepChain Hash Table
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class SepChainHashTable<K,V> extends HashTable<K,V> {

    //Load factors
    static final float IDEAL_LOAD_FACTOR =0.75f;
    static final float MAX_LOAD_FACTOR =0.9f;

    // The array of Map with singly linked list.
    private Map<K,V>[] table;

    public SepChainHashTable( ){
        super(DEFAULT_CAPACITY);
    }
    
    public SepChainHashTable( int capacity ){
        super(capacity);
        table = makeTable((int) (capacity * IDEAL_LOAD_FACTOR));
    }

    @SuppressWarnings("unchecked")
    private static <K,V> Map<K,V>[] makeTable(int length) {
        return (Map<K,V>[]) java.lang.reflect.Array.newInstance(Map.class, length);
    }

    // Returns the hash value of the specified key.
    protected int hash( K key ){
        return Math.abs( key.hashCode() ) % table.length;
    }
    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * returns its value; otherwise, returns null.
     *
     * @param key whose associated value is to be returned
     * @return value of entry in the dictionary whose key is the specified key,
     * or null if the dictionary does not have an entry with that key
     */
    public V get(K key) {
        int idx = hash(key);
        Map<K,V> bucket = table[idx];
        if (bucket == null) return null;
        return bucket.get(key);
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
        if (isFull())
            rehash();

        int idx = hash(key);
        Map<K,V> bucket = table[idx];
        if (bucket == null) {
            bucket = new MapSinglyList<>();
            table[idx] = bucket;
        }

        V old = bucket.put(key, value);
        if (old == null) {
            currentSize++;
        }
        return old;
    }


    private void rehash() {
        int oldCapacity = table.length;
        int proposed = oldCapacity <= 1 ? 3 : oldCapacity * 2;
        int newCapacity = HashTable.nextPrime(proposed);
        if (newCapacity == 0) {
            newCapacity = proposed;
        }

        Map<K,V>[] newTable = makeTable(newCapacity);

        for (Map<K, V> bucket : table) {
            if (bucket == null) continue;
            Iterator<Entry<K, V>> it = bucket.iterator();
            while (it.hasNext()) {
                Entry<K, V> e = it.next();
                int idx = Math.abs(e.key().hashCode()) % newTable.length;
                Map<K, V> nb = newTable[idx];
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
     * If there is an entry in the dictionary whose key is the specified key,
     * removes it from the dictionary and returns its value;
     * otherwise, returns null.
     *
     * @param key whose entry is to be removed from the map
     * @return previous value associated with key,
     * or null if the dictionary does not an entry with that key
     */
    public V remove(K key) {
        int idx = hash(key);
        Map<K,V> bucket = table[idx];
        if (bucket == null) return null;
        V old = bucket.remove(key);
        if (old != null) currentSize--;
        return old;
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    public Iterator<Entry<K, V>> iterator() {
        return new SepChainHashTableIterator<>(table);
    }


}
