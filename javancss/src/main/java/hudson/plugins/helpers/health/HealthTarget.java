package hudson.plugins.helpers.health;

import hudson.model.AbstractBuild;
import hudson.model.HealthReport;
import hudson.model.Result;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created by IntelliJ IDEA.
 * User: stephen
 * Date: 17-Mar-2008
 * Time: 12:44:28
 * To change this template use File | Settings | File Templates.
 */
public abstract class HealthTarget<M extends HealthMetric> {
    private final M healthMetric;
    private final Float healthy;
    private final Float unhealthy;
    private final Float unstable;

    @DataBoundConstructor
    public HealthTarget(M healthMetric, String healthy, String unhealthy, String unstable) {
        this.healthMetric = healthMetric;
        this.healthy = safeParse(healthy);
        this.unhealthy = safeParse(unhealthy);
        this.unstable = safeParse(unstable);
    }

    private static Float safeParse(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Float.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    public M getMetric() {
        return healthMetric;
    }

    public Float getHealthy() {
        return healthy;
    }

    public Float getUnhealthy() {
        return unhealthy;
    }

    public Float getUnstable() {
        return unstable;
    }

    public HealthReport evaluate(AbstractBuild<?, ?> build) {
        float result = healthMetric.measure(build);
        float healthy = this.healthy == null ? healthMetric.getBest() : this.healthy;
        float unhealthy = this.unhealthy == null ? healthMetric.getWorst() : this.unhealthy;
        if (unstable != null) {
            if ((healthy > unhealthy && result < unstable) || (healthy < unhealthy && result > unstable)) {
                if (Result.UNSTABLE.isWorseThan(build.getResult())) {
                    build.setResult(Result.UNSTABLE);
                }
            }
        }
        return new HealthReport((int) ((result - unhealthy) / (healthy - unhealthy) * 100), healthMetric.getName());
    }
}
