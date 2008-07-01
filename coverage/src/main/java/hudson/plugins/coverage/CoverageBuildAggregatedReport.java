package hudson.plugins.coverage;

import hudson.maven.MavenAggregatedReport;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.HealthReport;

import java.util.List;
import java.util.Map;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 01-Jul-2008 23:11:34
 */
public class CoverageBuildAggregatedReport extends AbstractBuildReport<MavenModuleSetBuild> implements MavenAggregatedReport {
    private HealthReport buildHealth = null;

    public CoverageBuildAggregatedReport(MavenModuleSetBuild build, Map<MavenModule, List<MavenBuild>> moduleBuilds) {
        //To change body of created methods use File | Settings | File Templates.
        super(results);
    }
}
