package hudson.plugins.javancss;

import hudson.maven.*;
import hudson.model.Action;
import hudson.model.HealthReportingAction;
import hudson.model.HealthReport;
import hudson.model.AbstractBuild;
import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import hudson.plugins.javancss.parser.Statistic;

import java.util.*;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 08-Jan-2008 21:12:12
 */
public class JavaNCSSBuildAggregatedReport extends AbstractBuildReport<MavenModuleSetBuild> implements MavenAggregatedReport {
    private HealthReport buildHealth = null;

    public JavaNCSSBuildAggregatedReport(MavenModuleSetBuild build, Map<MavenModule, List<MavenBuild>> moduleBuilds) {
        super(new ArrayList<Statistic>());
        setBuild(build);
        Map<MavenModule, List<MavenBuild>> processedModuleBuilds = new HashMap<MavenModule, List<MavenBuild>>();
        for (Map.Entry<MavenModule, List<MavenBuild>> childList : moduleBuilds.entrySet()) {
            List<MavenBuild> processedChildren = new ArrayList<MavenBuild>();
            processedModuleBuilds.put(childList.getKey(), processedChildren);
            for (MavenBuild child : childList.getValue()) {
                update(processedModuleBuilds, child);
                processedChildren.add(child);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void update(Map<MavenModule, List<MavenBuild>> moduleBuilds, MavenBuild newBuild) {
        JavaNCSSBuildIndividualReport report = newBuild.getAction(JavaNCSSBuildIndividualReport.class);
        if (report != null) {
            Collection<Statistic> u = Statistic.merge(report.getResults(), getResults());
            getResults().clear();
            getResults().addAll(u);
            buildHealth = HealthReport.min(buildHealth, report.getBuildHealth());
        }
    }

    /**
     * {@inheritDoc}
     */
    public Class<? extends AggregatableAction> getIndividualActionType() {
        return JavaNCSSBuildIndividualReport.class;
    }

    /**
     * {@inheritDoc}
     */
    public Action getProjectAction(MavenModuleSet moduleSet) {
        return new JavaNCSSProjectAggregatedReport(moduleSet);
    }

    /**
     * {@inheritDoc}
     */
    public HealthReport getBuildHealth() {
        return buildHealth;
    }

    protected void populateDataSetBuilder(DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> dataset) {
        for (MavenModuleSetBuild build = getBuild(); build != null; build = build.getPreviousBuild()) {
            ChartUtil.NumberOnlyBuildLabel label = new ChartUtil.NumberOnlyBuildLabel(build);

            JavaNCSSBuildAggregatedReport action = build.getAction(JavaNCSSBuildAggregatedReport.class);
            if (action != null) {
                dataset.add(Statistic.total(action.getResults()).getNcss(), "NCSS", label);
            }
        }
    }
}
