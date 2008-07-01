package hudson.plugins.coverage;

import hudson.maven.MavenModuleSet;
import hudson.model.ProminentProjectAction;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 01-Jul-2008 23:13:15
 */
public class CoverageProjectAggregatedReport extends AbstractProjectReport<MavenModuleSet> implements ProminentProjectAction {
    public CoverageProjectAggregatedReport(MavenModuleSet project) {
        super(project);
    }

    protected Class<? extends AbstractBuildReport> getBuildActionClass() {
        return CoverageBuildAggregatedReport.class;
    }
}
