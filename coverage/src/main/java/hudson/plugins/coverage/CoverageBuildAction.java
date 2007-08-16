package hudson.plugins.coverage;

import hudson.model.HealthReportingAction;
import hudson.model.HealthReport;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 16-Aug-2007 16:58:15
 */
public class CoverageBuildAction implements HealthReportingAction {
    public HealthReport getBuildHealth() {
        return new HealthReport(55, "Coverage is not done yet!");  
    }

    public String getIconFileName() {
        return "graph.gif";
    }

    public String getDisplayName() {
        return "Coverage report";
    }

    public String getUrlName() {
        return "coverage";
    }
}
