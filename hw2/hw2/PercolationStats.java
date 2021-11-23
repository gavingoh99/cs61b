package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private int lengthOfGrid;
    private int numberOfExperiments;
    private double mean;
    private double dev;
    private PercolationFactory sampleMaker;
    private double[] expThreshold;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        lengthOfGrid = N;
        numberOfExperiments = T;
        sampleMaker = pf;
        expThreshold = new double[numberOfExperiments];
        int currExperiment = 0;
        while (currExperiment < numberOfExperiments) {
            Percolation sample = sampleMaker.make(lengthOfGrid);
            int result = 0;
            while (!sample.percolates()) {
                int row = StdRandom.uniform(lengthOfGrid);
                int col = StdRandom.uniform(lengthOfGrid);
                sample.open(row, col);
                result++;
            }
            expThreshold[currExperiment] = (((double) result) / (lengthOfGrid * lengthOfGrid));
            currExperiment++;
        }

    }
    public double mean() {
        mean = StdStats.mean(expThreshold);
        return mean;
    }
    public double stddev() {
        dev = StdStats.stddev(expThreshold);
        return dev;
    }
    public double confidenceLow() {
        double low = mean - (1.96 * dev) / java.lang.Math.sqrt(numberOfExperiments);
        return low;
    }
    public double confidenceHigh() {
        double high = mean + (1.96 * dev) / java.lang.Math.sqrt(numberOfExperiments);
        return high;
    }

}
