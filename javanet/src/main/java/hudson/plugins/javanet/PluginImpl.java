package hudson.plugins.javanet;

import hudson.Plugin;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.Jobs;
import hudson.model.listeners.ItemListener;
import hudson.triggers.SafeTimerTask;
import hudson.triggers.Trigger;

/**
 * @author Kohsuke Kawaguchi
 */
public class PluginImpl extends Plugin {
    public void start() {
        Jobs.PROPERTIES.add(StatsProperty.DESCRIPTOR);

        Hudson.getInstance().getJobListeners().add(new ItemListener() {
            public void onLoaded() {
                Trigger.timer.scheduleAtFixedRate(new SafeTimerTask() {
                    protected void doRun() {
                        for(AbstractProject<?,?> j : Hudson.getInstance().getAllItems(AbstractProject.class)) {
                            StatsProperty p = j.getProperty(StatsProperty.class);
                            if(p==null) continue;
                            JavaNetStatsAction a = p.getJobAction(j);
                            if(a==null)  continue;

                            a.upToDateCheck();
                        }
                    }
                },0*MINUTE,3*HOUR);
            }
        });
    }

    static final long MINUTE = 1000*60;
    static final long HOUR   = 24*60*MINUTE;
    static final long DAY   = 24*HOUR;
}
