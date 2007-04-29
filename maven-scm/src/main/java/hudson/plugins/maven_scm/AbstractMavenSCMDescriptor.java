package hudson.plugins.maven_scm;

import hudson.scm.SCMDescriptor;
import hudson.util.FormFieldValidator;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class AbstractMavenSCMDescriptor extends SCMDescriptor<MavenSCM> {
    protected AbstractMavenSCMDescriptor() {
        super(MavenSCM.class,null);
    }

    public void doUrlCheck(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        new FormFieldValidator(req,rsp,false) {
            protected void check() throws IOException, ServletException {
                List list = PluginImpl.MANAGER.validateScmRepository(request.getParameter("value"));
                if(list.isEmpty())
                    ok();
                else {
                    StringBuffer buf = new StringBuffer();
                    for (Object o : list)
                        buf.append(o).append("<br>");
                    error(buf.toString());
                }
            }
        }.check();
    }
}
