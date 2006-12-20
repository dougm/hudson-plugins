package hudson.plugins.javatest_report;

/**
 * @author Rama Pulavarthi
 */
public enum Status {
    /**
     * Test ran successfully.
     */
    PASS("result-passed","pass"),
    /**
     * Test ran but failed.
     */
    FAIL("result-failed","fail"),
    /**
     * Test status ambiguous.
     */
    AMBIGUOUS("result-failed","ambiguous"),

    /**
     * Test error.
     */
    ERROR("result-failed","error"),
    /**
     * VM Failed.
     */
    VM_FAIL("result-failed","vm_fail"),
    /**
     * Test didn't run.
     */
    SKIP("result-failed","did_not_run");

    private final String cssClass;
    private final String message;

    Status(String cssClass, String message) {
       this.cssClass = cssClass;
       this.message = message;
   }

    public String getCssClass() {
        return cssClass;
    }

    public String getMessage() {
        return message;
    }
}
