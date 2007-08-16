package hudson.plugins.coverage;

import hudson.Plugin;
import hudson.tasks.BuildStep;

/**
 * Entry point of coverage plugin.
 *
 * @author Stephen Connolly
 * @since 1.0
 * @plugin
 */
public class PluginImpl extends Plugin {
    /** {@inheritDoc} */
    public void start() throws Exception {
        BuildStep.PUBLISHERS.add(CoveragePublisher.DESCRIPTOR);
    }
}
