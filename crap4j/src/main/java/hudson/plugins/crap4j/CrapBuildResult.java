package hudson.plugins.crap4j;

import hudson.model.AbstractBuild;
import hudson.model.ModelObject;
import hudson.plugins.crap4j.display.DecreasingCrapLoadComparator;
import hudson.plugins.crap4j.model.ICrapMethodPresentation;
import hudson.plugins.crap4j.model.IMethodCrap;
import hudson.plugins.crap4j.model.ProjectCrapBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class CrapBuildResult implements ModelObject, ICrapMethodPresentation {
	
	private final ProjectCrapBean crap;
	final AbstractBuild<?, ?> owner;

	public CrapBuildResult(AbstractBuild<?, ?> owner,
			ProjectCrapBean crap) {
		super();
		this.owner = owner;
		this.crap = crap;
	}
	
	public AbstractBuild<?, ?> getOwner() {
		return this.owner;
	}
	
	public String getSummary() {
		return buildSummary();
	}
	
	public String getDetails() {
		return buildDetails();
	}
	
	@Override
	public String getDisplayName() {
		return "Crap Report";
	}
	
	@Override
	public String getTitle() {
		return "All Crappy Methods for " + getOwner().getDisplayName();
	}
	
	@Override
	public Collection<IMethodCrap> getMethods() {
		List<IMethodCrap> result = new ArrayList<IMethodCrap>();
		Collections.addAll(result, this.crap.getCrapMethods());
		Collections.sort(result, new DecreasingCrapLoadComparator());
		return result;
	}

	private String buildListEntry(String url, int count, String denotation) {
		StringBuilder result = new StringBuilder();
		result.append("<li><a href=\"");
		result.append(url);
		result.append("\">");
		result.append(count);
		result.append(" ");
		result.append(denotation);
		result.append("</a></li>");
		return result.toString();
	}
	
	public boolean hasNewCrappyMethods() {
		return (this.crap.getNewCrapMethodsCount() > 0);
	}
	
	public boolean hasFixedCrappyMethods() {
		return (this.crap.getFixedCrapMethodsCount() > 0);
	}
	
	public boolean hasChangesAtCrappyMethods() {
		return (hasNewCrappyMethods() || hasFixedCrappyMethods());
	}
	
	private String buildDetails() {
		StringBuilder result = new StringBuilder();
		if (hasNewCrappyMethods()) {
			result.append(buildListEntry("crapResult/new",
					this.crap.getNewCrapMethodsCount(),
					"new crap methods"));
		}
		if (hasFixedCrappyMethods()) {
			result.append(buildListEntry("crapResult/fixed",
					this.crap.getFixedCrapMethodsCount(),
					"fewer crap methods"));
		}
		return result.toString();
	}
	
	private String buildSummary() {
        StringBuilder result = new StringBuilder();
        result.append("Crap4J: ");
        int crapMethods = this.crap.getCrapMethodCount();
        if (0 == crapMethods) {
        	result.append("No crappy methods in this project.");
        } else {
        	result.append("<a href=\"crapResult\">");
        	result.append(crapMethods);
        	result.append(" crappy methods (");
        	result.append(this.crap.getCrapMethodPercent());
        	result.append("%)</a> out of ");
        	result.append(this.crap.getMethodCount());
        	result.append(" methods in this project.");
        }
        return result.toString();
	}
	
	public ICrapMethodPresentation getDynamic(final String link, final StaplerRequest request, final StaplerResponse response) {
        if ("new".equals(link)) {
        	return new NewCrapMethodsResult(getOwner(), this.crap.getNewMethods());
        }
        if ("fixed".equals(link)) {
        	return new FixedCrapMethodsResult(getOwner(), this.crap.getFixedMethods());
        }
        return this;
    }
}
