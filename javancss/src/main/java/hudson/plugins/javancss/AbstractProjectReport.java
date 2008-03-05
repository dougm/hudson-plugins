package hudson.plugins.javancss;

import hudson.model.*;
import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import hudson.plugins.helpers.GraphHelper;
import hudson.plugins.helpers.AbstractProjectAction;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.io.IOException;
import java.util.Calendar;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 09-Jan-2008 21:22:45
 */
public abstract class AbstractProjectReport<T extends AbstractProject<?, ?>> extends AbstractProjectAction<T> implements ProminentProjectAction {

    public AbstractProjectReport(T project) {
        super(project);
    }

    /**
     * {@inheritDoc}
     */
    public String getIconFileName() {
        return PluginImpl.ICON_FILE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayName() {
        return PluginImpl.DISPLAY_NAME;
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
        return PluginImpl.URL;
    }

    /**
     * {@inheritDoc}
     */
    public String getSearchUrl() {
        return PluginImpl.URL;
    }

    /**
     * Generates the graph that shows the coverage trend up to this report.
     */
    public void doGraph(StaplerRequest req, StaplerResponse rsp) throws IOException {
        if (GraphHelper.isGraphUnsupported()) {
            GraphHelper.redirectWhenGraphUnsupported(rsp, req);
            return;
        }

        Calendar t = getProject().getLastBuild().getTimestamp();

        if (req.checkIfModified(t, rsp)) {
            return; // up to date
        }

        DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> dataSetBuilder =
                new DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel>();

        populateDataSetBuilder(dataSetBuilder);

        ChartUtil.generateGraph(req, rsp, GraphHelper.buildChart(dataSetBuilder.build()), getGraphWidth(), getGraphHeight());
    }

    /**
     * Returns <code>true</code> if there is a graph to plot.
     *
     * @return Value for property 'graphAvailable'.
     */
    public boolean isGraphActive() {
        AbstractBuild<?, ?> build = getProject().getLastBuild();
        // in order to have a graph, we must have at least two points.
        int numPoints = 0;
        while (numPoints < 2) {
            if (build == null) {
                return false;
            }
            if (build.getAction(getBuildActionClass()) != null) {
                numPoints++;
            }
            build = build.getPreviousBuild();
        }
        return true;
    }

    protected abstract Class<? extends AbstractBuildReport> getBuildActionClass();

    protected void populateDataSetBuilder(DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> dataset) {

        for (AbstractBuild<?, ?> build = getProject().getLastBuild(); build != null; build = build.getPreviousBuild()) {
            ChartUtil.NumberOnlyBuildLabel label = new ChartUtil.NumberOnlyBuildLabel(build);
            AbstractBuildReport action = build.getAction(getBuildActionClass());
            if (action != null) {
                dataset.add(action.getTotals().getNcss(), "NCSS", label);
                dataset.add(action.getTotals().getSingleCommentLines(), "Multi-line comment line count", label);
                dataset.add(action.getTotals().getMultiCommentLines(), "Single line comment line count", label);
                dataset.add(action.getTotals().getJavadocLines(), "Javadoc line count", label);
            }
        }

    }

    /**
     * Getter for property 'graphWidth'.
     *
     * @return Value for property 'graphWidth'.
     */
    public int getGraphWidth() {
        return 500;
    }

    /**
     * Getter for property 'graphHeight'.
     *
     * @return Value for property 'graphHeight'.
     */
    public int getGraphHeight() {
        return 200;
    }

}
