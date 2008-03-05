package hudson.plugins.javancss;

import hudson.model.Actionable;
import hudson.model.ProminentProjectAction;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import hudson.plugins.javancss.parser.Statistic;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 08-Jan-2008 22:06:11
 */
public class JavaNCSSProjectAggregatedReport extends AbstractProjectReport<MavenModuleSet> implements ProminentProjectAction {
    public JavaNCSSProjectAggregatedReport(MavenModuleSet project) {
        super(project);
    }

    protected Class<? extends AbstractBuildReport> getBuildActionClass() {
        return JavaNCSSBuildAggregatedReport.class;
    }
}
