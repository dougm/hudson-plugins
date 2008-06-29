package hudson.plugins.coverage.model;

import hudson.plugins.coverage.model.measurements.BranchCoverage;
import hudson.plugins.coverage.model.measurements.LineCoverage;

import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

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

    /**
     * Returns all the metrics.
     *
     * @return a read-only copy of all the metrics.
     */
    public static Set<Metric> values() {
        return Collections.unmodifiableSet(new HashSet<Metric>(SingletonHolder.ALL_METRICS));
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
     * Getter for property 'clazz'.
     *
     * @return Value for property 'clazz'.
     */
    public Class<? extends Measurement> getClazz() {
        return clazz;
    }

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

        return name.equals(element.name);
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

// -------------------------- OTHER METHODS --------------------------

    /**
     * Ensure that instances are deserialized correctly.
     *
     * @return The deserialized instance.
     * @throws ObjectStreamException never.
     */
    private Object readResolve() throws ObjectStreamException {
        for (Metric alternatives : SingletonHolder.ALL_METRICS) {
            if (alternatives.name.equals(name)) {
                return alternatives;
            }
        }
        SingletonHolder.ALL_METRICS.add(this);
        return this;
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
        private static final Set<Metric> ALL_METRICS = new CopyOnWriteArraySet<Metric>();

// --------------------------- CONSTRUCTORS ---------------------------

        /**
         * Do not instantiate SingletonHolder.
         */
        private SingletonHolder() {
        }
    }
}