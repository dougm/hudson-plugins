package hudson.plugins.coverage;

import hudson.maven.*;
import hudson.model.AbstractBuild;
import hudson.model.HealthReport;

import java.util.List;
import java.util.Map;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 29-Jun-2008 21:00:10
 */
public class CoverageBuildIndividualReport extends AbstractBuildReport<AbstractBuild<?, ?>>
        implements AggregatableAction {

    private HealthReport healthReport;

    public CoverageBuildIndividualReport() {
        super(results);
    }

    public MavenAggregatedReport createAggregatedAction(MavenModuleSetBuild build, Map<MavenModule, List<MavenBuild>> moduleBuilds) {
        return new CoverageBuildAggregatedReport(build, moduleBuilds);
    }

    /**
     * {@inheritDoc}
     */
    public HealthReport getBuildHealth() {
        return healthReport;
    }

    public void setBuildHealth(HealthReport healthReport) {
        this.healthReport = healthReport;
    }
}
