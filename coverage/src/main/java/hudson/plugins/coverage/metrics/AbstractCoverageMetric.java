package hudson.plugins.coverage.metrics;

/**
 * Abstract coverage metric, providing common implementations and utility methods.
 *
 * @author Stephen Connolly
 * @since 16-Aug-2007 07:53:34
 */
public abstract class AbstractCoverageMetric implements CoverageMetric {

    /** {@inheritDoc} */
    public int getCoverage() {
        final int total = getTotalCount();
        final int covered = getCoveredCount();

        return (total == 0 || covered == total) ? 100 : ((covered * 100) / total);
    }

    /** {@inheritDoc} */
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getCoverage());
        buf.append("% (");
        buf.append(getCoveredCount());
        buf.append('/');
        buf.append(getTotalCount());
        buf.append(')');
        return buf.toString();
    }
}
