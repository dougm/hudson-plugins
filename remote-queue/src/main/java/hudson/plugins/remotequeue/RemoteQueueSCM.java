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
	
	private final String directory;
	
	// private String timerSpec;
	
	/**
	 * Files to locate under the base directory.
	 */
	private final String files = "*.zip";
	
	private String ziplogFilename = null;
	
	private FileSet zipFileSet = null;

    @DataBoundConstructor
	public RemoteQueueSCM(String directory){
		// this.timerSpec = timerSpec;
    	System.out.println("DEBUG: const");
		this.directory = directory;
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
		preComState.setBuildNow(filesFound());
		System.out.println("DEBUG: calcRevisions, found: " + filesFound());
		
		System.out.println("DEBUG: calcRevisions, found: " + preComState.getBuildNow());
		return preComState;
	}

	@Override
	public boolean checkout(AbstractBuild<?, ?> build, Launcher launcher,
			FilePath filePath, BuildListener listener, File zipFileLog) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("DEBUG: checkout");
        PrintStream log = listener.getLogger();
        ziplogFilename = zipFileLog.getAbsolutePath();
        
        // copy and unzip the file
        FilePath path = build.getWorkspace();
        
        System.out.println("DEBUG: zipFileLog, path: " + path);
        
		return false;
	}

	@Override
	protected PollingResult compareRemoteRevisionWith(
			AbstractProject<?, ?> item, Launcher launcher, FilePath filePath,
			TaskListener taskListener, SCMRevisionState baseline) throws IOException,
			InterruptedException {
		System.out.println("DEBUG: compareRemote: found? : " + filesFound());
		RemoteQueueRevisionState preComState = (RemoteQueueRevisionState)baseline;
		// System.out.println("DEBUG: preComState: "+ preComState.getBuildNow());
		if (preComState.getBuildNow()){
			preComState.setBuildNow(false);
			System.out.println("DEBUG: filesFound: " + filesFound());			
			FilePath changeZip = changeZip();
			System.out.println("DEBUG: changeZip name: " + changeZip.getName());
			//changeZip.copyTo();
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
	//    for (int i = 0; i < files.length; i++) {
	//      System.out.println("DEBUG: files: " + files[i]);
	//    }
	    FilePath changeZip = new FilePath(new File(files[0]));
		
		return changeZip;
	}
	
	@Extension
    public static final class PreCommitSCMDescriptor extends SCMDescriptor<RemoteQueueSCM> {
        private String tfExecutable;

        public PreCommitSCMDescriptor() {
            super(RemoteQueueSCM.class, null);
            load();
        }
        
        public String getDisplayName() {
            return "PreCommit";
        }        
    }


}
