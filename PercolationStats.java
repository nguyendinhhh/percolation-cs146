import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    private double[] results;

    // perform T independent computational experiments on an
    // N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) throw new IllegalArgumentException();
        this.results = new double[T];

        for (int i = 0; i < T; i++) {
            int openedSites = 0;
            Percolation p = new Percolation(N);
            while (!p.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                if (!p.isOpen(row, col)) {
                    openedSites++;
                    p.open(row, col);
                }
            }
            results[i] = (double) openedSites / (double) (N * N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low end of 95% confidence interval
    public double confidenceLow() {
        return mean() - ((1.96 * StdStats.stddev(results)) / Math.sqrt(results.length));
    }

    // high end of 95% confidence interval
    public double confidenceHigh() {
        return mean() + ((1.96 * StdStats.stddev(results)) / Math.sqrt(results.length));
    }


    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}
