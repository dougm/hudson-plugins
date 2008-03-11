package hudson.plugins.javancss;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.plugins.helpers.BuildProxy;
import hudson.plugins.helpers.Ghostwriter;
import hudson.plugins.javancss.parser.Statistic;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 08-Jan-2008 23:16:52
 */
public class JavaNCSSGhostwriter
        implements Ghostwriter,
        Ghostwriter.MasterGhostwriter,
        Ghostwriter.SlaveGhostwriter {

    private final String reportFilenamePattern;

    public JavaNCSSGhostwriter(String reportFilenamePattern) {
        this.reportFilenamePattern = reportFilenamePattern;
    }

    public boolean performFromMaster(AbstractBuild<?, ?> build, FilePath executionRoot, BuildListener listener) throws InterruptedException, IOException {
        return true;
    }

    public boolean performFromSlave(BuildProxy build, BuildListener listener) throws InterruptedException, IOException {
        FilePath[] paths = build.getExecutionRootDir().list(reportFilenamePattern);
        Collection<Statistic> results = null;
        for (FilePath path : paths) {
            try {
                final File inFile = new File(path.getRemote());
                listener.getLogger().println("Parsing " + inFile);
                Collection<Statistic> result = Statistic.parse(inFile);
                listener.getLogger().println("Pre Results = " + results);
                listener.getLogger().println("Result = " + result);
                if (results == null) {
                    results = result;
                } else {
                    results = Statistic.merge(results, result);
                }
                listener.getLogger().println("Post Results = " + results);
            } catch (XmlPullParserException e) {
                e.printStackTrace(listener.getLogger());
            }
        }
        if (results != null) {
            JavaNCSSBuildIndividualReport action = new JavaNCSSBuildIndividualReport(results);
            build.getActions().add(action);
        }
        return true;
    }
}
