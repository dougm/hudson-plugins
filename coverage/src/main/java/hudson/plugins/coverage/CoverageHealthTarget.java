package hudson.plugins.coverage;

import hudson.plugins.helpers.health.HealthTarget;
import hudson.plugins.helpers.health.HealthMetric;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 29-Jun-2008 20:53:14
 */
public class CoverageHealthTarget extends HealthTarget {
    public CoverageHealthTarget(HealthMetric metric, String healthy, String unhealthy, String unstable) {
        super(metric, healthy, unhealthy, unstable);
    }
}
