package hudson.plugins.jprt;

import hudson.Plugin;
import hudson.model.Jobs;
import hudson.model.Items;

/**
 * @author Kohsuke Kawaguchi
 * @plugin
 */
public class PluginImpl extends Plugin {
    public void start() throws Exception {
        Items.LIST.add(JPRTJob.DESCRIPTOR);
    }
}
