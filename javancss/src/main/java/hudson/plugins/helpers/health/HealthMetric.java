package hudson.plugins.helpers.health;

import hudson.model.AbstractBuild;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: stephen
 * Date: 17-Mar-2008
 * Time: 12:43:47
 * To change this template use File | Settings | File Templates.
 */
public interface HealthMetric extends Serializable {
    String getName();

    float measure(AbstractBuild<?, ?> build);

    float getBest();

    float getWorst();
}
