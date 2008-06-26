package hudson.plugins.coverage.model;

/**
 * Represents a line of source code.
 *
 * @author Stephen Connolly
 * @since 26-Jun-2008 07:40:36
 */
public class LineDetail {
    private final int[] branchCounts;
    private int lineCount;

    public LineDetail(int branches) {
        if (branches < 0) {
            throw new IllegalArgumentException("Number of branches cannot be negative");
        }
        this.branchCounts = branches == 0 ? null : new int[branches];
        this.lineCount = 0;
    }

    public synchronized boolean isTouched() {
        return lineCount > 0;
    }

    public synchronized boolean isCovered() {
        return getBranchCoverage() > 0.9998f;
    }

    public synchronized int getLineCount() {
        return lineCount;
    }

    public synchronized float getBranchCoverage() {
        if (branchCounts == null) {
            return 1.0f;
        }
        int coveredBranches = 0;
        for (int branchCount : branchCounts) {
            if (branchCount > 0) {
                coveredBranches++;
            }
        }
        return (1.0f * coveredBranches) / branchCounts.length;
    }

    public synchronized void touch(int hits) {
        if (branchCounts != null) {
            throw new IllegalStateException("Cannot touch an entire line with branches");
        }
        lineCount += hits;
    }

    public synchronized void touch(int branch, int hits) {
        if (branchCounts == null) {
            throw new IllegalStateException("Cannot touch a branch on a line without branches");
        }
        if (branch < 0 || branch >= branchCounts.length) {
            throw new IndexOutOfBoundsException();
        }
        branchCounts[branch] += hits;
        lineCount += hits;
    }
}
