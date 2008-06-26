package hudson.plugins.coverage.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a source code metric.
 *
 * @author Stephen Connolly
 * @since 26-Jun-2008 17:04:11
 */
public final class Metric implements Comparable<Metric> {
    // ------------------------------ FIELDS ------------------------------

    /**
     * Standard metric for line coverage.
     */
    public static final Metric LINE_COVERAGE = Metric.newMetric("line");

    /**
     * Standard metric for branch coverage.
     */
    public static final Metric BRANCH_COVERAGE = Metric.newMetric("branch");

    /**
     * The name of this metric.
     */
    private final String name;

// -------------------------- STATIC METHODS --------------------------

    /**
     * Creates a new source code metric.
     *
     * @param name The name of the metric.
     * @return The new metric.
     */
    public static Metric newMetric(String name) {
        Metric result = new Metric(name);
        SingletonHolder.ALL_METRICS.add(result);
        return result;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructor for a child element.
     *
     * @param name The name.
     */
    private Metric(String name) {
        name.getClass(); // throw NPE if null
        this.name = name;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

// ------------------------ CANONICAL METHODS ------------------------

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Metric element = (Metric) o;

        if (!name.equals(element.name)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return name.hashCode();
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface Comparable ---------------------

    /**
     * {@inheritDoc}
     */
    public int compareTo(Metric that) {
        return name.compareTo(that.name);
    }

// -------------------------- INNER CLASSES --------------------------

    /**
     * Holds the metrics collection singleton.
     */
    private static final class SingletonHolder {
// ------------------------------ FIELDS ------------------------------

        /**
         * The collection of metrics.
         */
        private static final Set<Metric> ALL_METRICS = new HashSet<Metric>();

// --------------------------- CONSTRUCTORS ---------------------------

        /**
         * Do not instantiate SingletonHolder.
         */
        private SingletonHolder() {
        }
    }
}