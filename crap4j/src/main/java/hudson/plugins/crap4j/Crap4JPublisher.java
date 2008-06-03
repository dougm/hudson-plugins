package hudson.plugins.crap4j;

import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.plugins.crap4j.model.ProjectCrapBean;
import hudson.plugins.crap4j.util.FoundFile;
import hudson.plugins.crap4j.util.ReportFilesFinder;
import hudson.tasks.Publisher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

import com.schneide.crap4j.reader.ReportReader;
import com.schneide.crap4j.reader.model.ICrapReport;

public class Crap4JPublisher extends Publisher {
	
	public static final Descriptor<Publisher> DESCRIPTOR = new Crap4JPluginDescriptor();
	
	private final String reportPattern;
	
	/**
	 * @param reportPattern
	 * @stapler-constructor
	 */
	public Crap4JPublisher(String reportPattern) {
		super();
		this.reportPattern = reportPattern;
	}
	
	//@Override
	public Descriptor<Publisher> getDescriptor() {
		return DESCRIPTOR;
	}
	
	@Override
	public Action getProjectAction(AbstractProject<?, ?> project) {
		return new Crap4JProjectAction(project);
	}
	
	@Override
	public boolean perform(Build<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
        PrintStream logger = listener.getLogger();
        logger.println("Collecting Crap4J analysis files...");
        
        ReportFilesFinder finder = new ReportFilesFinder(this.reportPattern);
        FoundFile[] reports = build.getProject().getWorkspace().act(finder);
        if (0 == reports.length) {
            logger.println("No crap4j report files were found. Configuration error?");
            return false;
        }
        Reader reportReader = new BufferedReader(new FileReader(reports[0].getFile())); 
        ReportReader parser = new ReportReader(reportReader);
        ICrapReport report = parser.parseData();
        
        ProjectCrapBean previousCrap = null;
        CrapBuildResult previousResult = CrapBuildResult.getPrevious(build);
        if (null != previousResult) {
        	previousCrap = previousResult.getResultData();
        }
        ProjectCrapBean reportBean = new ProjectCrapBean(
        		previousCrap,
        		report.getStatistics(),
        		report.getDetails().getMethodCrapManager().getAllCrapData());
        logger.println("Got a report bean with " + reportBean.getCrapMethodCount() + " crap methods out of " + reportBean.getMethodCount() + " methods.");
        build.getActions().add(new Crap4JBuildAction(build, new CrapBuildResult(build, reportBean)));
		logger.println("Hell yeah, i got my crap published!");
		return true;
	}

	public String getReportPattern() {
		return this.reportPattern;
	}
}
