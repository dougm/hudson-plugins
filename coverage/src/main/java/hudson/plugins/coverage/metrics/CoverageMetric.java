package hudson.plugins.coverage.metrics;

import java.io.Serializable;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 16-Aug-2007 07:53:25
 */
public interface CoverageMetric extends Serializable {
    /**
     * Gets the total number of items that can be covered.
     *
     * @return The number of items that could be covered.
     */
    int getTotalCount();

    /**
     * Gets the number of items that are covered.
     *
     * @return The number of items that are covered.
     */
    int getCoveredCount();

    /**
     * Gets the percentage coverage.
     *
     * @return The percentage coverage.
     */
    int getCoverage();
}
