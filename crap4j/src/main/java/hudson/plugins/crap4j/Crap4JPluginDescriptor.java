package hudson.plugins.crap4j;

import hudson.maven.AbstractMavenProject;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

import org.kohsuke.stapler.StaplerRequest;

public class Crap4JPluginDescriptor extends BuildStepDescriptor<Publisher> {
	
	public static final String ACTION_ICON_PATH = "/plugin/crap4j/icons/crap-32x32.png";
	
	public Crap4JPluginDescriptor() {
		super(Crap4JPublisher.class);
	}
	
	@Override
	public String getDisplayName() {
		return "Report Crap";
	}
	
	@Override
	public Crap4JPublisher newInstance(StaplerRequest req) throws FormException {
        return req.bindParameters(Crap4JPublisher.class, "crap_");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return (!AbstractMavenProject.class.isAssignableFrom(jobType));
	}
}
