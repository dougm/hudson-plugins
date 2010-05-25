package hudson.plugins.remotequeue;

import java.io.Serializable;

import hudson.scm.SCMRevisionState;

public class RemoteQueueRevisionState extends SCMRevisionState implements Serializable {

	private boolean workspaceUpdated = false;
	
	public void setWorkspaceUpdated(boolean found){
		this.workspaceUpdated = found;
	}
	
	public boolean isWorkspaceUpdated(){
		return workspaceUpdated;
	}
	
}
