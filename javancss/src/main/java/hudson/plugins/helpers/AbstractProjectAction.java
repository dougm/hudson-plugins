package hudson.plugins.helpers;

import hudson.model.AbstractProject;
import hudson.model.Actionable;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 04-Feb-2008 19:42:40
 */
abstract public class AbstractProjectAction<PROJECT extends AbstractProject<?, ?>> extends Actionable {
    private final PROJECT project;

    protected AbstractProjectAction(PROJECT project) {
        this.project = project;
    }

    public PROJECT getProject() {
        return project;
    }
}
