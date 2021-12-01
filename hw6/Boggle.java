import java.util.List;
import java.util.ArrayList;
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
        reader.close();

        // read board and initialize 2d array
        List<String> words = new ArrayList<>();
        In boardReader = new In(boardFilePath);
        String[] lines = boardReader.readAllLines();
        for (int index = 0; index < lines.length - 1; index++) {
            if (lines[index].length() != lines[index + 1].length()) {
                throw new IllegalArgumentException("Input board not a rectangle");
            }
        }
        boardReader.close();
        char[][] board = new char[lines.length][lines[0].length()];
        for (int index = 0; index < lines.length; index++) {
            for (int i = 0; i < lines[index].length(); i++) {
                board[index][i] = lines[index].charAt(i);
            }
        }
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                boolean[][] visited = new boolean[board.length][board[y].length];
                List<String> list = longestWords(board, x, y, "" + board[y][x], trie, visited);
                if (list != null) {
                    for (String word: list) {
                        if (trie.containsWord(word) && word != null
                                && (words.isEmpty() || !words.contains(word))) {
                            words.add(word);
                        }
                    }
                }
            }
        }
        Collections.sort(words, Comparator.comparing(String::length,
            (length1, length2) -> {
                return length2 - length1;
            })
                .thenComparing(String.CASE_INSENSITIVE_ORDER));

        if (k < words.size()) {
            return words.subList(0, k);
        }
        return words;
    }
    private static List<String> longestWords(char[][] board,
                                             int x, int y, String word,
                                             Trie trie, boolean[][] visited) {
        if (!trie.containsPrefix(word)) {
            return null;
        }
        List<String> words = new ArrayList<>();
        if (trie.containsWord(word)) {
            words.add(word);
        }
        if (x - 1 >= 0) {
            if (!visited[y][x - 1]) {
                List<String> words1 = longestWords(board, x - 1, y,
                        word + board[y][x - 1], trie, makeVisitedArray(visited, x, y));
                if (words1 != null) {
                    words.addAll(words1);
                }
            }
            if (y - 1 >= 0 && !visited[y - 1][x - 1]) {
                List<String> words2 = longestWords(board, x - 1, y - 1,
                        word + board[y - 1][x - 1], trie, makeVisitedArray(visited, x, y));
                if (words2 != null) {
                    words.addAll(words2);
                }
            }
            if (y + 1 < board.length && !visited[y + 1][x - 1]) {
                List<String> words3 = longestWords(board, x - 1, y + 1,
                        word + board[y + 1][x - 1], trie, makeVisitedArray(visited, x, y));
                if (words3 != null) {
                    words.addAll(words3);
                }
            }
        }
        if (x + 1 < board[0].length) {
            if (!visited[y][x + 1]) {
                List<String> words4 = longestWords(board, x + 1, y,
                        word + board[y][x + 1], trie, makeVisitedArray(visited, x, y));
                if (words4 != null) {
                    words.addAll(words4);
                }
            }
            if (y - 1 >= 0 && !visited[y - 1][x + 1]) {
                List<String> words5 = longestWords(board, x + 1, y - 1,
                        word + board[y - 1][x + 1], trie, makeVisitedArray(visited, x, y));
                if (words5 != null) {
                    words.addAll(words5);
                }
            }
            if (y + 1 < board.length && !visited[y + 1][x + 1]) {
                List<String> words6 = longestWords(board, x + 1, y + 1,
                        word + board[y + 1][x + 1], trie, makeVisitedArray(visited, x, y));
                if (words6 != null) {
                    words.addAll(words6);
                }
            }
        }
        if (y - 1 >= 0 && !visited[y - 1][x]) {
            List<String> words7 = longestWords(board, x, y - 1,
                    word + board[y - 1][x], trie, makeVisitedArray(visited, x, y));
            if (words7 != null) {
                words.addAll(words7);
            }
        }
        if (y + 1 < board.length && !visited[y + 1][x]) {
            List<String> words8 = longestWords(board, x, y + 1,
                    word + board[y + 1][x], trie, makeVisitedArray(visited, x, y));
            if (words8 != null) {
                words.addAll(words8);
            }
        }

        for (int index = 0; index < words.size(); index++) {
            if (words.get(index) == null) {
                words.remove(index);
            }
            if (words.get(index).length() < 3) {
                words.remove(index);
            }
        }
        return words;
    }
    private static boolean[][] makeVisitedArray(boolean[][] original, int x, int y) {
        boolean[][] copy = new boolean[original.length][];
        for (int row = 0; row < original.length; row++) {
            copy[row] = new boolean[original[row].length];
            System.arraycopy(original[row], 0, copy[row], 0, original[row].length);
        }
        copy[y][x] = true;
        return copy;
    }
    public static void main(String[] args) {
        long start = System.nanoTime();
        List<String> test = Boggle.solve(7, "smallboard.txt");
        long end = System.nanoTime();
        System.out.println((end - start) / Math.pow(10, 6));
    }
}
