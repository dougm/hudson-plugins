package hudson.plugins.coverage.model;

import hudson.plugins.coverage.model.measurements.BranchCoverage;
import hudson.plugins.coverage.model.measurements.LineCoverage;

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
    public static final Metric LINE_COVERAGE = Metric.newMetric("line", LineCoverage.class);

    /**
     * Standard metric for branch coverage.
     */
    public static final Metric BRANCH_COVERAGE = Metric.newMetric("branch", BranchCoverage.class);

    /**
     * The name of this metric.
     */
    private final String name;

    /**
     * The type of measurement.
     */
    private final Class<? extends Measurement> clazz;

// -------------------------- STATIC METHODS --------------------------

    /**
     * Creates a new source code metric.
     *
     * @param name  The name of the metric.
     * @param clazz The measurement class.
     * @return The new metric.
     */
    public static Metric newMetric(String name, Class<? extends Measurement> clazz) {
        Metric result = new Metric(name, clazz);
        SingletonHolder.ALL_METRICS.add(result);
        return result;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructor for a child element.
     *
     * @param name  The name.
     * @param clazz The measurement class.
     */
    private Metric(String name, Class<? extends Measurement> clazz) {
        name.getClass(); // throw NPE if null
        clazz.getClass();
        this.name = name;
        this.clazz = clazz;
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

    /**
     * Getter for property 'clazz'.
     *
     * @return Value for property 'clazz'.
     */
    public Class<? extends Measurement> getClazz() {
        return clazz;
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