package hudson.plugins.javancss;

import hudson.model.AbstractProject;
import hudson.model.Actionable;
import hudson.model.ProminentProjectAction;
import hudson.model.AbstractBuild;
import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import hudson.plugins.javancss.parser.Statistic;

import java.util.Collection;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 08-Jan-2008 22:05:48
 */
public class JavaNCSSProjectIndividualReport extends AbstractProjectReport<AbstractProject<?, ?>> implements ProminentProjectAction {
    public JavaNCSSProjectIndividualReport(AbstractProject<?, ?> project) {
        super(project);
    }

    protected Class<? extends AbstractBuildReport> getBuildActionClass() {
        return JavaNCSSBuildIndividualReport.class;
    }
}
