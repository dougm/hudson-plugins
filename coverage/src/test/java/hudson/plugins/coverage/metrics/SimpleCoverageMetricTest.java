package hudson.plugins.coverage.metrics;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

/**
 * SimpleCoverageMetric Tester.
 *
 * @author <Authors name>
 * @since <pre>08/16/2007</pre>
 * @version 1.0
 */
public class SimpleCoverageMetricTest extends TestCase {
    public SimpleCoverageMetricTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCase0() throws Exception {
        SimpleCoverageMetric m = new SimpleCoverageMetric(0,0);
        assertEquals(0, m.getCoveredCount());
        assertEquals(0, m.getTotalCount());
        assertEquals(100, m.getCoverage());
        assertEquals("100% (0/0)", m.toString());
    }

    public void testCase1() throws Exception {
        SimpleCoverageMetric m = new SimpleCoverageMetric(5, 10);
        assertEquals(5, m.getCoveredCount());
        assertEquals(10, m.getTotalCount());
        assertEquals(50, m.getCoverage());
        assertEquals("50% (5/10)", m.toString());
    }

    public void testCase2() throws Exception {
        SimpleCoverageMetric m = new SimpleCoverageMetric(3, 9);
        assertEquals(3, m.getCoveredCount());
        assertEquals(9, m.getTotalCount());
        assertEquals(33, m.getCoverage());
        assertEquals("33% (3/9)", m.toString());
    }

    public void testCase3() throws Exception {
        SimpleCoverageMetric m = new SimpleCoverageMetric(6, 9);
        assertEquals(6, m.getCoveredCount());
        assertEquals(9, m.getTotalCount());
        assertEquals(66, m.getCoverage());
        assertEquals("66% (6/9)", m.toString());
    }

    public void testCase5() throws Exception {
        SimpleCoverageMetric m = new SimpleCoverageMetric(10, 10);
        assertEquals(10, m.getCoveredCount());
        assertEquals(10, m.getTotalCount());
        assertEquals(100, m.getCoverage());
        assertEquals("100% (10/10)", m.toString());
    }

    public void testCase6() throws Exception {
        SimpleCoverageMetric m = new SimpleCoverageMetric(12, 10);
        assertEquals(10, m.getCoveredCount());
        assertEquals(10, m.getTotalCount());
        assertEquals(100, m.getCoverage());
        assertEquals("100% (10/10)", m.toString());
    }

    public void testCase7() throws Exception {
        SimpleCoverageMetric m = new SimpleCoverageMetric(-12, 10);
        assertEquals(0, m.getCoveredCount());
        assertEquals(10, m.getTotalCount());
        assertEquals(0, m.getCoverage());
        assertEquals("0% (0/10)", m.toString());
    }

    public void testCase8() throws Exception {
        SimpleCoverageMetric m = new SimpleCoverageMetric(12, -10);
        assertEquals(0, m.getCoveredCount());
        assertEquals(0, m.getTotalCount());
        assertEquals(100, m.getCoverage());
        assertEquals("100% (0/0)", m.toString());
    }

    public void testCase9() throws Exception {
        SimpleCoverageMetric m = new SimpleCoverageMetric(999, 1000);
        assertEquals(999, m.getCoveredCount());
        assertEquals(1000, m.getTotalCount());
        assertEquals(99, m.getCoverage());
        assertEquals("99% (999/1000)", m.toString());
    }

    public static Test suite() {
        return new TestSuite(SimpleCoverageMetricTest.class);
    }
}
