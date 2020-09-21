import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// Models an N-by-N percolation system.
public class Percolation {
    private int size;
    private int openSites;
    private boolean[][] grid;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf2;

    // Create an N-by-N grid, with all sites blocked.
    //...
    public Percolation(int N) {
        if (N <= 0){
            throw new IllegalArgumentException("N must be > 0");
        }
        size = N;
        openSites = 0;
        uf = new WeightedQuickUnionUF(N * N + 2); // with top and bottom
        uf2 = new WeightedQuickUnionUF(N * N + 1); // with top
        grid = new boolean[N][N];
        // Connect first row to top and last row to bottom
        for (int i = 0; i < size; i++) {
            uf.union(0, encode(0, i)); // top row index is 0
            uf2.union(0, encode(0, i));
            uf.union(N * N + 1, encode((N - 1), i)); // last row index is size-1
        }
    }

    // Open site (row, col) if it is not open already.
    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            throw new IndexOutOfBoundsException("Row and/or col are out of bounds");
        }

        if (!isOpen(row, col)) {
            grid[row][col] = true;
            openSites++;
        }

        // if row and col are inside bounds of the array
        if (row < size && row >= 0 && col < size && col >= 0) {
            // connect if upper site is open
            if (row - 1 >= 0 && row - 1 < size && isOpen(row - 1, col)) {
                uf.union(encode(row - 1, col), encode(row, col));
                uf2.union(encode(row - 1, col), encode(row, col));
            }
            // connect if below site is open
            if (row + 1 >= 0 && row + 1 < size && isOpen(row + 1, col)) {
                uf.union(encode(row + 1, col), encode(row, col));
                uf2.union(encode(row + 1, col), encode(row, col));
            }
            // connect if right site is open
            if (col + 1 >= 0 && col + 1 < size && isOpen(row, col + 1)) {
                uf.union(encode(row, col + 1), encode(row, col));
                uf2.union(encode(row, col + 1), encode(row, col));
            }
            // connect if left site is open
            if (col - 1 >= 0 && col - 1 < size && isOpen(row, col - 1)) {
                uf.union(encode(row, col - 1), encode(row, col));
                uf2.union(encode(row, col - 1), encode(row, col));
            }
        }
    }

    // Is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            throw new IndexOutOfBoundsException("Row and/or col are out of bounds");
        }
        return grid[row][col];
    }

    // Is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            throw new IndexOutOfBoundsException("Row and/or col are out of bounds");
        }
        if (isOpen(row, col)) {
            // site (row,col) is full if uf and uf2 are connected to top
            return uf2.connected(encode(row, col), 0);
        }
        return false;
    }

    // Number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Does the system percolate?
    public boolean percolates() {
        return uf.connected(size * size + 1, 0);
    }

    // An integer ID (1...N) for site (row, col).
    private int encode(int row, int col) {
        return size * row + col + 1;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        } else {
            StdOut.println("does not percolate");
        }

        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}
