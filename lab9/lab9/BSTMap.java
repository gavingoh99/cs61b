package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>, Iterable<K> {

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
        if (p.key.equals(key)) {
            return p.value;
        } else if (key.compareTo(p.key) > 0) {
            return getHelper(key, p.right);
        } else {
            return getHelper(key, p.left);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size++;
            return new Node(key, value);
        }
        if (key.compareTo(p.key) > 0) {
            p.right = putHelper(key, value, p.right);
        } else if (key.compareTo(p.key) < 0) {
            p.left = putHelper(key, value, p.left);
        } else {
            p.value = value;
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.size;
    }
    public static void main(String[] args) {
        BSTMap<String, Integer> test = new BSTMap<>();
        test.put("hello", 5);
        test.put("a", 3);
        test.put("no", 1);
        test.remove("hello");
        System.out.println(test.size());
        System.out.println(test.containsKey("no"));
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    private void setAdder(Set set, Node p) {
        if (p == null) {
            return;
        }
        set.add(p.key);
        setAdder(set, p.left);
        setAdder(set, p.right);
    }
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        setAdder(keySet, root);
        return keySet;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    private Node update(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (key.compareTo(p.key) > 0) {
            p.right = update(key, p.right);
        } else if (key.compareTo(p.key) < 0) {
            p.left = update(key, p.left);
        } else {
            if (p.left == null) {
                return p.right;
            }
            if (p.right == null) {
                return p.left;
            }
            Node toBeRemoved = p;
            p = findSmallestNode(p.right);
            p.left = toBeRemoved.left;
            // remove the smallest node from the right side of
            // the initial BST
            p.right = removeSuccessorFrom(toBeRemoved.right);
        }
        return p;
    }
    // given a node, we traverse the tree in search of the
    // smallest node then we remove it from the branch
    private Node removeSuccessorFrom(Node p) {
        // once we've reached the smallest node
        // set the connection of parent to this node
        // to be the value of the smallest node's right child
        // either null or an actual node
        if (p.left == null) {
            return p.right;
        }
        p.left = removeSuccessorFrom(p.left);
        return p;
    }
    private Node findSmallestNode(Node p) {
        if (p.left == null) {
            return p;
        }
        return findSmallestNode(p.left);
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        V value = get(key);
        size--;
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
