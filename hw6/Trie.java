import java.util.HashMap;
import java.util.Map;

public class Trie {
    private class Node {
        boolean exists;
        Map<Character, Node> links;

        Node() {
            exists = false;
            links = new HashMap<>();
        }
    }

    private Node root;
    public Trie() {
        this.root = new Node();
    }
    public boolean containsWord(String key) {
        if (key == null) {
            return false;
        }
        return containsWord(root, key, 0);
    }
    private boolean containsWord(Node node, String key, int index) {
        if (index == key.length()) {
            return node.exists;
        }
        Node nextNode = node.links.get(key.charAt(index));
        if (nextNode == null) {
            return false;
        }
        return containsWord(nextNode, key, index + 1);
    }
    public boolean containsPrefix(String key) {
        return containsPrefix(root, key, 0);
    }
    private boolean containsPrefix(Node node, String key, int index) {
        if (index == key.length()) {
            return true;
        }
        Node nextNode = node.links.get(key.charAt(index));
        if (nextNode == null) {
            return false;
        }
        return containsPrefix(nextNode, key, index + 1);
    }
    public void put(String key) {
        put(root, key, 0);
    }
    private Node put(Node node, String key, int index) {
        if (node == null) {
            node = new Node();
        }
        if (index == key.length()) {
            node.exists = true;
            return node;
        }
        char character = key.charAt(index);
        node.links.put(character, put(node.links.get(character), key, index + 1));
        return node;
    }
}
