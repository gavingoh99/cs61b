package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] array;
    private int sites;
    private int topIndex;
    private int bottomIndex;
    private static WeightedQuickUnionUF disjoint;
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        array = new int[N][N];
        sites = 0;
        // create a disjoint with N * N capacity,
        // we'll store the coordinates as a single int variable
        // from 0 to (N * N - 1)
        disjoint = new WeightedQuickUnionUF(N * N + 2);
        // implement a virtual site that exists
        // at the top and bottom of the grid
        // to connect to sites at top and bottom
        topIndex = N * N;
        bottomIndex = N * N + 1;
        // fill the array with 0 to denote not open
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                array[row][col] = 0;
            }
        }
    }

    public void open(int row, int col) {
        if (row >= array.length || col >= array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            array[row][col] = 1;
            sites++;
        }
        if (sites > 1) {
            int setIndex = getSetIndex(row, col);
            if (row - 1 >= 0 && isOpen(row - 1, col)) {
                disjoint.union(setIndex, setIndex - array.length);
            } else if (row == 0) {
                disjoint.union(topIndex, setIndex);
            }
            // to be more discriminate, we only connect the bottom row
            // to the bottom virtual site if it is already connected to
            // the top virtual site, this is accomplished
            // by first connecting the bottom row to potentially any
            // sites above it to complete the connection
            // to top virtual site
            // then checking for the connection to top site
            // as part of the conditional, and only connecting
            // if the connection already exists
            // this means that we won't be adding
            // a connection to top virtual site unless
            // the site in question already has a connection
            // to it from sites above it
            if (row + 1 < array.length && isOpen(row + 1, col)) {
                disjoint.union(setIndex, setIndex + array.length);
            } else if (row == array.length - 1 && disjoint.find(setIndex) == topIndex) {
                disjoint.union(bottomIndex, setIndex);
            }
            if (col + 1 < array.length && isOpen(row, col + 1)) {
                disjoint.union(setIndex, setIndex + 1);
            }
            if (col - 1 >= 0 && isOpen(row, col - 1)) {
                disjoint.union(setIndex, setIndex - 1);
            }
        }
    }
    public boolean isOpen(int row, int col) {
        if (row >= array.length || col >= array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return array[row][col] == 1;
    }
    // there is a drawback of virtual sites
    // if we are not discriminate in connecting them,
    // we can lead to wrongfully concluding that a site is full
    // ie a site can be connected to the bottom virtual site
    // which connects to the top virtual site via another series
    // of sites that are full:
    // 1 0 0 0
    // 1 0 0 0
    // 1 0 1 0
    // 1 0 1 0
    // we may wrongfully conclude that (2, 1) is full since it
    // can connect to top virtual site if its connected to bottom site
    public boolean isFull(int row, int col) {
        if (row >= array.length || col >= array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int setIndex = getSetIndex(row, col);
        return disjoint.connected(setIndex, topIndex);
    }
    public int numberOfOpenSites() {
        return sites;
    }
    public boolean percolates() {
        return disjoint.connected(topIndex, bottomIndex);
    }
    public int getSetIndex(int row, int col) {
        return row * array.length + col;
    }

    public static void main(String[] args) {
        Percolation test = new Percolation(4);
        test.open(2, 0);
        test.open(3, 0);
        test.open(1, 0);
        test.open(0, 0);
        test.open(0, 3);
        test.open(1, 3);
        System.out.println(test.percolates());
    }
}
