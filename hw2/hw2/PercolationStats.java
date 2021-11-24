package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private int lengthOfGrid;
    private int numberOfExperiments;
    private PercolationFactory sampleMaker;
    private double[] expThreshold;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        lengthOfGrid = N;
        numberOfExperiments = T;
        sampleMaker = pf;
        expThreshold = new double[numberOfExperiments];
        for (int i = 0; i < N; i++) {
            Percolation sample = sampleMaker.make(lengthOfGrid);
            expThreshold[i] = doExperiment(sample, lengthOfGrid);
        }
    }
    private double doExperiment(Percolation p, int N) {
        while (!p.percolates()) {
            int row = StdRandom.uniform(0, N);
            int col = StdRandom.uniform(0, N);
            p.open(row, col);
        }
        return (double) p.numberOfOpenSites() / (N * N);
    }
    public double mean() {
        return StdStats.mean(expThreshold);
    }
    public double stddev() {
        return StdStats.stddev(expThreshold);
    }
    public double confidenceLow() {
        double value = (1.96 * stddev()) / java.lang.Math.sqrt(numberOfExperiments);
        return mean() - value;
    }
    public double confidenceHigh() {
        double value = (1.96 * stddev()) / java.lang.Math.sqrt(numberOfExperiments);
        return mean() + value;
    }

}
