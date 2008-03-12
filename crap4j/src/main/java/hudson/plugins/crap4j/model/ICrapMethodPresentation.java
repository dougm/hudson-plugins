package hudson.plugins.crap4j.model;

import hudson.model.AbstractBuild;

import java.util.Collection;

public interface ICrapMethodPresentation {
	
	public AbstractBuild<?, ?> getOwner();

	public String getTitle();
	
	public Collection<IMethodCrap> getMethods();
}
