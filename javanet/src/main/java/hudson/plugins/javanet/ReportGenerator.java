package hudson.plugins.javanet;

import hudson.util.DaemonThreadFactory;
import org.jvnet.its.Generator;
import org.kohsuke.jnt.JavaNet;
import org.kohsuke.jnt.ProcessingException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

/**
 * {@link Runnable} for generating the java.net issue-tracker stats reports.
 * 
 * @author Kohsuke Kawaguchi
 */
class ReportGenerator implements Runnable {
    private final String projectName;
    private final File reportDir;

    ReportGenerator(String projectName, File reportDir) {
        this.projectName = projectName;
        this.reportDir = reportDir;
    }

    /**
     * Submits this job for execution later.
     */
    public Future<?> schedule() {
        return EXECUTOR.submit(this);
    }

    public void run() {
        Generator gen = new Generator();

        long start = System.currentTimeMillis();
        LOGGER.info("Starting to generate java.net issue tracker stats for "+projectName);
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName("java.net issue tracker stat generation for "+projectName);
        reportDir.mkdirs();
        try {
            gen.generate(JavaNet.connect().getProject(projectName), reportDir);
        } catch (ProcessingException e) {
            LOGGER.log(Level.SEVERE, "Failed to generate java.net stat report for "+projectName,e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to generate java.net stat report for "+projectName,e);
        } finally {
            Thread.currentThread().setName(oldName);
        }

        LOGGER.info(MessageFormat.format("Completed generating java.net issue tracker stats for {0} in {1} ms", projectName, System.currentTimeMillis() - start));
    }

    /**
     * Used to generate reports.
     */
    static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(1, new DaemonThreadFactory());

    private static final Logger LOGGER = Logger.getLogger(ReportGenerator.class.getName());
}
