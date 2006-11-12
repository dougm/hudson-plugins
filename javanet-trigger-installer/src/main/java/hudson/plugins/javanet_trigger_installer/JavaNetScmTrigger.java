package hudson.plugins.javanet_trigger_installer;

import hudson.triggers.Trigger;
import hudson.model.Descriptor;
import hudson.model.Project;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.jnt.JavaNet;
import org.kohsuke.jnt.ProcessingException;

/**
 * {@link Trigger} for java.net CVS change notification e-mail.
 *
 * @author Kohsuke Kawaguchi
 */
public class JavaNetScmTrigger extends Trigger {

    public Descriptor<Trigger> getDescriptor() {
        return DESCRIPTOR;
    }

    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public static final class DescriptorImpl extends Descriptor<Trigger> {
        public DescriptorImpl() {
            super(JavaNetScmTrigger.class);
        }

        public String getDisplayName() {
            return "Monitor change in java.net CVS/SVN repository";
        }

        public Trigger newInstance(StaplerRequest staplerRequest) throws FormException {
            return new JavaNetScmTrigger();
        }
    }
}
