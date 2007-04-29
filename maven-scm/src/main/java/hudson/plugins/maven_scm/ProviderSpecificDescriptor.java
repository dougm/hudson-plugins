package hudson.plugins.maven_scm;

import hudson.scm.SCMDescriptor;
import hudson.scm.SCM;
import org.kohsuke.stapler.StaplerRequest;

/**
 * {@link SCMDescriptor} for {@link MavenSCM} in case providers are statically known to us.
 *
 * @author Kohsuke Kawaguchi
 * @see GenericMavenSCMDescriptor
 */
public final class ProviderSpecificDescriptor extends AbstractMavenSCMDescriptor {
    private final String displayName;
    public final String provider;

    /*package*/  ProviderSpecificDescriptor(String displayName, String provider) {
        this.displayName = displayName;
        this.provider = provider;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Maven SCM URL prefix. Matches with {@link MavenSCM#getScmPrefix()}.
     */
    public String getScmPrefix() {
        return "scm:"+provider;
    }

    public String getHelpFile() {
        return "/plugin/maven-scm/"+ provider +"/help.html";
    }

    public String getConfigPage() {
        return getViewPage(clazz, "config-"+provider+".jelly");
    }

    public SCM newInstance(StaplerRequest req) throws FormException {
        return req.bindParameters(MavenSCM.class,"mavenscm.");
    }
}
