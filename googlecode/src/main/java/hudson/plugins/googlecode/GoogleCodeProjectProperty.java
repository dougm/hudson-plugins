package hudson.plugins.googlecode;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.scm.SubversionChangeLogSet.LogEntry;

import org.kohsuke.stapler.StaplerRequest;

/**
 * Property for {@link AbstractProject} that stores the associated Google Code website URL.
 *
 * @author Kohsuke Kawaguchi
 * @author Erik Ramfelt
 */
public final class GoogleCodeProjectProperty extends JobProperty<AbstractProject<?,?>> {

    /**
     * Google Code website URL that this project uses.
     *
     * This value is normalized and therefore it always ends with '/'.
     * Null if this is not configured yet.
     */
    public final String googlecodeWebsite;

    /**
     * @stapler-constructor
     */
    public GoogleCodeProjectProperty(String googlecodeWebsite) {
        // normalize
        if(googlecodeWebsite==null || googlecodeWebsite.length()==0)
            googlecodeWebsite=null;
        else {
            if(!googlecodeWebsite.endsWith("/"))
                googlecodeWebsite += '/';
        }
        this.googlecodeWebsite = googlecodeWebsite;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return PluginImpl.PROJECT_PROPERTY_DESCRIPTOR;
    }

    public static final class DescriptorImpl extends JobPropertyDescriptor implements GoogleCodeProjectProperty.PropertyRetriever{

        public DescriptorImpl() {
            super(GoogleCodeProjectProperty.class);
            load();
        }

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return AbstractProject.class.isAssignableFrom(jobType);
        }

        @Override
        public String getDisplayName() {
            return "Associated Google Code website";
        }

        @Override
        public JobProperty<?> newInstance(StaplerRequest req) throws FormException {
            GoogleCodeProjectProperty tpp = req.bindParameters(GoogleCodeProjectProperty.class, "googlecode.");
            if(tpp.googlecodeWebsite==null)
                tpp = null; // not configured
            return tpp;
        }

        public GoogleCodeProjectProperty getProperty(AbstractBuild<?, ?> build) {
            return build.getProject().getProperty(GoogleCodeProjectProperty.class);
        }

        public GoogleCodeProjectProperty getProperty(LogEntry entry) {
            return getProperty(entry.getParent().build);
        }
    }

    /**
     * Interface needed to remove the dependency of AbstractBuild and Project classes which
     * are quite difficult to mock out.
     */
    public interface PropertyRetriever {
        GoogleCodeProjectProperty getProperty(AbstractBuild<?,?> build);
        GoogleCodeProjectProperty getProperty(LogEntry entry);
    }
}
