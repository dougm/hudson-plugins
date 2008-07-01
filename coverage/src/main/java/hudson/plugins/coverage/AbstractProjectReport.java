package hudson.plugins.coverage;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import hudson.plugins.helpers.AbstractProjectAction;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 01-Jul-2008 23:05:13
 */
public abstract class AbstractProjectReport<T extends AbstractProject<?, ?>> extends AbstractProjectAction<T>
        implements ProminentProjectAction {

    public AbstractProjectReport(T project) {
        super(project);
    }

    /**
     * {@inheritDoc}
     */
    public String getIconFileName() {
        for (AbstractBuild<?, ?> build = getProject().getLastBuild(); build != null; build = build.getPreviousBuild()) {
            final AbstractBuildReport action = build.getAction(getBuildActionClass());
            if (action != null) {
                return PluginImpl.ICON_FILE_NAME;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayName() {
        for (AbstractBuild<?, ?> build = getProject().getLastBuild(); build != null; build = build.getPreviousBuild()) {
            final AbstractBuildReport action = build.getAction(getBuildActionClass());
            if (action != null) {
                return PluginImpl.DISPLAY_NAME;
            }
        }
        return null;
    }

    /**
     * Getter for property 'graphName'.
     *
     * @return Value for property 'graphName'.
     */
    public String getGraphName() {
        return PluginImpl.GRAPH_NAME;
    }

    /**
     * {@inheritDoc}
     */
    public String getUrlName() {
        for (AbstractBuild<?, ?> build = getProject().getLastBuild(); build != null; build = build.getPreviousBuild()) {
            final AbstractBuildReport action = build.getAction(getBuildActionClass());
            if (action != null) {
                return PluginImpl.URL;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getSearchUrl() {
        return PluginImpl.URL;
    }

    protected abstract Class<? extends AbstractBuildReport> getBuildActionClass();

}
