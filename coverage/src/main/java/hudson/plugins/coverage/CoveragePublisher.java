package hudson.plugins.coverage;

import hudson.tasks.Publisher;
import hudson.model.*;
import hudson.Launcher;

import java.io.IOException;

import org.kohsuke.stapler.StaplerRequest;

/**
 * Generic code coverage {@link hudson.tasks.Publisher}.
 *
 * @author Stephen Connolly
 * @since 1.0
 */
public class CoveragePublisher extends Publisher {
    /** {@inheritDoc} */
    public boolean perform(Build<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        build.addAction(new CoverageBuildAction());
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /** {@inheritDoc} */
    public Action getProjectAction(Project project) {
        return new CoverageProjectAction();  //To change body of implemented methods use File | Settings | File Templates.
    }

    /** {@inheritDoc} */
    public Descriptor<Publisher> getDescriptor() {
        // see Descriptor javadoc for more about what a descriptor is.
        return DESCRIPTOR;
    }

    /**
     * Descriptor should be singleton.
     */
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    /**
     * Descriptor for {@link CoveragePublisher}. Used as a singleton. The class is marked as public so that it can be
     * accessed from views.
     * <p/>
     * <p/>
     * See <tt>views/hudson/plugins/clover/CloverPublisher/*.jelly</tt> for the actual HTML fragment for the
     * configuration screen.
     */
    public static final class DescriptorImpl extends Descriptor<Publisher> {
        /** Constructs a new DescriptorImpl. */
        public DescriptorImpl() {
            super(CoveragePublisher.class);
        }

        /** {@inheritDoc} */
        public String getDisplayName() {
            return "Record code coverage";  //To change body of implemented methods use File | Settings | File Templates.
        }

        /** {@inheritDoc} */
        public Publisher newInstance(StaplerRequest req) throws FormException {
            return new CoveragePublisher();  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
