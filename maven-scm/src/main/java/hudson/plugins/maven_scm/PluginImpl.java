package hudson.plugins.maven_scm;

import hudson.Plugin;
import hudson.plugins.maven_scm.bazaar.BazaarDescriptor;
import hudson.scm.SCMS;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.manager.BasicScmManager;

/**
 * @plugin
 * @author Kohsuke Kawaguchi
 */
public class PluginImpl extends Plugin {
    public void start() throws Exception {
        SCMS.SCMS.add(BazaarDescriptor.INSTANCE);
        //SCMS.SCMS.add(new ProviderSpecificDescriptor("ClearCase","clearcase"));
        //SCMS.SCMS.add(new ProviderSpecificDescriptor("Mercurial","hg"));
        //SCMS.SCMS.add(new ProviderSpecificDescriptor("CM Synergy","synergy"));
        //SCMS.SCMS.add(new ProviderSpecificDescriptor("Perforce","perforce"));
        //SCMS.SCMS.add(new ProviderSpecificDescriptor("StarTeam","starteam"));
        //SCMS.SCMS.add(new ProviderSpecificDescriptor("Visual SourceSafe","vss"));
        SCMS.SCMS.add(GenericMavenSCMDescriptor.INSTANCE);
    }

    public static final ScmManager MANAGER = new BasicScmManager();
}
