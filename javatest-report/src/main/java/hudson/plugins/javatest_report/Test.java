/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://jwsdp.dev.java.net/CDDLv1.0.html
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://jwsdp.dev.java.net/CDDLv1.0.html  If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 */
package hudson.plugins.javatest_report;

/**
 * A {@link Test} is a set of {@link TestCase}s.
 *
 * <p>
 * It is also counted as a runnable test by itself, if there are no testcases.
 *
 * @author Rama Pulavarthi
 */
public class Test extends TestCollection<Test,TestCase> {
    public String getChildTitle() {
        return "Test Case";
    }

    public int getTotalCount() {
        if(super.getTotalCount() != 0)
            return super.getTotalCount();
        else
            return 1;
    }

    public int getFailCount() {
        if(super.getTotalCount() != 0)
            return super.getFailCount();
        else
            return ((getStatus()==Status.PASS || getStatus()==Status.SKIP) ? 0 : 1);
    }
    
    public int getSkippedCount() {
       if(super.getTotalCount() != 0)
           return super.getSkippedCount();
       else
           return (getStatus()==Status.SKIP ? 1 : 0);
   }
}
