import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    private static class Node implements Comparable<Node>, Serializable {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }
        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }
        @Override
        public int compareTo(Node node) {
            return this.freq - node.freq;
        }
    }
    private Node root;
    private Map<Character, BitSequence> lookup = new HashMap<>();
    public BinaryTrie(Map<Character, Integer> frequency) {
        MinPQ<Node> pq = new MinPQ<>();
        for (char character: frequency.keySet()) {
            int freq = frequency.get(character);
            if (freq > 0) {
                pq.insert(new Node(character, freq, null, null));
            }
        }
        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        this.root = pq.delMin();
    }
    public Match longestPrefixMatch(BitSequence querySequence) {
        return longestPrefixMatch(querySequence, root, 0, "");
    }
    private Match longestPrefixMatch(BitSequence querySequence, Node node, int index, String s) {
        if (node.isLeaf()) {
            return new Match(new BitSequence(s), node.ch);
        } else {
            int bit = querySequence.bitAt(index);
            if (bit == 0) {
                return longestPrefixMatch(querySequence, node.left, index + 1, s + "0");
            } else {
                return longestPrefixMatch(querySequence, node.right, index + 1, s + "1");
            }
        }
    }
    public Map<Character, BitSequence> buildLookupTable() {
        buildLookupTable("", root);
        return this.lookup;
    }
    private void buildLookupTable(String s, Node node) {
        if (!node.isLeaf()) {
            buildLookupTable(s + "0", node.left);
            buildLookupTable(s + "1", node.right);
        } else {
            lookup.put(node.ch, new BitSequence(s));
        }
    }
}
