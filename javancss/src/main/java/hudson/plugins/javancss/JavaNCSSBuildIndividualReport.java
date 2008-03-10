package hudson.plugins.javancss;

import hudson.maven.*;
import hudson.model.AbstractBuild;
import hudson.model.HealthReport;
import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import hudson.plugins.javancss.parser.Statistic;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 08-Jan-2008 21:15:05
 */
public class JavaNCSSBuildIndividualReport extends AbstractBuildReport<AbstractBuild<?, ?>> implements AggregatableAction {

    public JavaNCSSBuildIndividualReport(Collection<Statistic> results) {
        super(results);
    }

    /**
     * Write-once setter for property 'build'.
     *
     * @param build The value to set the build to.
     */
    @Override
    public synchronized void setBuild(AbstractBuild<?, ?> build) {
        super.setBuild(build);
        if (this.getBuild() != null) {
            for (Statistic r : getResults()) {
                r.setOwner(this.getBuild());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public MavenAggregatedReport createAggregatedAction(MavenModuleSetBuild build, Map<MavenModule, List<MavenBuild>> moduleBuilds) {
        return new JavaNCSSBuildAggregatedReport(build, moduleBuilds);
    }

    /**
     * {@inheritDoc}
     */
    public HealthReport getBuildHealth() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
