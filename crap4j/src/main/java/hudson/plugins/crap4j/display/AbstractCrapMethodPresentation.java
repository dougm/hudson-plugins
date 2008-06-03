/**
 * 
 */
package hudson.plugins.crap4j.display;

import hudson.model.AbstractBuild;
import hudson.model.ModelObject;
import hudson.plugins.crap4j.model.ICrapMethodPresentation;
import hudson.plugins.crap4j.model.IMethodCrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCrapMethodPresentation implements ICrapMethodPresentation, ModelObject {

	private final String title;
	private final AbstractBuild<?, ?> owner;

	public AbstractCrapMethodPresentation(AbstractBuild<?, ?> owner,
			String title) {
		super();
		this.owner = owner;
		this.title = title;
	}
	
	public AbstractBuild<?, ?> getOwner() {
		return this.owner;
	}
	
	//@Override
	public String getTitle() {
		return this.title;
	}
	
	//@Override
	public String getDisplayName() {
		return getTitle();
	}
	
	//@Override
	public Collection<IMethodCrap> getMethods() {
		List<IMethodCrap> result = new ArrayList<IMethodCrap>();
		Collections.addAll(result, loadMethods());
		Collections.sort(result, new DecreasingCrapLoadComparator());
		return result;
	}
	
	protected abstract IMethodCrap[] loadMethods();
}