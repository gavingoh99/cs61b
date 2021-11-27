package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private int[][] tiles;
    private int size;
    private final int BLANK = 0;
    public Board(int[][] tiles) {
        this.size = tiles.length;
        this.tiles = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }
    public int tileAt(int i, int j) {
        if (inBounds(i, j)) {
            return this.tiles[i][j];
        } else {
            throw new java.lang.IndexOutOfBoundsException("Row or column out of bounds");
        }
    }

    private boolean inBounds(int i, int j) {
        return i >= 0 && i < size && j >= 0 && j < size;
    }
    public int size() {
        return size;
    }
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int rowWithBlank = -1;
        int colWithBlank = -1;
        int side = size();
        int[][] newTiles = new int[side][side];
        for (int row = 0; row < side; row++) {
            for (int col = 0; col < side; col++) {
                newTiles[row][col] = tileAt(row, col);
                if (tileAt(row, col) == BLANK) {
                    rowWithBlank = row;
                    colWithBlank = col;
                }
            }
        }
        for (int row = 0; row < side; row++) {
            for (int col = 0; col < side; col++) {
                // iterate through new tiles, and check for
                // corresponding row, col pairs that differ by one
                // ie if blank occurred at 0,0 we want 0,1 and 1,0
                if (Math.abs(row - rowWithBlank) + Math.abs(col - colWithBlank) - 1 == 0) {
                    // swap the tiles to the new coordinates and enqueue
                    newTiles[rowWithBlank][colWithBlank] = newTiles[row][col];
                    newTiles[row][col] = BLANK;
                    Board neighbour = new Board(newTiles);
                    neighbors.enqueue(neighbour);
                    // then swap back to original board to faciliate creating other
                    // combinations of the original board
                    newTiles[row][col] = newTiles[rowWithBlank][colWithBlank];
                    newTiles[rowWithBlank][colWithBlank] = BLANK;
                }
            }
        }
        return neighbors;
    }
    public int hamming() {
        int count = 0;
        for (int row = 0; row < size(); row++) {
            for (int col = 0; col < size(); col++) {
                int expectedTile = row * size() + col + 1;
                if (tileAt(row, col) != expectedTile && tileAt(row, col) != BLANK) {
                    count++;
                }
            }
        }
        return count;
    }
    public int manhattan() {
        int count = 0;
        for (int row = 0; row < size(); row++) {
            for (int col = 0; col < size(); col++) {
                int tileValue = tileAt(row, col);
                if (tileValue != BLANK) {
                    int correctRow = (tileValue - 1) / size;
                    int correctCol = (tileValue - 1) % size;
                    count += Math.abs(correctRow - row) + Math.abs(correctCol - col);
                }
            }
        }
        return count;
    }
    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }
    // Overriding the equals and hashcode
    // is extremely important if our program
    // wishes to use equality checks accurately
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() == this.getClass()) {
            Board compared = (Board) o;
            if (compared.size() != this.size()) {
                return false;
            }
            for (int row = 0; row < size(); row++) {
                for (int col = 0; col < size(); col++) {
                    if (compared.tileAt(row, col) != this.tileAt(row, col)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result *= 10;
                result += tiles[i][j];
            }
        }

        return result;
    }


    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
