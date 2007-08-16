package hudson.plugins.coverage;

import hudson.model.Action;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 16-Aug-2007 17:36:56
 */
public class CoverageProjectAction implements Action {
    public String getIconFileName() {
        return "graph.gif";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getDisplayName() {
        return "Coverage report";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getUrlName() {
        return "coverage";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
