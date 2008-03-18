package hudson.plugins.javancss;

import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Descriptor;
import hudson.plugins.helpers.AbstractPublisherImpl;
import hudson.plugins.helpers.Ghostwriter;
import hudson.plugins.helpers.health.HealthMetric;
import hudson.plugins.helpers.health.HealthTarget;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.util.Arrays;
import java.util.List;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 08-Jan-2008 21:24:06
 */
public class JavaNCSSPublisher extends AbstractPublisherImpl {

    private String reportFilenamePattern;
    private HealthTarget[] targets;

    @DataBoundConstructor
    public JavaNCSSPublisher(String reportFilenamePattern, HealthTarget[] targets) {
        reportFilenamePattern.getClass();
        this.reportFilenamePattern = reportFilenamePattern;
        this.targets = targets;
    }

    public String getReportFilenamePattern() {
        return reportFilenamePattern;
    }

    public HealthTarget[] getTargets() {
        return targets;
    }

    /**
     * {@inheritDoc}
     */
    public boolean needsToRunAfterFinalized() {
        return false;
    }

    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    /**
     * {@inheritDoc}
     */
    public Descriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
    }

    /**
     * {@inheritDoc}
     */
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new JavaNCSSProjectIndividualReport(project);
    }

    protected Ghostwriter newGhostwriter() {
        return new JavaNCSSGhostwriter(reportFilenamePattern);
    }

    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        private static final HealthMetric[] HEALTH_METRIC = new HealthMetric[]{
                new HealthMetric() {
                    public String getName() {
                        return "Fancy";
                    }

                    public float measure(AbstractBuild<?, ?> build) {
                        return 0;
                    }

                    public float getBest() {
                        return 10;
                    }

                    public float getWorst() {
                        return 0;
                    }
                },
                new HealthMetric() {
                    public String getName() {
                        return "Simple";
                    }

                    public float measure(AbstractBuild<?, ?> build) {
                        return 0;
                    }

                    public float getBest() {
                        return 10;
                    }

                    public float getWorst() {
                        return 0;
                    }
                }
        };

        /**
         * Do not instantiate DescriptorImpl.
         */
        private DescriptorImpl() {
            super(JavaNCSSPublisher.class);
        }

        /**
         * {@inheritDoc}
         */
        public String getDisplayName() {
            return PluginImpl.DISPLAY_NAME;
        }

        public Publisher newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            return req.bindJSON(JavaNCSSPublisher.class, formData);
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return !MavenModuleSet.class.isAssignableFrom(aClass)
                    && !MavenModule.class.isAssignableFrom(aClass);
        }

        public HealthTarget[] getTargets(JavaNCSSPublisher instance) {
//            if (instance == null) {
            return new HealthTarget[]{new HealthTarget(getMetrics().iterator().next(), "55", "0", null)};
            //           }
            //           return instance.getTargets();
        }

        public List<HealthMetric> getMetrics() {
            return Arrays.asList(HEALTH_METRIC);
        }
    }

}
