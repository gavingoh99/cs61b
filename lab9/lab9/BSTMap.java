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
        test.put("hi", 3);
        test.put("no", 1);
        test.remove("hello");
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
    private V keyFinder(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (p.left != null && p.left.key.equals(key)) {
            // if left child is the key, check for children
            // no children just remove node and sever connection from parent
            if (p.left.left == null && p.left.right == null) {
                V value = p.left.value;
                p.left = null;
                return value;
            // 1 child we have to preserve the connection from grandparent to child
            } else if (p.left.left == null || p.left.right == null) {
                V value = p.left.value;
                // if right child is present we link grandparent to right grandchild else left grandchild
                if (p.left.left == null) {
                    p.left = p.left.right;
                } else {
                    p.left = p.left.left;
                }
                return value;
            // both children are around then we must decide on a successor
            } else {
                Node toBeRemoved = findSuccessorAndReplace(p.left);
                return toBeRemoved.value;
            }
        } else if (p.right != null && p.right.key.equals(key)) {
            // if left child is the key, check for children
            // no children just remove node and sever connection from parent
            if (p.right.left == null && p.right.right == null) {
                V value = p.right.value;
                p.right = null;
                return value;
                // 1 child we have to preserve the connection from grandparent to child
            } else if (p.right.left == null || p.right.right == null) {
                V value = p.right.value;
                // if right child is present we link grandparent to right grandchild else left grandchild
                if (p.right.left == null) {
                    p.right = p.right.right;
                } else {
                    p.right = p.right.left;
                }
                return value;
                // both children are around then we must decide on a successor
            } else {
                Node toBeRemoved = findSuccessorAndReplace(p.right);
                return toBeRemoved.value;
            }
        }
        if (key.compareTo(p.key) > 0) {
            return keyFinder(key, p.right);
        } else {
            return keyFinder(key, p.left);
        }
    }
    private Node findSmallestNode(Node p) {
        Node currNode = p;
        Node prevNode = root;
        while (true) {
            // node has no children
            if (currNode.left == null && currNode.right == null) {
                if (currNode == p) {
                    return currNode;
                }
                prevNode.left = null;
                return currNode;
            }
            // node has one child - left branch which is less than this node
            if (currNode.left == null) {
                prevNode.left = currNode.right;
                return currNode;
            }
            prevNode = currNode;
            currNode = currNode.left;
        }
    }
    private Node findLargestNode(Node p) {
        Node currNode = p;
        Node prevNode = root;
        while (true) {
            // node has no children
            if (currNode.left == null && currNode.right == null) {
                if (currNode == p) {
                    return currNode;
                }
                prevNode.right = null;
                return currNode;
            }
            // node has one child - left branch which is less than this node
            if (currNode.right == null) {
                prevNode.right = currNode.left;
                return currNode;
            }
            prevNode = currNode;
            currNode = currNode.right;
        }
    }
    @Override
    public V remove(K key) {
        if (root.key.equals(key)) {
            if (root.left == null && root.right == null) {
                V value = root.value;
                root = null;
                return value;
            } else if (root.left == null) {
                V value = root.value;
                root = root.right;
                return value;
            } else if (root.right == null) {
                V value = root.value;
                root = root.left;
                return value;
            } else {
                Node toBeRemoved = findSuccessorAndReplace(root);
                return toBeRemoved.value;
            }
        }
        return keyFinder(key, root);
    }
    // finds successor to replace the given node with two children, then
    // after severing everything we return the initial node
    private Node findSuccessorAndReplace(Node p) {
        Node left = p.left;
        Node right = p.right;
        Node successor = findSmallestNode(left);
        successor.left = left;
        successor.right = right;
        Node toBeRemoved = p;
        p = successor;
        return toBeRemoved;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/


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
