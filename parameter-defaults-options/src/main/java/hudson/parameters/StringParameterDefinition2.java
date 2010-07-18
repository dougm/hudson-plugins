package hudson.parameters;

import javax.servlet.ServletException;

import hudson.Extension;
import hudson.model.Messages;
import hudson.model.ParameterValue;
import hudson.model.StringParameterDefinition;
import hudson.model.StringParameterValue;
import hudson.model.Descriptor.FormException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class StringParameterDefinition2 extends StringParameterDefinition {

	private boolean neverPrompt;
	private String defaultValueForManualBuild;
	private boolean useDefaultValueForManualBuild;
	
	@DataBoundConstructor
	public StringParameterDefinition2(
			String name, String defaultValue, String defaultValueForManualBuild, boolean useDefaultValueForManualBuild,
			String description, boolean neverPrompt) {
		super(name, defaultValue, description);
		this.neverPrompt = neverPrompt;
		this.defaultValueForManualBuild = defaultValueForManualBuild;
		this.useDefaultValueForManualBuild = useDefaultValueForManualBuild;
		System.out.println("defaultValueForManualBuild." + defaultValueForManualBuild);
		System.out.println("useDefaultValueForManualBuild." + useDefaultValueForManualBuild);
	}

	public boolean isNeverPrompt() {
		return neverPrompt;
	}
	public String getDefaultValueForManualBuild() {
		return defaultValueForManualBuild;
	}
	public boolean isUseDefaultValueForManualBuild() {
		return useDefaultValueForManualBuild;
	}
	
	public ParameterValue getDefaultParameterValueForManualBuild() {
		if (defaultValueForManualBuild != null) {
	        StringParameterValue v = new StringParameterValue(getName(), defaultValueForManualBuild, getDescription());
	        return v;
		} else {
			return super.getDefaultParameterValue();
		}
	}
	
    public void submit(StaplerRequest req) throws ServletException, FormException {
    	System.out.println("submit");
    }

	@Extension
    public static class DescriptorImpl extends ParameterDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.StringParameterDefinition_DisplayName() + " (with defaults options)";
        }

        @Override
        public String getHelpFile() {
            return "/help/parameter/string.html";
        }
    }

}
