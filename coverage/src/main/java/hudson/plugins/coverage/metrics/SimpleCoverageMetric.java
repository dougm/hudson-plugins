package hudson.plugins.coverage.metrics;

/**
 * A simple measure of coverage.
 *
 * @author Stephen Connolly
 * @since 16-Aug-2007 07:59:19
 */
public class SimpleCoverageMetric extends AbstractCoverageMetric{
    private final int total;
    private final int covered;


    /**
     * Create a new SimpleCoverageMetric
     *
     * @param covered the number of items that are covered.
     * @param total the number of items that could be covered.
     */
    public SimpleCoverageMetric(int covered, int total) {
        if (total < 0) {
            total = 0;
        }
        if (covered < 0) {
            covered = 0;
        }
        if (covered > total) {
            covered = total;
        }
        this.total = total;
        this.covered = covered;
    }

    /** {@inheritDoc} */
    public int getTotalCount() {
        return total;
    }

    /** {@inheritDoc} */
    public int getCoveredCount() {
        return covered;
    }
}
