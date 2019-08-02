/* *****************************************************************************
 *  Name: it's-a me
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private int w;
    private int h;
    private int[][] picArray;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("null input");
        w = picture.width();
        h = picture.height();
        picArray = new int[h][w];
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                picArray[row][col] = picture.getRGB(col, row);
            }
        }

    }

    public Picture picture() {
        Picture pic = new Picture(w, h);
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                pic.setRGB(col, row, picArray[row][col]);
            }
        }

        return pic;
    }

    public int width() {
        return this.w;
    }

    public int height() {
        return this.h;
    }

    public double energy(int x, int y) {
        if (x > this.w || x < 0 || y > this.h || y < 0) {
            throw new IllegalArgumentException("index out of range");
        }

        if (x == 0 || x == w - 1 || y == 0 || y == h - 1) return 1000;

        int leftRGB = 0;
        int rightRGB = 0;
        int topRGB = 0;
        int bottomRGB = 0;
        if (w > 1) {
            leftRGB = picArray[y][x - 1];
            rightRGB = picArray[y][x + 1];
        }
        if (h > 1) {
            topRGB = picArray[y - 1][x];
            bottomRGB = picArray[y + 1][x];
        }

        double xgrad = gradient(leftRGB, rightRGB);
        double ygrad = gradient(topRGB, bottomRGB);

        double grad = Math.sqrt(xgrad + ygrad);

        return grad;
    }

    private double gradient(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = (rgb1) & 0xFF;
        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >> 8) & 0xFF;
        int b2 = (rgb2) & 0xFF;

        double grad = Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2);

        return grad;
    }

    public int[] findHorizontalSeam() {
        int[] seam = new int[this.w];
        double seamEnergy = Double.POSITIVE_INFINITY;
        double[][] energy = new double[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                energy[y][x] = this.energy(x, y);
            }
        }

        for (int y = 1; y < h - 1; y++) {
            int[] thisSeam = new int[w];
            thisSeam[1] = y;
            double thisSeamEnergy = energy[y][1];

            for (int x = 2; x < w - 1; x++) {
                int min = thisSeam[x - 1] - 1;
                double minEnergy = energy[min][x];
                if (energy[thisSeam[x - 1]][x] < minEnergy) {
                    min = thisSeam[x - 1];
                    minEnergy = energy[thisSeam[x - 1]][x];
                }
                if (energy[thisSeam[x - 1] + 1][x] < minEnergy) {
                    min = thisSeam[x - 1] + 1;
                    minEnergy = energy[thisSeam[x - 1] + 1][x];
                }
                thisSeam[x] = min;
                thisSeamEnergy += minEnergy;
            }

            if (thisSeamEnergy < seamEnergy) {
                seam = thisSeam;
            }
        }

        seam[0] = seam[1];
        seam[w - 1] = seam[w - 2];
        return seam;
    }

    public int[] findVerticalSeam() {
        int[] seam = new int[h];
        double seamEnergy = Double.POSITIVE_INFINITY;
        double[][] energy = new double[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                energy[y][x] = this.energy(x, y);
            }
        }
        for (int x = 1; x < w - 1; x++) {
            int[] thisSeam = new int[h];
            thisSeam[1] = x;
            double thisSeamEnergy = energy[1][x];

            for (int y = 2; y < h - 1; y++) {
                int min = thisSeam[y - 1] - 1;
                double minEnergy = energy[y][min];
                if (energy[y][thisSeam[y - 1]] < minEnergy) {
                    min = thisSeam[y - 1];
                    minEnergy = energy[y][thisSeam[y - 1]];
                }
                if (energy[y][thisSeam[y - 1] + 1] < minEnergy) {
                    min = thisSeam[y - 1] + 1;
                    minEnergy = energy[y][thisSeam[y - 1] + 1];
                }
                thisSeam[y] = min;
                thisSeamEnergy += minEnergy;
            }

            if (thisSeamEnergy < seamEnergy) {
                seam = thisSeam;
            }
        }

        seam[0] = seam[1];
        seam[h - 1] = seam[h - 2];

        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null arg");
        if (seam.length != w) throw new IllegalArgumentException("wrong size");
        if (h <= 1) throw new IllegalArgumentException("image too small");
        if (seam[0] >= h || seam[0] < 0) throw new IllegalArgumentException("index out of range");
        for (int i = 1; i < w; i++) {
            if (seam[i] >= h || seam[i] < 0)
                throw new IllegalArgumentException("index out of range");
            if (Math.abs(seam[i] - seam[i - 1]) >= 2)
                throw new IllegalArgumentException("not a valid seam");
        }

        int[][] transposed = new int[w][h];

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                transposed[col][row] = picArray[row][col];
            }
        }

        int[][] transposedPic = new int[w][h - 1];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < seam[i]; j++) {
                transposedPic[i][j] = transposed[i][j];
            }

            for (int j = seam[i] + 1; j < h; j++) {
                transposedPic[i][j - 1] = transposed[i][j];
            }
        }

        this.h -= 1;

        int[][] newPic = new int[h][w];
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                newPic[row][col] = transposedPic[col][row];
            }
        }

        this.picArray = newPic;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null arg");
        if (seam.length != h) throw new IllegalArgumentException("wrong size");
        if (w <= 1) throw new IllegalArgumentException("image too small");
        if (seam[0] >= w || seam[0] < 0) throw new IllegalArgumentException("index out of range");
        for (int i = 1; i < h; i++) {
            if (seam[i] >= w || seam[i] < 0)
                throw new IllegalArgumentException("index out of range");
            if (Math.abs(seam[i] - seam[i - 1]) >= 2)
                throw new IllegalArgumentException("not a valid seam");
        }

        int[][] newPic = new int[h][w - 1];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < seam[i]; j++) {
                newPic[i][j] = this.picArray[i][j];
            }
            for (int j = seam[i] + 1; j < w; j++) {
                newPic[i][j - 1] = this.picArray[i][j];
            }
        }

        this.picArray = newPic;
        this.w -= 1;
    }

    public static void main(String[] args) {

        // empty
    }
}
