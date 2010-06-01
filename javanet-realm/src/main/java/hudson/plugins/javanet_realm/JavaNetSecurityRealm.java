package hudson.plugins.javanet_realm;

import hudson.Extension;
import hudson.Util;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.security.AbstractPasswordBasedSecurityRealm;
import hudson.security.GroupDetails;
import hudson.security.SecurityRealm;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.kohsuke.jnt.JavaNet;
import org.kohsuke.jnt.JavaNetRealm;
import org.kohsuke.jnt.ProcessingException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.springframework.dao.DataAccessException;

import java.io.File;
import java.io.IOException;

/**
 * {@link SecurityRealm} that talks to java.net.
 *
 * @author Kohsuke Kawaguchi
 */
public class JavaNetSecurityRealm extends AbstractPasswordBasedSecurityRealm {
    public final String project;

    private transient JavaNetRealm realm;

    @DataBoundConstructor
    public JavaNetSecurityRealm(String project) {
        this.project = Util.fixEmpty(project);
        readResolve();  // initialize
    }

    private Object readResolve() {
        realm = new JavaNetRealm(new File(Hudson.getInstance().getRootDir(),"java.net-realm/"+ project)) {
            @Override
            protected boolean authenticateConnection(JavaNet con) throws ProcessingException {
                if (project==null)  return true;
                return con.getMyself().getMyProjects().contains(con.getProject(project));
            }
        };
        return this;
    }

    @Override
    protected UserDetails authenticate(String username, String password) throws AuthenticationException {
        try {
            if (realm.authenticate(username,password))
                return loadUserByUsername(username);
            else
            if (project==null)
                throw new BadCredentialsException("Not a valid user");
            else
                throw new BadCredentialsException("Either not a valid user or you are not a "+project+" committer");
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to authenticate",e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
        return new User(userName,"",true,true,true,true,new GrantedAuthority[]{AUTHENTICATED_AUTHORITY});
    }

    @Override
    public GroupDetails loadGroupByGroupname(String groupName) throws UsernameNotFoundException, DataAccessException {
        throw new UsernameNotFoundException(groupName);
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<SecurityRealm> {
        @Override
        public String getDisplayName() {
            return "Authenticate java.net users";
        }
    }
}
