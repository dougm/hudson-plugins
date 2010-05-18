package hudson.plugins.remotequeue;

import java.io.Serializable;

import hudson.scm.SCMRevisionState;

public class RemoteQueueRevisionState extends SCMRevisionState implements Serializable {

	private boolean buildNow = false;
	
	public void setBuildNow(boolean buildNow){
		this.buildNow = buildNow;
	}
	
	public boolean getBuildNow(){
		return buildNow;
	}
	
}
