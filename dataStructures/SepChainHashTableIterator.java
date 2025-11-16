package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * Iterator for SepChainHashTable
 * Iterates over all elements in the hash table (all buckets).
 *
 * @param <K> Key type
 * @param <V> Value type
 */
class SepChainHashTableIterator<K,V> implements Iterator<Map.Entry<K,V>> {

    /** Array of buckets (each bucket is a Map) */
    private final Map<K,V>[] table;

    /** Current index in the array of buckets */
    private int currentIndex;

    /** Iterator of the current bucket */
    private Iterator<Map.Entry<K,V>> currentIterator;

    /**
     * Constructor: initializes the iterator pointing to the first non-empty bucket
     *
     * Time complexity: O(1)
     * @param table array of buckets
     */
    public SepChainHashTableIterator(Map<K,V>[] table) {
        this.table = table;
        this.currentIndex = -1;
        this.currentIterator = null;
        advanceToNextBucket();
    }

    /**
     * Advances to the next non-empty bucket
     *
     * Time complexity: O(n) in worst case, O(1) on average
     */
    private void advanceToNextBucket() {
        currentIterator = null;
        currentIndex++;
        while (currentIndex < table.length) {
            Map<K,V> bucket = table[currentIndex];
            if (bucket != null && !bucket.isEmpty()) {
                currentIterator = bucket.iterator();
                break;
            }
            currentIndex++;
        }
    }

    /**
     * Returns true if there is a next element
     *
     * Time complexity: O(1) on average
     * @return true if the iterator has more elements
     */
    public boolean hasNext() {
        if (currentIterator != null && currentIterator.hasNext()) return true;

        int tempIndex = currentIndex + 1;
        while (tempIndex < table.length) {
            Map<K,V> bucket = table[tempIndex];
            if (bucket != null && !bucket.isEmpty()) return true;
            tempIndex++;
        }
        return false;
    }

    /**
     * Returns the next element in the iterator
     *
     * Time complexity: O(1) on average
     * @return next element in the iterator
     * @throws NoSuchElementException if no element exists
     */
    public Map.Entry<K,V> next() {
        if (currentIterator == null || !currentIterator.hasNext()) {
            advanceToNextBucket();
            if (currentIterator == null || !currentIterator.hasNext())
                throw new NoSuchElementException();
        }
        return currentIterator.next();
    }

    /**
     * Restarts the iteration
     *
     * Time complexity: O(1)
     */
    public void rewind() {
        currentIndex = -1;
        currentIterator = null;
        advanceToNextBucket();
    }
}
