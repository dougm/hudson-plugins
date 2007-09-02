package hudson.plugins.javatest_report;

/**
 * 
 * @author Vladimir Ralev
 *
 */
public class Package extends TestCollection<Suite,Test> {
    public String getChildTitle() {
        return "Package";
    }

}
