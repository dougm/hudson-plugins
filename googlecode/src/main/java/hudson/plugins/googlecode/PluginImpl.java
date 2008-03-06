package hudson.plugins.googlecode;

import hudson.Plugin;
import hudson.model.Jobs;
import hudson.scm.RepositoryBrowsers;

/**
 * Entry point of the plugin.
 *
 * @author Kohsuke Kawaguchi
 * @author Erik Ramfelt
 * @plugin
 */
public class PluginImpl extends Plugin {
    
    private final GoogleCodeLinkAnnotator annotator = new GoogleCodeLinkAnnotator(PROJECT_PROPERTY_DESCRIPTOR);

    static final GoogleCodeProjectProperty.DescriptorImpl PROJECT_PROPERTY_DESCRIPTOR = new GoogleCodeProjectProperty.DescriptorImpl();
    static final GoogleCodeRepositoryBrowser.DescriptorImpl REPOSITORY_BROWSER_DESCRIPTOR = new GoogleCodeRepositoryBrowser.DescriptorImpl();

    @Override
    public void start() throws Exception {
        annotator.register();
        Jobs.PROPERTIES.add(PROJECT_PROPERTY_DESCRIPTOR);
        RepositoryBrowsers.LIST.add(REPOSITORY_BROWSER_DESCRIPTOR);
    }

    @Override
    public void stop() throws Exception {
        annotator.unregister();
    }
}
