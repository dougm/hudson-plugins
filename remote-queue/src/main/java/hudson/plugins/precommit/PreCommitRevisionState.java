package hudson.plugins.precommit;

import java.io.Serializable;

import hudson.scm.SCMRevisionState;

public class PreCommitRevisionState extends SCMRevisionState implements Serializable {

	private boolean buildNow = false;
	
	public void setBuildNow(boolean buildNow){
		this.buildNow = buildNow;
	}
	
	public boolean getBuildNow(){
		return buildNow;
	}
	
}
