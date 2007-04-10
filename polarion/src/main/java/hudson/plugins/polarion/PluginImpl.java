package hudson.plugins.polarion;

import hudson.Plugin;
import hudson.scm.RepositoryBrowsers;
import hudson.model.Jobs;

/**
 * Entry point of the plugin.
 *
 * @author Jonny Wray
 * @plugin
 */
public class PluginImpl extends Plugin {

    @Override
    public void start() throws Exception {
        RepositoryBrowsers.LIST.add(PolarionRepositoryBrowser.DESCRIPTOR);
    }

    @Override
    public void stop() throws Exception {
    }
}
