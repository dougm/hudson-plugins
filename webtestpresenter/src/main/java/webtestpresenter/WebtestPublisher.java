package webtestpresenter;

import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.DirectoryBrowserSupport;
import hudson.model.Result;
import hudson.tasks.Publisher;
import hudson.util.FormFieldValidator;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * Publishes links to canoo webtest results.
 * 
 * @see http://webtest.canoo.com
 *
 * @author Adam Ambrose
 */
public class WebtestPublisher extends Publisher implements Serializable {
    // Uncomment for debugging:
    //private static final Logger LOG = 
    //    Logger.getLogger(WebtestPublisher.class.getName());
    private static final String WEBTEST_REPORTS = "webtestReports";
	private static final long serialVersionUID = 1L;
    private final String webtestResultsSrcDir;

    WebtestPublisher(String webtestResultsSrcDir) {
        this.webtestResultsSrcDir= webtestResultsSrcDir;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getWebtestResultsSrcDir() {
        return webtestResultsSrcDir;
    }
    /**
     * Gets the directory where the latest Webtest results are stored for the
     * given project.
     */
    private static FilePath getWebtestReportDir(AbstractBuild<?,?> build) {
        //return new File(build.getArtifactsDir(), "webtestReports");
        return new FilePath(
                new File(build.getRootDir(), WEBTEST_REPORTS));
    }

    public boolean perform(AbstractBuild<?,?> build, Launcher launcher,
                           BuildListener listener) throws InterruptedException {

        // TODO: does time check need to account for running on slave?
        // see hudson.tasks.junit.JUnitResultsArchiver
        final long buildTime = build.getTimestamp().getTimeInMillis();
        
        FilePath webtestResults =
            build.getParent().getWorkspace().child(webtestResultsSrcDir);
        FilePath target = getWebtestReportDir(build);
        Action action = null;

        try {
            if (!webtestResults.exists()) {
                return true;
            }
            // Check if the results are stale.  If not, copy them over
            // anyways.
            if (build.getResult().isWorseOrEqualTo(Result.FAILURE) &&
                    buildTime + 1000 /*error margin*/ >
                    webtestResults.lastModified()) {
                listener.getLogger().println(
                        "Webtest reports are stale.  Not publishing.");
                return true;
            }

            StringBuffer msgBuf = new StringBuffer("Publishing webtest results: ");
            msgBuf.append(webtestResultsSrcDir);
            msgBuf.append(" to: ");
            msgBuf.append(target);
            listener.getLogger().println(msgBuf.toString());
            webtestResults.copyRecursiveTo("**/*", target);
            action = new WebtestReportAction(build);
        } catch (IOException e) {
            Util.displayIOException(e,listener);
            String err = "Unable to copy Webtest results from "
                + webtestResults + " to " + target;
            e.printStackTrace(listener.fatalError(err));
            build.setResult(Result.FAILURE);
        }

        build.getActions().add(action);
        return true;
    }

    public Descriptor<Publisher> getDescriptor() {
        // see Descriptor javadoc for more about what a descriptor is.
        return DESCRIPTOR;
    }

    public Action getProjectAction(AbstractBuild<?,?> build) {
        return new WebtestReportAction(build);
    }

    /**
     * Descriptor should be singleton.
     */
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    /**
     * Descriptor for {@link WebtestPublisher}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>views/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    public static final class DescriptorImpl extends Descriptor<Publisher> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */
        public static final String CONFIG_PARAM =
            "webtest_publisher.webtestResultsSrc";
        private String webtestResultsSrc;

        DescriptorImpl() {
            super(WebtestPublisher.class);
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Publish Webtest results";
        }

        public String getWebtestResultsSrc() {
            return webtestResultsSrc;
        }

        public boolean configure(StaplerRequest req) throws FormException {
            // to persist global configuration information,
            // set that to properties and call save().
            webtestResultsSrc = req.getParameter(CONFIG_PARAM);
            save();
            return super.configure(req);
        }

        /**
         * Performs on-the-fly validation on the file mask wildcard.
         */
        public void doCheck(StaplerRequest req, StaplerResponse rsp)
            throws IOException, ServletException {
            new FormFieldValidator.WorkspaceFileMask(req,rsp).process();
        }
        /**
         * Creates a new instance of {@link WebtestPublisher} from a submitted
         * form.
         */
        public WebtestPublisher newInstance(StaplerRequest req)
            throws FormException {
            // see config.jelly and you'll find "hello_world.name" form entry.
            return new WebtestPublisher(req.getParameter(CONFIG_PARAM));
        }
    }

    public static final class WebtestReportAction implements Action {
        private static final long serialVersionUID = -3578312514160132918L;
        /**
         * Project that owns this action.
         */
        public final AbstractBuild<?,?> build;

        public WebtestReportAction(AbstractBuild<?,?> build) {
            this.build = build;
        }

        public String getUrlName() {
            return "webtestResults";
        }

        public String getDisplayName() {
            return "Webtest Results";
        }

        public String getIconFileName() {
            return "clipboard.gif";
        }

        public Object getTarget() {
            if (build != null) {
                return getWebtestReportDir(build);
            }
            return null;
        }

        public void doDynamic(StaplerRequest req, StaplerResponse rsp)
            throws IOException, ServletException, InterruptedException {

            if(this.build != null) {
                new DirectoryBrowserSupport(this, "webtest")
                    .serveFile(req, rsp,
                               getWebtestReportDir(this.build),
                               "clipboard.gif", false);
            }
        }
    }
}
