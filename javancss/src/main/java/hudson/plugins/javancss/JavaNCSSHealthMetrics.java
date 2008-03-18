package hudson.plugins.javancss;

import hudson.model.AbstractBuild;
import hudson.plugins.helpers.health.HealthMetric;

/**
 * Created by IntelliJ IDEA.
 * User: stephen
 * Date: 18-Mar-2008
 * Time: 06:04:17
 * To change this template use File | Settings | File Templates.
 */
public enum JavaNCSSHealthMetrics implements HealthMetric {
    FANCY {public String getName() {
        return "Fancy";  //To change body of implemented methods use File | Settings | File Templates.
    }public float measure(AbstractBuild<?, ?> build) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }public float getBest() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }public float getWorst() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }},
    SIMPLE {public String getName() {
        return "Simple";  //To change body of implemented methods use File | Settings | File Templates.
    }public float measure(AbstractBuild<?, ?> build) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }public float getBest() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }public float getWorst() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }};
}
