package hudson.plugins.jprt;

import hudson.Plugin;
import hudson.model.Jobs;
import hudson.model.Job;
import hudson.tasks.BuildStep;

/**
 * @author Kohsuke Kawaguchi
 */
public class PluginImpl extends Plugin {
    public void start() throws Exception {
        Jobs.JOBS.add(JPRTJob.DESCRIPTOR);
    }
}
