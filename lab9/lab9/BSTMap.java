package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp == 0) {
            return p.value;
        } else if (cmp < 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        assert key != null;
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
     * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size += 1;
            return new Node(key, value);
        }
        int cmp = key.compareTo(p.key);
        if (cmp == 0) {
            p.value = value;
        } else if (cmp < 0) {
            p.left = putHelper(key, value, p.left);
        } else {
            p.right = putHelper(key, value, p.right);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        assert key != null;
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    private void keySetHelper(Set<K> set, Node node) {
        if (node == null) {
            return;
        }
        keySetHelper(set, node.left);
        set.add(node.key);
        keySetHelper(set, node.right);
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keyset = new HashSet<>();
        keySetHelper(keyset, root);
        return keyset;
    }

    private Node findMinNode(Node node) {
        if (node.left == null) {
            return node;
        }
        return findMinNode(node.left);
    }

    private Node updateBranch(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = updateBranch(node.left);
        return node;
    }

    private Node update(K key, Node node) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = update(key, node.left);
        } else if (cmp > 0) {
            node.right = update(key, node.right);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            Node t = node;
            node = findMinNode(t.right);
            node.right = updateBranch(t.right);
            node.left = t.left;
        }
        return node;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        V value = get(key);
        size -= 1;
        root = update(key, root);
        return value;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (get(key) != value) {
            return null;
        }
        return remove(key);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
