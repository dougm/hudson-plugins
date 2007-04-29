package hudson.plugins.maven_scm.bazaar;

import hudson.plugins.maven_scm.ProviderSpecificDescriptor;
import hudson.plugins.maven_scm.MavenSCM;
import hudson.plugins.maven_scm.PluginImpl;
import hudson.scm.SCM;
import org.kohsuke.stapler.StaplerRequest;
import org.apache.maven.scm.provider.bazaar.BazaarScmProvider;

/**
 * @author Kohsuke Kawaguchi
 */
public class BazaarDescriptor extends ProviderSpecificDescriptor {
    public static BazaarDescriptor INSTANCE = new BazaarDescriptor();
    
    private BazaarDescriptor() {
        super("Bazaar","bazaar");

        PluginImpl.MANAGER.setScmProvider(provider,new BazaarScmProvider());
    }

    public String getBazaarUrl(MavenSCM scm) {
        return scm.scmUrl.substring("scm:bazaar:".length());
    }

    public SCM newInstance(StaplerRequest req) throws FormException {
        return new MavenSCM("scm:bazaar:"+req.getParameter("mavenscm.bazaar.url"));
    }
}
