import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

public class Trie {
    private static class TrieNode implements Comparable<TrieNode> {
        private char letter;
        private boolean exists;
        private Map<Character, TrieNode> next;

        TrieNode(char letter) {
            this.letter = letter;
            this.exists = false;
            this.next = new HashMap<>();
        }
        public TrieNode addNextLetter(char character) {
            if (!next.containsKey(character)) {
                next.put(character, new TrieNode(character));
            }
            return next.get(character);
        }
        @Override
        public int compareTo(TrieNode other) {
            if (other == null) {
                throw new IllegalArgumentException("cannot compare with null");
            }
            return this.letter - other.letter;
        }
        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o.getClass() != this.getClass()) {
                return false;
            }
            TrieNode compared = (TrieNode) o;
            return this.letter == compared.letter;
        }
        @Override
        public int hashCode() {
            return Objects.hashCode(letter);
        }
    }
    private static final char FIRSTVALID = 'a';
    private static final char LASTVALID = 'z';
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode(' ');
    }
    // traverses trie to add word
    public void addWord(String s) {
        if (s == null || s.equals("")) {
            return;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!validLetter(s.charAt(i))) {
                return;
            }
        }
        TrieNode pointer = this.root;
        for (int i = 0; i < s.length(); i++) {
            pointer = pointer.addNextLetter(s.charAt(i));
        }
        pointer.exists = true;
    }

    private boolean validLetter(char character) {
        return character >= FIRSTVALID && character <= LASTVALID || character == ' ';
    }

    public List<String> getWordsByPrefix(String s) {
        List<String> results = new ArrayList<>();
        if (s.equals("")) {
            return results;
        }
        TrieNode pointer = this.root;
        // traverse the string till we've reached the last letter
        // pointer holds the reference to last letter
        for (int i = 0; i < s.length(); i++) {
            char currLetter = s.charAt(i);
            if (!pointer.next.containsKey(currLetter)) {
                return results;
            }
            pointer = pointer.addNextLetter(currLetter);
        }
        // search for the suffix from the last letter
        // remove last letter from prefix
        String prefix = s.substring(0, s.length() - 1);
        List<String> suffixes = getAllWords(pointer);
        for (String suffix: suffixes) {
            results.add(prefix + suffix);
        }
        return results;
    }
    private List<String> getAllWords(TrieNode node) {
        List<String> words = new ArrayList<>();
        getAllWords(words, "", node);
        return words;
    }
    // recursively adds to list of words all suffixes from the specified node
    private void getAllWords(List<String> words, String s, TrieNode node) {
        if (node == null) {
            return;
        }
        if (node.exists) {
            words.add(s + node.letter);
        }
        for (TrieNode currNode: node.next.values()) {
            getAllWords(words, s + node.letter, currNode);
        }
    }
}
