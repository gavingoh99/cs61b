import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energies;
    private double[][] minimum;
    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.energies = new double[height()][width()];
        this.minimum = new double[height()][width()];
    }
    public Picture picture() {
        return this.picture;
    }
    public int width() {
        return this.picture.width();
    }
    public int height() {
        return this.picture.height();
    }
    public double energy(int x, int y) {
        if (!validateParams(x, y)) {
            throw new IndexOutOfBoundsException("x or y is out of range");
        }
        if (energies[y][x] == 0.0) {
            int leftX = x - 1 >= 0 ? x - 1 : width() - 1;
            int rightX = x + 1 < width() ? x + 1 : 0;
            int upY = y - 1 >= 0 ? y - 1 : height() - 1;
            int downY = y + 1 < height() ? y + 1 : 0;
            Color leftPixel = picture.get(leftX, y);
            Color rightPixel = picture.get(rightX, y);
            Color upPixel = picture.get(x, upY);
            Color downPixel = picture.get(x, downY);

            double xRed = Math.pow((leftPixel.getRed() - rightPixel.getRed()), 2);
            double xGreen = Math.pow((leftPixel.getGreen() - rightPixel.getGreen()), 2);
            double xBlue = Math.pow((leftPixel.getBlue() - rightPixel.getBlue()), 2);
            double yRed = Math.pow((upPixel.getRed() - downPixel.getRed()), 2);
            double yGreen = Math.pow((upPixel.getGreen() - downPixel.getGreen()), 2);
            double yBlue = Math.pow((upPixel.getBlue() - downPixel.getBlue()), 2);

            double xValue = xRed + xGreen + xBlue;
            double yValue = yRed + yGreen + yBlue;

            double energy = xValue + yValue;
            energies[y][x] = energy;
        }
        return energies[y][x];
    }
    private boolean validateParams(int x, int y) {
        return x >= 0 && x < width() && y >= 0 && y < height();
    }
    public int[] findHorizontalSeam() {
        // 1 2 3 4
        // 5 6 7 8

        // 1 5
        // 2 6
        // 3 7
        // 4 8
        Picture transposed = new Picture(height(), width());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                Color pixel = this.picture.get(col, row);
                transposed.set(row, col, pixel);
            }
        }
        Picture placeholder = this.picture;
        double[][] tempEnergies = this.energies;
        double[][] tempMinimums = this.minimum;
        this.minimum = new double[width()][height()];
        this.energies = new double[width()][height()];
        this.picture = transposed;
        int[] seam = findVerticalSeam();
        this.picture = placeholder;
        this.energies = tempEnergies;
        return seam;
    }
    public int[] findVerticalSeam() {
        double min = Double.MAX_VALUE;
        int x = -1;
        for (int col = 0; col < width(); col++) {
            double curr = minFinder(col, height() - 1);
            if (curr < min) {
                min = curr;
                x = col;
            }
        }
        int[] seam = new int[height()];
        for (int height = seam.length - 1; height >= 0; height--) {
            seam[height] = x;
            min -= energy(x, height);
            if (height == 0) {
                break;
            }
            if (x - 1 >= 0 && min == minFinder(x - 1, height - 1)) {
                x = x - 1;
            } else if (x + 1 < width() && min == minFinder(x + 1, height - 1)) {
                x = x + 1;
            }
        }
        return seam;
    }
    private double minFinder(int x, int y) {
        if (y == 0) {
            return energy(x, y);
        }
        if (minimum[y][x] == 0.0) {
            double min = energy(x, y);
            double minAbove = minFinder(x, y - 1);
            double minDiagonalLeft = x > 0 ? minFinder(x - 1, y - 1) : Double.MAX_VALUE;
            double minDiagonalRight = x < width() - 1 ? minFinder(x + 1, y - 1) : Double.MAX_VALUE;
            if (minAbove < minDiagonalLeft && minAbove < minDiagonalRight) {
                min += minAbove;
            } else if (minDiagonalLeft < minAbove && minDiagonalLeft < minDiagonalRight) {
                min += minDiagonalLeft;
            } else {
                min += minDiagonalRight;
            }
            minimum[y][x] = min;
        }
        return minimum[y][x];
    }
    public void removeHorizontalSeam(int[] seam) {
        this.picture = SeamRemover.removeHorizontalSeam(this.picture, seam);
    }
    public void removeVerticalSeam(int[] seam) {
        this.picture = SeamRemover.removeVerticalSeam(this.picture, seam);
    }
}
