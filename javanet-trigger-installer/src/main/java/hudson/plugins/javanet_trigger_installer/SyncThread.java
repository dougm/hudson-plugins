package hudson.plugins.javanet_trigger_installer;

import hudson.model.Hudson;
import hudson.model.PeriodicWork;
import hudson.model.Project;
import hudson.plugins.javanet_trigger_installer.Task.Check;

/**
 * Runs periodically to update the project setting
 * from the actual setting in java.net.
 *
 * @author Kohsuke Kawaguchi
 */
public class SyncThread extends PeriodicWork {
    public SyncThread() {
        super("java.net SCM trigger setting sync");
    }

    protected void execute() {
        for( Project p : Hudson.getInstance().getProjects() )
            new Check(p).schedule();
    }
}
