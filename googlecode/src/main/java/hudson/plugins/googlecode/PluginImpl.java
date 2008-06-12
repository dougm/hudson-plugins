package hudson.plugins.googlecode;

import hudson.Plugin;
import hudson.model.Jobs;
import hudson.plugins.googlecode.scm.GoogleCodeSCM;
import hudson.scm.RepositoryBrowsers;
import hudson.scm.SCMS;

/**
 * Entry point of the plugin.
 *
 * @author Erik Ramfelt
 * @plugin
 */
public class PluginImpl extends Plugin {
    
    private final GoogleCodeLinkAnnotator annotator = new GoogleCodeLinkAnnotator(PROJECT_PROPERTY_DESCRIPTOR);

    public static final GoogleCodeProjectProperty.DescriptorImpl PROJECT_PROPERTY_DESCRIPTOR = new GoogleCodeProjectProperty.DescriptorImpl();
    public static final GoogleCodeRepositoryBrowser.DescriptorImpl REPOSITORY_BROWSER_DESCRIPTOR = new GoogleCodeRepositoryBrowser.DescriptorImpl();
    public static final GoogleCodeSCM.DescriptorImpl GOOGLE_CODE_SCM_DESCRIPTOR = new GoogleCodeSCM.DescriptorImpl();

    @Override
    public void start() throws Exception {
        annotator.register();
        Jobs.PROPERTIES.add(PROJECT_PROPERTY_DESCRIPTOR);
        RepositoryBrowsers.LIST.add(REPOSITORY_BROWSER_DESCRIPTOR);
        SCMS.SCMS.add(GOOGLE_CODE_SCM_DESCRIPTOR);
    }

    @Override
    public void stop() throws Exception {
        SCMS.SCMS.remove(GOOGLE_CODE_SCM_DESCRIPTOR);
        RepositoryBrowsers.LIST.remove(REPOSITORY_BROWSER_DESCRIPTOR);
        Jobs.PROPERTIES.remove(PROJECT_PROPERTY_DESCRIPTOR);
        annotator.unregister();
    }
}
