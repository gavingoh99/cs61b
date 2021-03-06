package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // TODO figure out how to use byte array to solve the backwash problem
    // Instead of using two WQUF
    // https://github.com/SawyerSun/Algorithms-and-Data-Structures/blob/master/coursera-algorithms-princeton/src/main/java/one/Percolation.java
    private boolean[][] array;
    private int sites;
    private int topIndex;
    private int bottomIndex;
    private WeightedQuickUnionUF disjoint;
    private WeightedQuickUnionUF disjointTopOnly;
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        array = new boolean[N][N];
        sites = 0;
        // create a disjoint with N * N capacity,
        // we'll store the coordinates as a single int variable
        // from 0 to (N * N - 1)
        disjoint = new WeightedQuickUnionUF(N * N + 2);
        disjointTopOnly = new WeightedQuickUnionUF(N * N + 1);
        // implement a virtual site that exists
        // at the top and bottom of the grid
        // to connect to sites at top and bottom
        topIndex = N * N;
        bottomIndex = N * N + 1;
        // fill the array with 0 to denote not open
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                array[row][col] = false;
            }
        }
    }

    public void open(int row, int col) {
        if (row >= array.length || col >= array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            array[row][col] = true;
            sites++;
        }
        // we make use of two quickunions to handle the backwash
        // one quickunion only has a top virtual site
        // we'll check for connection using this quickunion
        // while we check for connection using the other
        // for percolation
        int setIndex = getSetIndex(row, col);
        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            disjoint.union(setIndex, setIndex - array.length);
            disjointTopOnly.union(setIndex, setIndex - array.length);
        } else if (row == 0) {
            disjoint.union(topIndex, setIndex);
            disjointTopOnly.union(topIndex, setIndex);
        }
        if (row + 1 < array.length && isOpen(row + 1, col)) {
            disjoint.union(setIndex, setIndex + array.length);
            disjointTopOnly.union(setIndex, setIndex + array.length);
        } else if (row == array.length - 1) {
            disjoint.union(bottomIndex, setIndex);
        }
        if (col + 1 < array.length && isOpen(row, col + 1)) {
            disjoint.union(setIndex, setIndex + 1);
            disjointTopOnly.union(setIndex, setIndex + 1);
        }
        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            disjoint.union(setIndex, setIndex - 1);
            disjointTopOnly.union(setIndex, setIndex - 1);
        }
    }
    public boolean isOpen(int row, int col) {
        if (row >= array.length || col >= array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return array[row][col];
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
        return disjointTopOnly.connected(setIndex, topIndex);
    }
    public int numberOfOpenSites() {
        return sites;
    }
    public boolean percolates() {
        return disjoint.connected(topIndex, bottomIndex);
    }
    private int getSetIndex(int row, int col) {
        return row * array.length + col;
    }

    public static void main(String[] args) {

    }
}
