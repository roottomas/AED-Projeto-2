package dataStructures;

/**
 * Abstract Hash Table.
 * @author AED Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
abstract class HashTable<K,V> implements Map<K,V> {

    /** Default size of the hash table */
    static final int DEFAULT_CAPACITY = 50;

    /** Number of entries in the hash table */
    protected int currentSize;

    /** Predicted number of elements */
    private int size;

    /** Maximum number of entries before reaching MAX_LOAD_FACTOR */
    protected int maxSize;

    public HashTable(int capacity) {
        currentSize = 0;
        size = capacity;
    }

    // Public Static Methods

    /**
     * Returns the hash code of the specified string key.
     *
     * Time complexity: O(n) where n is key.length()
     * @param key key to hash
     * @return hash code of key
     */
    public static int hash(String key) {
        int a = 127; // a is a prime number
        int b = 2147483647; // b is a prime number
        int hashCode = 0;

        for (int i = 0; i < key.length(); i++)
            hashCode = (hashCode * a + key.charAt(i)) % b;
        return hashCode;
    }

    // Protected Static Methods

    /**
     * Returns a prime number not less than the specified number,
     * or zero if none exists.
     *
     * Time complexity: O(sqrt(n)) per candidate number
     * @param number lower bound for the prime search
     * @return next prime not less than number, or 0 if none exists
     */
    protected static int nextPrime(int number) {
        if (number <= 2) return 2;

        long candidate = number;
        if (candidate % 2 == 0) candidate++;

        for (; candidate <= Integer.MAX_VALUE; candidate += 2) {
            if (isPrime(candidate)) return (int) candidate;
            if (candidate >= Integer.MAX_VALUE - 2) break;
        }

        if (Integer.MAX_VALUE >= number && isPrime((long) Integer.MAX_VALUE)) {
            return Integer.MAX_VALUE;
        }

        return 0;
    }

    /**
     * Determines whether a number is prime.
     *
     * Time complexity: O(sqrt(n))
     * @param n number to test
     * @return true iff n is prime
     */
    private static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;

        long limit = (long) Math.sqrt(n);
        for (long i = 3; i <= limit; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /** Returns true iff the hash table cannot contain more entries */
    protected boolean isFull() {
        return currentSize == maxSize;
    }

    /**
     * Returns true iff the hash table contains no entries.
     *
     * Time complexity: O(1)
     * @return true if dictionary is empty
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Returns the number of entries in the hash table.
     *
     * Time complexity: O(1)
     * @return number of elements in the dictionary
     */
    public int size() {
        return currentSize;
    }

    /**
     * Returns the value associated with the specified key,
     * or null if no such entry exists.
     *
     * @param key whose associated value is to be returned
     * @return value associated with key or null
     */
    public abstract V get(K key);

    /**
     * Inserts or updates the key-value pair.
     * Returns previous value if key existed, null otherwise.
     *
     * @param key key to associate value with
     * @param value value to insert or replace
     * @return previous value associated with key, or null
     */
    public abstract V put(K key, V value);

    /**
     * Removes the entry with the specified key.
     * Returns the value removed, or null if key not found.
     *
     * @param key key whose entry is to be removed
     * @return removed value, or null if key not found
     */
    public abstract V remove(K key);

    /**
     * Returns an iterator over the entries in the hash table.
     *
     * @return iterator of entries
     */
    public abstract Iterator<Entry<K,V>> iterator();

    /**
     * Returns an iterator over the values in the hash table.
     *
     * Time complexity: O(1)
     * @return iterator of values
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<V> values() {
        return new ValuesIterator(iterator());
    }

    /**
     * Returns an iterator over the keys in the hash table.
     *
     * Time complexity: O(1)
     * @return iterator of keys
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<K> keys() {
        return new KeysIterator(iterator());
    }
}
