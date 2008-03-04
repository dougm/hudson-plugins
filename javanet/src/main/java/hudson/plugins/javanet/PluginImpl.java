package hudson.plugins.javanet;

import hudson.Plugin;
import hudson.model.Jobs;

/**
 * @author Kohsuke Kawaguchi
 */
public class PluginImpl extends Plugin {
    public void start() {
        Jobs.PROPERTIES.add(StatsProperty.DESCRIPTOR);
    }
}
