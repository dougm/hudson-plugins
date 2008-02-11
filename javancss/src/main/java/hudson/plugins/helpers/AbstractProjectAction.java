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

    /**
     * Getter for property 'project'.
     *
     * @return Value for property 'project'.
     */
    public PROJECT getProject() {
        return project;
    }

    /**
     * Override to control when the floating box should be displayed.
     *
     * @return <code>true</code> if the floating box should be visible.
     */
    public boolean isFloatingBoxActive() {
        return true;
    }

    /**
     * Override to control when the action displays a trend graph.
     *
     * @return <code>true</code> if the action should show a trend graph.
     */
    public boolean isGraphActive() {
        return false;
    }

}
