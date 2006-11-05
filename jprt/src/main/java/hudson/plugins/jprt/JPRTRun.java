package hudson.plugins.jprt;

import hudson.model.Run;
import hudson.model.Result;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.net.URL;

import JPRT.shared.transported.status.JobStatus;
import JPRT.shared.transported.status.BuildTargetStatus;
import JPRT.shared.transported.JobID;
import JPRT.shared.transported.StateID;
import JPRT.shared.JobLayout;

/**
 * {@link Run} for {@link JPRTJob}.
 *
 * @author Kohsuke Kawaguchi
 */
public class JPRTRun extends Run<JPRTJob,JPRTRun> {
    private final File archiveDir;

    /**
     * JPRT's data model that represents the state of this job.
     * Lazily loaded.
     */
    private volatile JobStatus jprtStatus;

    private final JobID jprtId;

    public JPRTRun(JPRTJob job, JPRTRun prevBuild, File archiveDir) throws ParseException {
        super(job, getTimestamp(archiveDir));
        // TODO: think about a way to give a consistent build number
        this.number = prevBuild!=null ? prevBuild.number+1 : 1;
        this.archiveDir = archiveDir;

        jprtId = JobID.fromString(archiveDir.getName());
        jprtStatus = JobStatus.fromXml(jprtId.getXmlFile());

        switch (jprtStatus.getState().id().getPhase()) {
        case NOT_STARTED:
            // hasn't started yet
            break;
        case FAIL:
            onStartBuilding();
            setResult(Result.FAILURE);
            onEndBuilding();
            break;
        case PASS:
            // success
            onStartBuilding();
            setResult(Result.SUCCESS);
            onEndBuilding();
            break;
        case WORKING:
            onStartBuilding();
            break;
        default:
            throw new IllegalStateException(jprtStatus.getState().toString());
        }
    }

    // TODO: we need another constructor for jobs in the queue

    /**
     * Returns the Job ID in the JPRT sense. The archive directory name
     * is the job ID.
     */
    @Override
    public String getId() {
        return jprtId.toString();
    }

    @Override
    public String getDisplayName() {
        return jprtId.toString();
    }

    /**
     * Use the JPRT ID as the URL.
     */
    @Override
    public String getUrl() {
        return project.getUrl()+getId()+'/';
    }


    /**
     * Gets the JPRT Job status.
     */
    public JobStatus getStatus() {
        return jprtStatus;
    }

    /**
     * Helper code used in view to turn {@link StateID} into the appropriate color ball icon.
     */
    public String _getStatusUrl(StateID state) {
        switch (state.id().getPhase()) {
        case NOT_STARTED:
            return "grey.gif";
        case FAIL:
            return "red.gif";
        case PASS:
            return "blue.gif";
        case WORKING:
            return "grey_anime.gif";
        default:
            throw new IllegalStateException(state.toString());
        }
    }

    /**
     * Computes the log file URL of the given {@link BuildTargetStatus}.
     */
    public URL getBuildLogURL(BuildTargetStatus status) throws IOException {
        getParent().prepareToTalkToJPRT();
        String logPath = new JobLayout(jprtStatus.getJobID()).buildLog(status.getBuildTargetID());
        return new URL(jprtStatus.jobUrl(logPath));
    }

    /**
     * Computes the timestamp of the build from the JPRT archive directory name format.
     */
    private static Calendar getTimestamp(File dir) throws ParseException {
        String name = dir.getName();
        name = name.substring(0,17);
        synchronized(TIME_FORMATTER) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(TIME_FORMATTER.parse(name));
            return cal;
        }
    }

    protected static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
}
