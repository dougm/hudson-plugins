package hudson.plugins.crap4j;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.kohsuke.stapler.StaplerProxy;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class Crap4JProjectAction implements Action, StaplerProxy {
	
	private static final long serialVersionUID = -2146614418093678624L;
	private final AbstractProject<?, ?> project;
	
	public Crap4JProjectAction(AbstractProject<?, ?> project) {
		super();
		this.project = project;
	}
	
	//@Override
	public String getDisplayName() {
		return "Crap";
	}
	
	//@Override
	public String getIconFileName() {
		return "/plugin/crap4j/icons/crap-32x32.gif";
	}
	
	//@Override
	public String getUrlName() {
		return "crap";
	}
	
	//@Override
	public Object getTarget() {
		return this;
	}
	
	public AbstractProject<?, ?> getOwner() {
		return this.project;
	}
	
    public final boolean hasValidResults(final AbstractBuild<?, ?> build) {
        if (build != null) {
        	Crap4JBuildAction resultAction = build.getAction(Crap4JBuildAction.class);
            if (resultAction != null) {
                return resultAction.hasPreviousCrap();
            }
        }
        return false;
    }
	
    public void doGraph(final StaplerRequest request, final StaplerResponse response) throws IOException {
    	doTrend(request, response);
    }
	
    public Crap4JBuildAction getLastResultAction() {
    	AbstractBuild<?, ?> lastSuccessfulBuild = this.project.getLastSuccessfulBuild();
    	if (null == lastSuccessfulBuild) {
    		return null;
    	}
        Crap4JBuildAction action = lastSuccessfulBuild.getAction(Crap4JBuildAction.class);
        return action;
    }
    
    public void doTrendMap(final StaplerRequest request, final StaplerResponse response) throws IOException {
    	Crap4JBuildAction action = getLastResultAction();
    	if (action == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        action.doGraphMap(request, response);
    }
    
    public void doTrend(final StaplerRequest request, final StaplerResponse response) throws IOException {
    	Crap4JBuildAction action = getLastResultAction();
    	if (action == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        action.doGraph(request, response);
    }
}
