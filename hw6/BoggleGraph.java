import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayDeque;

public class BoggleGraph {
    private Set<Node> nodes;
    private Trie trie;
    public BoggleGraph(Trie trie) {
        this.trie = trie;
        this.nodes = new HashSet<>();
    }
    public static BoggleGraph initialize(char[][] board, Trie trie) {
        BoggleGraph graph = new BoggleGraph(trie);
        Node[][] grid = new Node[board.length][board[0].length];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[y][x] = new Node(board[y][x]);
                graph.nodes.add(grid[y][x]);
            }
        }
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                addNeighboursInGrid(grid, y, x);
            }
        }
        return graph;
    }
    private static void addNeighboursInGrid(Node[][] grid, int row, int col) {
        int height = grid.length;
        int width = grid[0].length;
        for (int i = -1; i <= +1; i++) {
            int r = row + i;
            for (int j = -1; j <= +1; j++) {
                int c = col + j;
                if ((r == row && c == col) || (r < 0 || r >= height) || (c < 0 || c >= width)) {
                    continue;
                }
                Node node = grid[row][col];
                Node neighbour = grid[r][c];
                node.addNeighbour(neighbour);
            }
        }
    }
    public List<String> findValidWords() {
        Set<String> words = new HashSet<>();
        for (Node node: nodes) {
            List<String> wordsFrom = findWordsFrom(node);
            words.addAll(wordsFrom);
        }
        List<String> results = new ArrayList<>(words);
        return results;
    }
    private List<String> findWordsFrom(Node node) {
        Queue<Word> fringe = new ArrayDeque<>();
        List<String> words = new ArrayList<>();
        Word base = new Word(node);
        fringe.add(base);
        while (!fringe.isEmpty()) {
            Word curr = fringe.remove();
            String word = curr.getWord();
            if (word.length() > 3 && trie.containsWord(word)) {
                words.add(word);
            }
            if (trie.containsPrefix(word)) {
                Node tail = curr.tail();
                for (Node neighbour: tail.getNeighbours()) {
                    if (!curr.contains(neighbour)) {
                        Word copy = (Word) curr.clone();
                        copy.extend(neighbour);
                        fringe.add(copy);
                    }
                }
            }
        }
        return words;
    }
}
