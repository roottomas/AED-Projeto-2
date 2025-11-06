package dataStructures;
import dataStructures.exceptions.*;
/**
 * Closed Hash Table
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class ClosedHashTable<K,V> extends HashTable<K,V> {

    //Load factors
    static final float IDEAL_LOAD_FACTOR =0.5f;
    static final float MAX_LOAD_FACTOR =0.8f;
    static final int NOT_FOUND=-1;

    // removed cell
    static final Entry<?,?> REMOVED_CELL = new Entry<>(null,null);

    // The array of entries.
    private Entry<K,V>[] table;

    /**
     * Constructors
     */

    public ClosedHashTable( ){
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ClosedHashTable( int capacity ){
        super(capacity);
        int arraySize = HashTable.nextPrime((int) (capacity / IDEAL_LOAD_FACTOR));
        // Compiler gives a warning.
        table = (Entry<K,V>[]) new Entry[arraySize];
        for ( int i = 0; i < arraySize; i++ )
            table[i] = null;
        maxSize = (int)(arraySize * MAX_LOAD_FACTOR);
    }

    //Methods for handling collisions.
    // Returns the hash value of the specified key.
    int hash( K key, int i ){
        return Math.abs( key.hashCode() + i) % table.length;
    }
    /**
     * Linear Proving
     * @param key to search
     * @return the index of the table, where is the entry with the specified key, or null
     */
    int searchLinearProving(K key) {
        for (int i = 0; i < table.length; i++) {
            int idx = hash(key, i);
            Entry<K,V> e = table[idx];
            if (e == null) {
                return NOT_FOUND;
            }
            if (e == REMOVED_CELL) {
                continue;
            }
            if (e.key().equals(key)) return idx;
        }
        return NOT_FOUND;
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
        int idx = searchLinearProving(key);
        if (idx == NOT_FOUND) return null;
        return table[idx].value();
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
        if (isFull())
            rehash();
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

     private void rehash(){
         int oldLen = table.length;
         int proposed = Math.max(3, oldLen * 2);
         int newLen = HashTable.nextPrime(proposed);
         if (newLen == 0) newLen = proposed; // fallback
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
         maxSize = (int)(table.length * MAX_LOAD_FACTOR);
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
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new ClosedHashTableIterator();
    }

    private class ClosedHashTableIterator implements Iterator<Entry<K,V>> {
        private int index;
        public ClosedHashTableIterator() { rewind(); }

        @Override
        public boolean hasNext() {
            return index < table.length;
        }

        @Override
        public Entry<K,V> next() {
            while (index < table.length) {
                Entry<K,V> e = table[index++];
                if (e != null && e != REMOVED_CELL) return e;
            }
            throw new NoSuchElementException();
        }

        @Override
        public void rewind() {
            index = 0;
            while (index < table.length && (table[index] == null || table[index] == REMOVED_CELL)) index++;
        }
    }
}