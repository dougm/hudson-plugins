import hudson.plugins.jwsdp_sqe.Report;

import java.io.File;

/**
 * @author Kohsuke Kawaguchi
 */
public class JWSDPMain {
    public static void main(String[] args) throws Exception {
        Report r = new Report(null);
        r.add(new File(args[0]));
        System.out.println(r.getFailCount()+"/"+r.getTotalCount());
    }
}
