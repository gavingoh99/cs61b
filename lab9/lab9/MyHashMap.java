package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V>, Iterable<K> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        ArrayMap<K, V> bucket = buckets[hash(key)];
        return bucket.get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (loadFactor() > MAX_LF) {
            resize();
        }
        int hash = hash(key);
        if (buckets[hash] == null) {
            buckets[hash] = new ArrayMap<>();
        }
        // if bucket already contains key, we are merely replacing
        // only increase size if we are adding not replacing
        if (!buckets[hash].containsKey(key)) {
            size++;
        }
        buckets[hash].put(key, value);
    }
    private void resize() {
        // create new buckets
        ArrayMap<K, V>[] oldBuckets = buckets;
        buckets = new ArrayMap[buckets.length * 2];
        clear();
        // iterate over existing buckets and recalculate hash
        for (ArrayMap<K, V> bucket: oldBuckets) {
            for (K existingKey : bucket.keySet()) {
                int hash = hash(existingKey);
                buckets[hash].put(existingKey, bucket.get(existingKey));
                size++;
            }
        }
    }
    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        for (String test: map) {
            System.out.println(test);
        }
    }
    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (ArrayMap<K, V> bucket: buckets) {
            for (K k: bucket.keySet()) {
                keySet.add(k);
            }
        }
        return keySet;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        size--;
        return buckets[hash(key)].remove(key);
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        if (!get(key).equals(value)) {
            return null;
        }
        return remove(key);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
