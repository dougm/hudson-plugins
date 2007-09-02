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

import hudson.model.AbstractBuild;
import hudson.model.ModelObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Common data applicable to all test model objects.
 *
 * <p>
 * Setter methods are for Digester, and once created the test objects
 * are immutable.
 *
 * @param <S>
 *      The derived type of {@link TestCollection} (the same design pattern as you seen in {@link Enum})
 *
 * @author Rama Pulavarthi
 */
public abstract class TestObject<S extends TestObject<S>>
    implements ModelObject {

    /**
     * Unique identifier.
     */
    private String id;

    /**
     * Optional human-readable name.
     */
    private String name;

    /**
     * Optional description that possibly includes HTML.
     */
    private String description;

    private Status status;

    /**
     * Optional message that complements status.
     */
    private String statusMessage;

    /**
     * Optional attributes of a test.
     */
    private Map<String,String> attributes;

    // set by the TestCollection when this is added to it.
    TestCollection parent;

    /**
     * This test has been failing since this build number (not id.)
     * <p/>
     * If {@link #getStatus()}  is {@link Status#PASS}}, this field is left unused to 0.
     */
    private /*final*/ int failedSince;


    /*package*/ TestObject() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.replace(':','.');
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public final String getDisplayName() {
        if(name!=null) {
            if(name.equals("unknown")) {
                return id.substring(7);
            }
            return name;
        } else
            return id;
    }

    public AbstractBuild getOwner() {
        return parent.getOwner();
    }

    /**
     * If this test failed, then return the build number
     * when this test started failing.
     */
    public int getFailedSince() {
        // some old test data doesn't have failedSince value set, so for those compute them.
        if (!isPassed() && failedSince == 0) {
            TestObject prev = getPreviousResult();
            if (prev != null && !prev.isPassed()) {
                this.failedSince = prev.failedSince;
                //   this.failedSince = prev.getFailedSince();
            } else
                this.failedSince = getOwner().getNumber();
        }
        return failedSince;
    }

    private boolean isPassed() {
        return getStatus() == Status.PASS;
    }

    /**
     * Gets the number of consecutive builds (including this)
     * that this test case has been failing.
     */
    public int getAge() {
        if (isPassed())
            return 0;
        else
            return getOwner().getNumber() - failedSince + 1;
    }

    /**
     * Gets the counter part of this {@link TestObject} in the previous run.
     *
     * @return null
     *      if no such counter part exists.
     */
    public S getPreviousResult() {
        TestCollection p = (TestCollection)parent.getPreviousResult();
        if(p!=null)     return (S)p.get(getId());
        else            return null;
    }


    public Status getStatus() {
        return status;
    }

    // Digester don't understand enum
    public void setStatusString(String status) {
        if(status.equalsIgnoreCase("pass"))
            this.status = Status.PASS;
        else
        if(status.equalsIgnoreCase("did_not_run"))
            this.status = Status.SKIP;
        else
        if(status.equalsIgnoreCase("error"))
            this.status = Status.ERROR;
        else
            this.status = Status.FAIL;
    }

    public void addAttribute(String name, String value) {
        if(attributes == null)
            attributes = new HashMap<String,String>();
        attributes.put(name,value);
        if(name.equals("logfile"))
            statusMessage = value;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }

    public abstract int getTotalCount();
    public abstract int getFailCount();
    public abstract int getSkippedCount();
}
