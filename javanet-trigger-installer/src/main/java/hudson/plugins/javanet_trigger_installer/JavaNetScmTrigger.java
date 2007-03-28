package hudson.plugins.javanet_trigger_installer;

import hudson.model.Item;
import hudson.model.SCMedItem;
import hudson.plugins.javanet_trigger_installer.Task.Update;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * {@link Trigger} for java.net CVS change notification e-mail.
 *
 * @author Kohsuke Kawaguchi
 */
public class JavaNetScmTrigger extends Trigger<SCMedItem> {

    public TriggerDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public void start(SCMedItem project, boolean newInstance) {
        super.start(project, newInstance);
        if(newInstance)
            new Update(project.asProject()).scheduleHighPriority();
    }

    @Override
    public void stop() {
        super.stop();
        new Update(job.asProject()).scheduleHighPriority();
    }

    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public static final class DescriptorImpl extends TriggerDescriptor {
        public DescriptorImpl() {
            super(JavaNetScmTrigger.class);
        }

        public boolean isApplicable(Item item) {
            return item instanceof SCMedItem;
        }

        public String getDisplayName() {
            return "Monitor changes in java.net CVS/SVN repository";
        }

        public String getHelpFile() {
            return "/plugin/javanet-trigger-installer/help.html";
        }

        public Trigger newInstance(StaplerRequest staplerRequest) throws FormException {
            return new JavaNetScmTrigger();
        }
    }
}
