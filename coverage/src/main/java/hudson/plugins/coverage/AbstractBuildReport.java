package hudson.plugins.coverage;

import hudson.model.AbstractBuild;
import hudson.model.HealthReportingAction;
import hudson.plugins.coverage.model.Instance;
import hudson.plugins.coverage.model.Measurement;
import hudson.plugins.coverage.model.Metric;
import hudson.plugins.helpers.AbstractBuildAction;
import hudson.plugins.helpers.GraphHelper;
import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Map;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 01-Jul-2008 23:04:59
 */
public abstract class AbstractBuildReport<T extends AbstractBuild<?, ?>> extends AbstractBuildAction<T> implements HealthReportingAction {
    private final Map<Metric, Measurement> results;
    private transient WeakReference<Instance> detailedResults = null;

    public AbstractBuildReport(Map<Metric, Measurement> results) {
        this.results = results;
    }

    public Map<Metric, Measurement> getResults() {
        return results;
    }

    public String toSummary() {
        return "";
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
     * Generates the graph that shows the coverage trend up to this report.
     */
    public void doGraph(StaplerRequest req, StaplerResponse rsp) throws IOException {
        if (GraphHelper.isGraphUnsupported()) {
            GraphHelper.redirectWhenGraphUnsupported(rsp, req);
            return;
        }

        Calendar t = getBuild().getTimestamp();

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
        AbstractBuild<?, ?> build = getBuild();
        // in order to have a graph, we must have at least two points.
        int numPoints = 0;
        while (numPoints < 2) {
            if (build == null) {
                return false;
            }
            if (build.getAction(getClass()) != null) {
                numPoints++;
            }
            build = build.getPreviousBuild();
        }
        return true;
    }

    protected void populateDataSetBuilder(DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> dataset) {
        for (AbstractBuild<?, ?> build = getBuild(); build != null; build = build.getPreviousBuild()) {
            ChartUtil.NumberOnlyBuildLabel label = new ChartUtil.NumberOnlyBuildLabel(build);
            AbstractBuildReport<T> action = build.getAction(getClass());
            if (action != null) {
                for (Map.Entry<Metric, Measurement> entry : action.getResults().entrySet()) {
                    dataset.add(entry.getValue().getPercentValue(), entry.getKey().getName(), label);
                }
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
