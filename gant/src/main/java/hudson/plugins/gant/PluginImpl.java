package hudson.plugins.gant;

import hudson.Plugin;
import hudson.tasks.BuildStep;

/**
 * Gant plugin entry point.
 * 
 * @author Kohsuke Kawaguchi
 * @plugin
 */
public class PluginImpl extends Plugin {
    public void start() throws Exception {
        BuildStep.BUILDERS.add(Gant.DESCRIPTOR);
    }
}
