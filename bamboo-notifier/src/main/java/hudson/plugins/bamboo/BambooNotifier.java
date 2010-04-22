package hudson.plugins.bamboo;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;

import java.io.IOException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Asgeir Storesund Nilsen
 *
 */
public class BambooNotifier extends Notifier {

    public final String jobName;
    public final String serverAddress;
    public final String username;
    public final String password;
    public final boolean triggerUnstable;

    @DataBoundConstructor
    public BambooNotifier(String jobName, String serverAddress,
            String username, String password, boolean triggerUnstable) {
        if (serverAddress.endsWith("/"))
            this.serverAddress = serverAddress;
        else
            this.serverAddress = serverAddress + "/";
        this.jobName = jobName;
        this.username = username;
        this.password = password;
        this.triggerUnstable = triggerUnstable;
    }

    /*
     * (non-Javadoc)
     *
     * @see hudson.tasks.BuildStep#getRequiredMonitorService()
     */
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * hudson.tasks.BuildStepCompatibilityLayer#perform(hudson.model.AbstractBuild
     * , hudson.Launcher, hudson.model.BuildListener)
     */
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
            BuildListener listener) throws InterruptedException, IOException {
        if (build.getResult() == Result.SUCCESS)
	    triggerBamboo(listener);
        else if (triggerUnstable && build.getResult() == Result.UNSTABLE) {
	    listener.getLogger().println("Triggering unstable build");
	    triggerBamboo(listener);
        }
        return true;
    }

    void triggerBamboo(BuildListener listener) {
        String url = serverAddress + "rest/api/latest/queue/" + jobName
                + "?os_authType=basic";
	listener.getLogger().printf("Triggering Bamboo job %s%n", url);
        PostMethod postMethod = new PostMethod(url);
        HttpClient client = new HttpClient();
        Credentials credentials = new UsernamePasswordCredentials(username,
                password);
        client.getState().setCredentials(AuthScope.ANY, credentials);
        try {
            int status = client.executeMethod(postMethod);
	    listener.getLogger().printf("Response code: %d%n", status);
        } catch (HttpException e) {
	    e.printStackTrace(listener.error("Unable to notify Bamboo URL %s",
		    url));
        } catch (IOException e) {
	    e.printStackTrace(listener.error(
		    "Unable to connect to Bamboo URL %s", url));
        } finally {
            postMethod.releaseConnection();
        }
    }

    @Extension
    public static final class DescriptorImpl extends
            BuildStepDescriptor<Publisher> {

        /*
         * (non-Javadoc)
         *
         * @see hudson.tasks.BuildStepDescriptor#isApplicable(java.lang.Class)
         */
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        /*
         * (non-Javadoc)
         *
         * @see hudson.model.Descriptor#getDisplayName()
         */
        @Override
        public String getDisplayName() {
            return "Bamboo Notifier";
        }

    }
}
