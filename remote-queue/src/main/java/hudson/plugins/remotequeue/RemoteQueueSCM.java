package hudson.plugins.remotequeue;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

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

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Map;

public class RemoteQueueSCM extends SCM {
	
	/**
	 * The base directory to use when locating files.  The queue.
	 */
	
	public final String directory;
	
	// private String timerSpec;
	
	/**
	 * Files to locate under the base directory.
	 */
	private final String files = "*.zip";
	
	private String ziplogFilename = null;
	
	private FileSet zipFileSet = null;
	private FilePath changeZip;

    @DataBoundConstructor
	public RemoteQueueSCM(String directory){
		// this.timerSpec = timerSpec;
		this.directory = directory.trim();
    }
    
    @Override
    public void buildEnvVars(AbstractBuild<?,?> build, Map<String, String> env){
        super.buildEnvVars(build, env);
        env.put("directory", directory);
    }

	@Override
	public SCMRevisionState calcRevisionsFromBuild(AbstractBuild<?, ?> arg0,
			Launcher arg1, TaskListener arg2) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		RemoteQueueRevisionState preComState = new RemoteQueueRevisionState();
		// if we get here, the change has been unzipped and the workspace updated.
		preComState.setWorkspaceUpdated(filesFound());
		return preComState;
	}

	@Override
	public boolean checkout(AbstractBuild<?, ?> build, Launcher launcher,
			FilePath zipFilePath, BuildListener listener, File zipFileLog) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
        PrintStream log = listener.getLogger();
        ziplogFilename = zipFileLog.getAbsolutePath();
        
        // copy and unzip the file
        FilePath path = build.getWorkspace();
        FilePath targetZip = new FilePath(build.getWorkspace(), changeZip.getName());
        try{
            changeZip.copyTo(targetZip);
            changeZip.delete();
        	
        } catch (IOException ie){
        	System.out.println("IO problem moving the zip to the workspace.");
        	ie.printStackTrace();
        }
        
		return true;
	}

	@Override
	protected PollingResult compareRemoteRevisionWith(
			AbstractProject<?, ?> item, Launcher launcher, FilePath filePath,
			TaskListener taskListener, SCMRevisionState baseline) throws IOException,
			InterruptedException {
		RemoteQueueRevisionState preComState = (RemoteQueueRevisionState)baseline;
		// System.out.println("DEBUG: preComState: "+ preComState.getBuildNow());
		preComState.setWorkspaceUpdated(filesFound());
		if (preComState.isWorkspaceUpdated()){
			// preComState.setBuildNow(false);
			changeZip = changeZip();
			return BUILD_NOW;
		}
		return NO_CHANGES;
	}

	@Override
	public ChangeLogParser createChangeLogParser() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Does remote queue directory exist?
	 * 
	 * @return {@code true} if the search directory exists, {@code false} if the
	 *         search directory does not exist or has not been configured
	 */
	private boolean directoryFound() {
	  return ((!directory.isEmpty()) && new File(directory.trim()).isDirectory());
	}
	
	/**
	 * Search for the zip files.
	 * 
	 * @return {@code true} if at least one file was found matching this trigger's
	 *         configuration, {@code false} if none were found
	 */
	private boolean filesFound() {
	  if (directoryFound()) {
	    zipFileSet = Util.createFileSet(new File(directory), files);
	    zipFileSet.setDefaultexcludes(false);
	    
	    return zipFileSet.size() > 0;
	  }
	  return false;
	  }
	
	/**
	 * Return the latest zip file.
	 * 
	 * @return FilePath
	 *         
	 */	
	private FilePath changeZip(){
		//FilePath changeZip = null;
		
	    DirectoryScanner directoryScanner = zipFileSet.getDirectoryScanner();
	    String[] files = directoryScanner.getIncludedFiles();
	    FilePath queueDir = new FilePath(new File(directory));
		
		return queueDir.child(files[0]);
	}
	
	@Extension
    public static final class PreCommitSCMDescriptor extends SCMDescriptor<RemoteQueueSCM> {
        private String tfExecutable;

        public PreCommitSCMDescriptor() {
            super(RemoteQueueSCM.class, null);
            load();
        }
        
        public String getDisplayName() {
            return "Remote Queue";
        }        
    }


}
