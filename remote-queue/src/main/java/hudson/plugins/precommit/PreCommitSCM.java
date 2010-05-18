package hudson.plugins.precommit;

import java.io.File;
import java.io.IOException;

import static hudson.scm.PollingResult.BUILD_NOW;
import static hudson.scm.PollingResult.NO_CHANGES;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import hudson.scm.ChangeLogParser;
import hudson.scm.PollingResult;
import hudson.scm.SCM;
import hudson.scm.SCMDescriptor;
import hudson.scm.SCMRevisionState;
import hudson.triggers.SCMTrigger;
import hudson.Util;

import org.apache.tools.ant.types.FileSet;
import org.kohsuke.stapler.DataBoundConstructor;


public class PreCommitSCM extends SCM {
	
	/**
	 * The base directory to use when locating files.  The queue.
	 */
	
	private final String directory;
	
	//private String timerSpec;
	
	/**
	 * Files to locate under the base directory.
	 */
	private final String files = "*.zip";

    @DataBoundConstructor
	public PreCommitSCM(String directory){
		//this.timerSpec = timerSpec;
    	System.out.println("DEBUG: const");
		this.directory = directory;
    }

	@Override
	public SCMRevisionState calcRevisionsFromBuild(AbstractBuild<?, ?> arg0,
			Launcher arg1, TaskListener arg2) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		PreCommitRevisionState preComState = new PreCommitRevisionState();
		preComState.setBuildNow(filesFound());
		System.out.println("DEBUG: calcRevisions, found: " + filesFound());
		
		System.out.println("DEBUG: calcRevisions, found: " + preComState.getBuildNow());
		return preComState;
	}

	@Override
	public boolean checkout(AbstractBuild<?, ?> arg0, Launcher arg1,
			FilePath arg2, BuildListener arg3, File arg4) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("DEBUG: checkout");
		return false;
	}

	@Override
	protected PollingResult compareRemoteRevisionWith(
			AbstractProject<?, ?> item, Launcher arg1, FilePath arg2,
			TaskListener arg3, SCMRevisionState baseline) throws IOException,
			InterruptedException {
		PreCommitRevisionState preComState = (PreCommitRevisionState)baseline;
		System.out.println("DEBUG: preComState: "+ preComState.getBuildNow());
		if (preComState.getBuildNow()){
			return BUILD_NOW;
		}
		System.out.println("DEBUG: compareRemoteRev");
		return NO_CHANGES;
	}

	@Override
	public ChangeLogParser createChangeLogParser() {
		// TODO Auto-generated method stub
		System.out.println("DEBUG: createChangeLogParser");
		return null;
	}
	
	/**
	 * Does remote queue directory exist?
	 * 
	 * @return {@code true} if the search directory exists, {@code false} if the
	 *         search directory does not exist or has not been configured
	 */
	private boolean directoryFound() {
	  return ((!directory.trim().isEmpty()) && new File(directory.trim()).isDirectory());
	}
	
	/**
	 * Search for the zip files.
	 * 
	 * @return {@code true} if at least one file was found matching this trigger's
	 *         configuration, {@code false} if none were found
	 */
	private boolean filesFound() {
	  if (directoryFound()) {
		  System.out.println("DEBUG: files: " + files);
	    FileSet fileSet = Util.createFileSet(new File(directory), files);
	    fileSet.setDefaultexcludes(false);
	    return fileSet.size() > 0;
	  }
	  return false;
	  }	
	
	@Extension
    public static final class PreCommitSCMDescriptor extends SCMDescriptor<PreCommitSCM> {
        private String tfExecutable;

        public PreCommitSCMDescriptor() {
            super(PreCommitSCM.class, null);
            load();
        }
        
        public String getDisplayName() {
            return "PreCommit";
        }        
    }


}
