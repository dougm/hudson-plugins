package hudson.plugins.maven_scm;

import hudson.scm.SCMDescriptor;
import hudson.scm.SCM;
import org.kohsuke.stapler.StaplerRequest;

/**
 * {@link SCMDescriptor} for {@link MavenSCM} in case providers are not statically known to us.
 *
 * @author Kohsuke Kawaguchi
 */
public class GenericMavenSCMDescriptor extends AbstractMavenSCMDescriptor {
    public static final GenericMavenSCMDescriptor INSTANCE = new GenericMavenSCMDescriptor();

    private GenericMavenSCMDescriptor() {
    }

    public String getDisplayName() {
        return "Other Maven SCM";
    }

    public SCM newInstance(StaplerRequest req) throws FormException {
        return new MavenSCM(req.getParameter("mavenscm.scmUrl"));
    }
}
