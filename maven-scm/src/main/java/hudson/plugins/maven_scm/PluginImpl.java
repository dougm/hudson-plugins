package hudson.plugins.maven_scm;

import hudson.Plugin;
import hudson.scm.SCMS;

/**
 * @plugin
 * @author Kohsuke Kawaguchi
 */
public class PluginImpl extends Plugin {
    public void start() throws Exception {
        SCMS.SCMS.add(new ProviderSpecificDescriptor("Bazaar","bazaar"));
        SCMS.SCMS.add(new ProviderSpecificDescriptor("ClearCase","clearcase"));
        SCMS.SCMS.add(new ProviderSpecificDescriptor("Mercurial","hg"));
        SCMS.SCMS.add(new ProviderSpecificDescriptor("CM Synergy","synergy"));
        SCMS.SCMS.add(new ProviderSpecificDescriptor("Perforce","perforce"));
        SCMS.SCMS.add(new ProviderSpecificDescriptor("StarTeam","starteam"));
        SCMS.SCMS.add(new ProviderSpecificDescriptor("Visual SourceSafe","vss"));
        SCMS.SCMS.add(GenericMavenSCMDescriptor.INSTANCE);
    }
}
