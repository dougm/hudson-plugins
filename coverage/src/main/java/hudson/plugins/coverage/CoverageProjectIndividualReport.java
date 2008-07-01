package hudson.plugins.coverage;

import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 01-Jul-2008 23:13:31
 */
public class CoverageProjectIndividualReport extends AbstractProjectReport<AbstractProject<?, ?>> implements ProminentProjectAction {
    public CoverageProjectIndividualReport(AbstractProject<?, ?> project) {
        super(project);
    }

    protected Class<? extends AbstractBuildReport> getBuildActionClass() {
        return CoverageBuildIndividualReport.class;
    }
}
