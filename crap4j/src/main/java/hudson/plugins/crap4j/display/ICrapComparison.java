package hudson.plugins.crap4j.display;

import hudson.plugins.crap4j.model.IMethodCrap;

public interface ICrapComparison {
	
	public IMethodCrap[] getNewCrapMethods();
	
	public IMethodCrap[] getFixedCrapMethods();
	
	public IMethodCrap[] getUnchangedCrapMethods();
}
