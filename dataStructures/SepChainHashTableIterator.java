package dataStructures;
/**
 * SepChain Hash Table Iterator
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
import dataStructures.exceptions.NoSuchElementException;

class SepChainHashTableIterator<K,V> implements Iterator<Map.Entry<K,V>> {

    // The array of bucket maps (each bucket is a MapSinglyList)
    private final Map<K,V>[] table;

    // Current position in the table
    private int currentIndex;

    // Iterator for the current bucket
    private Iterator<Map.Entry<K,V>> currentIterator;

    public SepChainHashTableIterator(Map<K,V>[] table) {
        this.table = table;
        this.currentIndex = -1;
        this.currentIterator = null;
        advanceToNextBucket();
    }

    /**
     * Move to the next non-empty bucket.
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
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @return true iff the iteration has more elements
     */
    public boolean hasNext() {
        if (currentIterator.hasNext()) return true;

        int tempIndex = currentIndex + 1;
        while (tempIndex < table.length) {
            Map<K,V> bucket = table[tempIndex];
            if (bucket != null && !bucket.isEmpty()) return true;
            tempIndex++;
        }
        return false;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public Map.Entry<K,V> next() {
        if (!currentIterator.hasNext()) {
            advanceToNextBucket();
            if (!currentIterator.hasNext())
                throw new NoSuchElementException();
        }
        return currentIterator.next();
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    public void rewind() {
        currentIndex = -1;
        currentIterator = null;
        advanceToNextBucket();
    }
}

