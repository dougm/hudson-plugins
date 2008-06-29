package hudson.plugins.coverage;

import hudson.tasks.Publisher;
import hudson.model.*;
import hudson.Launcher;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.plugins.helpers.AbstractPublisherImpl;
import hudson.plugins.helpers.health.HealthMetric;

import java.io.IOException;

import org.kohsuke.stapler.StaplerRequest;

/**
 * Generic code coverage {@link hudson.tasks.Publisher}.
 *
 * @author Stephen Connolly
 * @since 1.0
 */
public class CoveragePublisher extends AbstractPublisherImpl {

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
     * Descriptor for {@link CoveragePublisher}.
     */
    public static final class DescriptorImpl extends Descriptor<Publisher> {
        /** Constructs a new DescriptorImpl. */
        public DescriptorImpl() {
            super(CoveragePublisher.class);
        }

        /** {@inheritDoc} */
        public String getDisplayName() {
            return "Record code coverage";
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return !MavenModuleSet.class.isAssignableFrom(aClass)
                    && !MavenModule.class.isAssignableFrom(aClass);
        }

        public HealthMetric[] getMetrics() {
            return CoverageHealthMetrics.values();
        }
    }
}
