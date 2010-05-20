package hudson.plugins.remotequeue;

import java.io.Serializable;

import hudson.scm.SCMRevisionState;

public class RemoteQueueRevisionState extends SCMRevisionState implements Serializable {

	private boolean zipFilesQueued = false;
	
	public void setZipFilesQueued(boolean found){
		this.zipFilesQueued = found;
	}
	
	public boolean isZipFilesQueued(){
		return zipFilesQueued;
	}
	
}
