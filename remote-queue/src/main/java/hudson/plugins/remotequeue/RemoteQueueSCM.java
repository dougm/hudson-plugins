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
import hudson.plugins.perforce.PerforceChangeLogParser;
import hudson.plugins.perforce.PerforcePasswordEncryptor;
import hudson.plugins.perforce.PerforceRepositoryBrowser;
import hudson.plugins.perforce.PerforceSCM;
import hudson.plugins.perforce.PerforceTagAction;
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
	

	public final String p4User;
	public final String p4Passwd;
	public final String p4Port;
	public final String p4Client;
	public final String projectPath;
	public final String projectOptions;
	public final String p4Label;
	public final String p4Counter;
	
	public final String lineEndValue;
	public final String p4Charset;
	public final String p4CommandCharset;
	public final boolean updateCounterValue;
	public final boolean forceSync;
	public final boolean alwaysForceSync;
	public final boolean updateView;
	public final boolean disableAutoSync;
	public final boolean wipeBeforeBuild;
	public final boolean dontUpdateClient;
	public final boolean exposeP4Passwd;
    public final String slaveClientNameFormat;
    public final int firstChange;
    public final PerforceRepositoryBrowser browser;	

    public final String p4Exe;
    public final String p4SysDrive;
    public final String p4SysRoot;
    
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
	public RemoteQueueSCM(String p4User, 
			String p4Passwd, 
			String p4Client,
			String p4Port,
			String projectPath,
			String projectOptions,
			String p4Exe,
			String p4SysRoot,
			String p4SysDrive,
			String p4Label,
			String p4Counter,
			String lineEndValue,
			String p4Charset,
			String p4CommandCharset,
            boolean updateCounterValue,
            boolean forceSync,
            boolean alwaysForceSync,
            boolean updateView,
            boolean disableAutoSync,
            boolean wipeBeforeBuild,
            boolean dontUpdateClient,
            boolean exposeP4Passwd,
            String slaveClientNameFormat,
            int firstChange,
            PerforceRepositoryBrowser browser,			
			String directory){
		// this.timerSpec = timerSpec;
    	
    	this.p4User = p4User.trim();
    	this.p4Passwd = p4Passwd.trim();
    	this.p4Port = p4Port.trim();
    	this.p4Client = p4Client.trim();
    	this.projectPath = projectPath.trim();
    	this.projectOptions = projectOptions.trim();
    	this.p4Label = p4Label.trim();
    	this.p4Counter = p4Counter.trim();
    	
    	this.lineEndValue = lineEndValue.trim();
    	this.p4Charset = p4Charset.trim();
    	this.p4CommandCharset = p4CommandCharset.trim();
    	this.updateCounterValue = updateCounterValue;
    	this.forceSync = forceSync;
    	this.alwaysForceSync = alwaysForceSync;
    	this.updateView = updateView;
    	this.disableAutoSync = disableAutoSync;
    	this.wipeBeforeBuild = wipeBeforeBuild;
    	this.dontUpdateClient = dontUpdateClient;
    	this.exposeP4Passwd = exposeP4Passwd;
    	this.slaveClientNameFormat = slaveClientNameFormat.trim();
    	this.firstChange = firstChange;
    	this.browser = browser ;	

    	this.p4Exe = p4Exe.trim();
    	this.p4SysDrive = p4SysDrive.trim();
    	this.p4SysRoot = p4SysRoot.trim();
         	
		this.directory = directory.trim();
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
	public boolean checkout(AbstractBuild build, Launcher launcher,
			FilePath filePath, BuildListener listener, File zipFileLog) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		
		
		PerforceSCM perforceScm = new PerforceSCM(
                p4User,
                p4Passwd,
                p4Client,
                p4Port,
                projectPath,
                projectOptions,
                p4Exe,
                p4SysRoot,
                p4SysDrive,
                p4Label,
                p4Counter,
                lineEndValue,
                p4Charset,
                p4CommandCharset,
                updateCounterValue,
                forceSync,
                alwaysForceSync,
                updateView,
                disableAutoSync,
                wipeBeforeBuild,
                dontUpdateClient,
                exposeP4Passwd,
                slaveClientNameFormat,
                firstChange,
                browser/*,
                String viewMask,
                boolean useViewMaskForPolling,
                boolean useViewMaskForSyncing*/			
				
		);
		
		perforceScm.setWipeBeforeBuild(true);
		// set up the workspace
		perforceScm.checkout(build, launcher, filePath, listener, zipFileLog);

	//	super.checkout(build, launcher, filePath, listener, zipFileLog);
        PrintStream log = listener.getLogger();
        ziplogFilename = zipFileLog.getAbsolutePath();
		// boolean result = super.checkout(build, launcher, filePath, listener, zipFileLog);
        
        // copy and unzip the file
        FilePath path = build.getWorkspace();
        FilePath targetZip = new FilePath(build.getWorkspace(), changeZip.getName());
        try{
        	
            changeZip.copyTo(targetZip);
            changeZip.delete();
            targetZip.unzip(build.getWorkspace());
        	
        } catch (IOException ie){
        	System.out.println("IO problem moving or unzipping to the workspace.");
        	//ie.printStackTrace();
        	throw (ie);
        } catch (InterruptedException ie){
        	System.out.println("IO problem moving or unzipping to the workspace.");
        	throw (ie);
        }  
        
		return true;
	}

	@Override
	protected PollingResult compareRemoteRevisionWith(
			AbstractProject<?, ?> item, Launcher launcher, FilePath filePath,
			TaskListener taskListener, SCMRevisionState baseline) throws IOException,
			InterruptedException {
		RemoteQueueRevisionState preComState = (RemoteQueueRevisionState)baseline;

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
		return new PerforceChangeLogParser();
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
    public static final class RemoteQueueSCMDescriptor extends SCMDescriptor<RemoteQueueSCM> {
        private String tfExecutable;

        public RemoteQueueSCMDescriptor() {
            super(RemoteQueueSCM.class, null);
            load();
        }
        
        public String getDisplayName() {
            return "Remote Queue";
        }        
    }


}
