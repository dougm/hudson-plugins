package hudson.plugins.javanet;

import hudson.model.Action;
import hudson.model.AbstractProject;

/**
 * @author Kohsuke Kawaguchi
 */
public class JavaNetStatsAction implements Action {
    /**
     * Project that owns this action.
     */
    public final AbstractProject<?,?> project;

    /**
     * Java.net project name.
     */
    private final String projectName;

    public JavaNetStatsAction(AbstractProject<?, ?> project, String projectName) {
        this.project = project;
        this.projectName = projectName;
    }

    public String getIconFileName() {
        return "a.png";
    }

    public String getDisplayName() {
        return "java.net statistics";
    }

    public String getUrlName() {
        return "java.net-stats";
    }
}
