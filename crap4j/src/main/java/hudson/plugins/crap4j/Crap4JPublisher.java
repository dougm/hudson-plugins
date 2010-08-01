package hudson.plugins.crap4j;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.plugins.crap4j.calculation.HealthBuilder;
import hudson.plugins.crap4j.model.CrapReportMerger;
import hudson.plugins.crap4j.model.IMethodCrap;
import hudson.plugins.crap4j.model.MethodCrapBean;
import hudson.plugins.crap4j.model.ProjectCrapBean;
import hudson.plugins.crap4j.util.FoundFile;
import hudson.plugins.crap4j.util.ReportFilesFinder;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.schneide.crap4j.reader.ReportReader;
import com.schneide.crap4j.reader.model.ICrapReport;
import com.schneide.crap4j.reader.model.IMethodCrapData;
import hudson.Extension;
import hudson.matrix.MatrixProject;
import hudson.model.FreeStyleProject;
import hudson.tasks.Recorder;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class Crap4JPublisher extends Recorder {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(Crap4JPublisher.class.getName());

    @Extension
    public static final Crap4JPluginDescriptor DESCRIPTOR = new Crap4JPluginDescriptor();

	private String reportPattern;
	private String healthThreshold;

	/**
	 * @param reportPattern
	 */
        @DataBoundConstructor
	public Crap4JPublisher(String reportPattern,
			String healthThreshold) {
		super();
		this.reportPattern = reportPattern;
		this.healthThreshold = healthThreshold;
	}

	private HealthBuilder getHealthBuilderFor(String healthThreshold) {
		if ((null == healthThreshold) || (healthThreshold.isEmpty())) {
			return DESCRIPTOR.getHealthBuilder();
		}
		try {
			return new HealthBuilder(Double.parseDouble(healthThreshold));
		} catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Could not parse health threshold representation to a number: " + healthThreshold, e);
			return DESCRIPTOR.getHealthBuilder();
		} catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Not a valid health threshold: " + healthThreshold, e);
			return DESCRIPTOR.getHealthBuilder();
		}
	}

	@Override
	public BuildStepDescriptor<Publisher> getDescriptor() {
		return DESCRIPTOR;
	}

	@Override
	public Action getProjectAction(AbstractProject<?, ?> project) {
		return new Crap4JProjectAction(project);
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.BUILD;
	}

    protected void log(final PrintStream logger, final String message) {
        logger.println("[CRAP4J] " + message);
    }

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
        PrintStream logger = listener.getLogger();
        log(logger, "Collecting Crap4J analysis files...");

        log(logger, "Searching for report files within " + this.reportPattern);
        log(logger, "Using the new FileSetBuilder");
        
        ReportFilesFinder finder = new ReportFilesFinder(this.reportPattern);
        FoundFile[] reports = build.getWorkspace().act(finder);
        if (0 == reports.length) {
            log(logger, "No crap4j report files were found. Configuration error?");
            return false;
        }
        ProjectCrapBean previousCrap = getPreviousProjectCrapBean(build);
        ProjectCrapBean reportBean = createCurrentProjectCrapBean(logger,
				reports, previousCrap);
        build.getActions().add(new Crap4JBuildAction(
        		build,
        		new CrapBuildResult(build, reportBean),
        		getHealthBuilderFor(this.healthThreshold)));
		return true;
	}

	private ProjectCrapBean getPreviousProjectCrapBean(AbstractBuild<?, ?> build) {
        CrapBuildResult previousResult = CrapBuildResult.getPrevious(build);
        if (null != previousResult) {
        	return previousResult.getResultData();
        }
		return null;
	}

	private ProjectCrapBean createCurrentProjectCrapBean(PrintStream logger,
			FoundFile[] reports, ProjectCrapBean previousCrap)
			throws UnsupportedEncodingException, IOException {
		ProjectCrapBean[] currentBeans = loadProjectCrapBeans(logger, reports, previousCrap);
		if (1 == currentBeans.length) {
			return currentBeans[0];
		}
		CrapReportMerger merger = new CrapReportMerger();
		return merger.mergeReports(previousCrap, currentBeans);
	}
	
	private ProjectCrapBean[] loadProjectCrapBeans(PrintStream logger,
			FoundFile[] reports, ProjectCrapBean previousCrap)
			throws UnsupportedEncodingException, IOException {
		List<ProjectCrapBean> result = new ArrayList<ProjectCrapBean>();
		for (FoundFile currentReportFile : reports) {
			Reader reportReader = new BufferedReader(
	        		new InputStreamReader(
	        				currentReportFile.getFile().read(),
	        				currentReportFile.getEncoding()));
	        ReportReader parser = new ReportReader(reportReader);
	        ICrapReport report = parser.parseData();
	        ProjectCrapBean reportBean = new ProjectCrapBean(
	        		previousCrap,
	        		report.getStatistics(),
	        		convertToMethodCrap(report.getDetails().getMethodCrapManager().getCrapData()));
	        log(logger, "Got a report bean with " + reportBean.getCrapMethodCount() + " crap methods out of " + reportBean.getMethodCount() + " methods.");
	        result.add(reportBean);
		}
		return result.toArray(new ProjectCrapBean[result.size()]);
	}

	private IMethodCrap[] convertToMethodCrap(Collection<IMethodCrapData> crapData) {
		IMethodCrap[] result = new IMethodCrap[crapData.size()];
		Iterator<IMethodCrapData> dataIterator = crapData.iterator();
		int index = 0;
		while (dataIterator.hasNext()) {
			result[index] = new MethodCrapBean(dataIterator.next());
			index++;
		}
		return result;
	}

	public String getReportPattern() {
            return this.reportPattern;
	}

	public String getHealthThreshold() {
            return this.healthThreshold;
	}

    public void setHealthThreshold(String healthThreshold) {
        this.healthThreshold = healthThreshold;
    }

    public void setReportPattern(String reportPattern) {
        this.reportPattern = reportPattern;
    }

    public static final class Crap4JPluginDescriptor extends BuildStepDescriptor<Publisher> {
	public static final String ACTION_ICON_PATH = "/plugin/crap4j/icons/crap-32x32.png";

	private HealthBuilder healthBuilder;

	Crap4JPluginDescriptor() {
            super(Crap4JPublisher.class);
            this.healthBuilder = new HealthBuilder();
	}

	public HealthBuilder getHealthBuilder() {
            return this.healthBuilder;
	}

	@Override
	public String getDisplayName() {
            return "Report Crap";
	}

	@Override
	public Crap4JPublisher newInstance(StaplerRequest req, JSONObject object) throws FormException {
            Crap4JPublisher instance = req.bindParameters(Crap4JPublisher.class, "crap4j.");
            return instance;
	}

	@Override
	public boolean configure(StaplerRequest req, JSONObject object) throws FormException {
            try {
        	double healthThreshold = Double.parseDouble(req.getParameter("crap4j.healthThreshold"));
        	this.healthBuilder = new HealthBuilder(healthThreshold);
            } catch (NumberFormatException e) {
                throw new FormException("health threshold field must be a positive Double",
                        "crap4j.healthThreshold");
            } catch (IllegalArgumentException e) {
                throw new FormException("health threshold field must be a positive Double",
                        "crap4j.healthThreshold");
            }
            save();
            return super.configure(req, object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return (FreeStyleProject.class.isAssignableFrom(jobType) || MatrixProject.class.isAssignableFrom(jobType));
	}
    }
}
