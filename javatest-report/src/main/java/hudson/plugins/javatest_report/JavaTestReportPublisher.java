/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://jwsdp.dev.java.net/CDDLv1.0.html
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://jwsdp.dev.java.net/CDDLv1.0.html  If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 */
package hudson.plugins.javatest_report;

import hudson.Launcher;
import hudson.model.Action;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Project;
import hudson.model.Result;
import hudson.tasks.Publisher;
import org.kohsuke.stapler.StaplerRequest;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.Copy;

import java.io.File;

/**
 * @author Rama Pulavarthi
 */

public class JavaTestReportPublisher extends Publisher {
    private final String includes;
    private final String jtwork;

    public JavaTestReportPublisher(String includes,String jtwork) {
        this.includes = includes;
        this.jtwork = jtwork;
    }

    /**
     * Ant "&lt;fileset @includes="..." /> pattern to specify SQE XML files
     */
    public String getIncludes() {
        return includes;
    }

    /**
     * TCK work directory so that JTR files can be accessed.
     */
    public String getJtwork() {
        return jtwork;
    }

    public boolean perform(Build build, Launcher launcher, BuildListener listener) {
        archiveJTWork(build,listener);
        FileSet fs = new FileSet();
        org.apache.tools.ant.Project p = new org.apache.tools.ant.Project();
        fs.setProject(p);
        fs.setDir(build.getProject().getWorkspace().getLocal());
        fs.setIncludes(includes);
        DirectoryScanner ds = fs.getDirectoryScanner(p);

        if(ds.getIncludedFiles().length==0) {
            listener.getLogger().println("No Java test report files were found. Configuration error?");
            // no test result. Most likely a configuration error or fatal problem
            build.setResult(Result.FAILURE);
        }

        JavaTestAction action = new JavaTestAction(build, ds, listener);
        build.getActions().add(action);

        Report r = action.getResult();

        if(r.getTotalCount()==0) {
            listener.getLogger().println("Test reports were found but none of them are new. Did tests run?");
            // no test result. Most likely a configuration error or fatal problem
            build.setResult(Result.FAILURE);
        }

        if(r.getFailCount()>0)
            build.setResult(Result.UNSTABLE);

        return true;
     }

    private void archiveJTWork(Build owner, BuildListener listener) {
        if (jtwork == null || jtwork.equals("")) {
            listener.getLogger().println("Set Java Test Work directory for better reporting");
        } else {
            hudson.model.Project p = owner.getProject();
            Copy copyTask = new Copy();
            copyTask.setProject(new org.apache.tools.ant.Project());
            File dir = new File(owner.getArtifactsDir(), "java-test-work");
            dir.mkdirs();
            copyTask.setTodir(dir);
            FileSet src = new FileSet();
            src.setDir(new File(p.getWorkspace().getLocal(), jtwork));
            copyTask.addFileset(src);
            copyTask.execute();
        }
    }

    public Descriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
    }

     public static final Descriptor<Publisher> DESCRIPTOR = new Descriptor<Publisher>(JavaTestReportPublisher.class) {
        public String getDisplayName() {
            return "Publish JavaTest result report";
        }

        public String getHelpFile() {
            return "/plugin/javatest-report/help.html";
        }

        public Publisher newInstance(StaplerRequest req) {
            return new JavaTestReportPublisher(req.getParameter("javatest_includes"), req.getParameter("javatest_jtwork"));
        }
    };
}
