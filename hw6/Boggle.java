
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        if (k < 0) {
            throw new IllegalArgumentException("k is not positive");
        }
        // read dict and initialize trie
        In reader = new In(dictPath);
        Trie trie = new Trie();
        while (reader.hasNextLine()) {
            String line = reader.readLine();
            trie.put(line);
        }

        // read board and initialize 2d array

        In boardReader = new In(boardFilePath);
        String[] lines = boardReader.readAllLines();
        char[][] board = new char[lines.length][];
        for (int index = 0; index < lines.length; index++) {
            if (index != lines.length - 1 && lines[index].length() != lines[index + 1].length()) {
                throw new IllegalArgumentException("Input board not a rectangle");
            }
            board[index] = lines[index].toCharArray();
        }
        BoggleGraph graph = BoggleGraph.initialize(board, trie);
        List<String> words = graph.findValidWords();

        Collections.sort(words, Comparator.comparing(String::length,
            (length1, length2) -> {
                return length2 - length1;
            })
                .thenComparing(String.CASE_INSENSITIVE_ORDER));

        int numItems = k < words.size() ? k : words.size();
        return words.subList(0, numItems);
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        List<String> test = Boggle.solve(7, "smallboard2.txt");
        long end = System.nanoTime();
        System.out.println((end - start) / Math.pow(10, 6));
        System.out.println(test);
    }
}
