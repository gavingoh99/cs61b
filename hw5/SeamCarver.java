import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energies;
    private double[][] minimum;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.energies = new double[height()][width()];
        this.minimum = new double[height()][width()];
    }
    public Picture picture() {
        return new Picture(this.picture);
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
        this.minimum = tempMinimums;
        return seam;
    }
    public int[] findVerticalSeam() {
        for (int w = 0; w < width(); w++) {
            this.minimum[0][w] = energy(w, 0);
        }
        for (int h = 1; h < height(); h++) {
            for (int w = 0; w < width(); w++) {
                minimum[h][w] = energy(w, h) + minimum[h - 1][w];
                if (w - 1 >= 0 && energy(w, h) + minimum[h - 1][w - 1] < minimum[h][w]) {
                    minimum[h][w] = energy(w, h) + minimum[h - 1][w - 1];
                }
                if (w + 1 <= width() - 1 && energy(w, h) + minimum[h - 1][w + 1] < minimum[h][w]) {
                    minimum[h][w] = energy(w, h) + minimum[h - 1][w + 1];
                }
            }
        }
        double min = Double.MAX_VALUE;
        int x = -1;
        for (int col = 0; col < width(); col++) {
            double curr = minimum[height() - 1][col];
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
            if (x - 1 >= 0 && min == minimum[height - 1][x - 1]) {
                x = x - 1;
            } else if (x + 1 < width() && min == minimum[height - 1][x + 1]) {
                x = x + 1;
            }
        }
        return seam;
    }
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length == 0) {
            return;
        }
        if (checkSeam(seam)) {
            this.picture = SeamRemover.removeHorizontalSeam(this.picture, seam);
            this.energies = new double[height()][width()];
            this.minimum = new double[height()][width()];
        } else {
            throw new IllegalArgumentException();
        }
    }
    public void removeVerticalSeam(int[] seam) {
        if (seam.length == 0) {
            return;
        }
        if (checkSeam(seam)) {
            this.picture = SeamRemover.removeVerticalSeam(this.picture, seam);
            this.energies = new double[height()][width()];
            this.minimum = new double[height()][width()];
        } else {
            throw new IllegalArgumentException();
        }
    }
    private boolean checkSeam(int[] seam) {
        for (int index = 0; index < seam.length - 1; index++) {
            if (Math.abs(seam[index] - seam[index + 1]) > 1) {
                return false;
            }
        }
        return true;
    }
}
