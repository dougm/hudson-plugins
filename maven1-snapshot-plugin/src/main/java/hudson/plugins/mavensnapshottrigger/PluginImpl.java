package hudson.plugins.mavensnapshottrigger;

import hudson.Plugin;
import hudson.triggers.Triggers;

/**
 * Entry point of a plugin.
 * 
 * <p>
 * There must be one {@link Plugin} class in each plugin.
 * See javadoc of {@link Plugin} for more about what can be done on this class.
 *
 * @author Jarkko Viinamäki
 * @plugin
 */
public class PluginImpl extends Plugin {
    public void start() throws Exception {
        Triggers.TRIGGERS.add(MavenSnapshotTrigger.DESCRIPTOR);
    }
}
