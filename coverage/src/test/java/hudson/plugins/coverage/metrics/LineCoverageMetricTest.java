package hudson.plugins.coverage.metrics;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

/**
 * LineCoverageMetric Tester.
 *
 * @author <Authors name>
 * @since <pre>08/16/2007</pre>
 * @version 1.0
 */
public class LineCoverageMetricTest extends TestCase {
    public LineCoverageMetricTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCase1() throws Exception {
        LineCoverageMetric instance = new LineCoverageMetric();
        assertEquals(0, instance.getCoveredCount());
        assertEquals(0, instance.getTotalCount());
    }

    public void testCase2() throws Exception {
        LineCoverageMetric instance = new LineCoverageMetric();
        instance.addCoverage(5, 2);
        instance.addCoverage(15, 3);
        instance.addCoverage(23, 0);
        assertEquals(2, instance.getCoveredCount());
        assertEquals(3, instance.getTotalCount());
    }

    public void testCase3() throws Exception {
        LineCoverageMetric instance1 = new LineCoverageMetric();
        instance1.addCoverage(5, 2);
        instance1.addCoverage(15, 3);
        instance1.addCoverage(23, 0);
        assertEquals(2, instance1.getCoveredCount());
        assertEquals(3, instance1.getTotalCount());
        LineCoverageMetric instance2 = new LineCoverageMetric();
        instance2.addCoverage(5, 0);
        instance2.addCoverage(15, 5);
        instance2.addCoverage(25, 4);
        instance2.addCoverage(27, 0);
        assertEquals(2, instance2.getCoveredCount());
        assertEquals(4, instance2.getTotalCount());
        LineCoverageMetric instance3 = new LineCoverageMetric();
        instance3.merge(instance1);
        assertEquals(instance1.getCoveredCount(), instance3.getCoveredCount());
        assertEquals(instance1.getTotalCount(), instance3.getTotalCount());
        instance3.merge(instance2);
        assertEquals(3, instance3.getCoveredCount());
        assertEquals(5, instance3.getTotalCount());
    }

    public void testCase4() throws Exception {
        LineCoverageMetric instance1 = new LineCoverageMetric();
        instance1.addCoverage(5, 2);
        instance1.addCoverage(15, 3);
        instance1.addCoverage(23, 0);
        assertEquals(2, instance1.getCoveredCount());
        assertEquals(3, instance1.getTotalCount());
        LineCoverageMetric instance2 = new LineCoverageMetric();
        instance2.addCoverage(5, 0);
        instance2.addCoverage(15, 5);
        instance2.addCoverage(25, 4);
        instance2.addCoverage(27, 0);
        assertEquals(2, instance2.getCoveredCount());
        assertEquals(4, instance2.getTotalCount());
        LineCoverageMetric instance3 = new LineCoverageMetric(instance1.getLineCoverageCounts());
        assertEquals(instance1.getCoveredCount(), instance3.getCoveredCount());
        assertEquals(instance1.getTotalCount(), instance3.getTotalCount());
        instance1.addCoverage(32, 0);
        assertEquals(2, instance3.getCoveredCount());
        assertEquals(3, instance3.getTotalCount());
        assertEquals(2, instance1.getCoveredCount());
        assertEquals(4, instance1.getTotalCount());
        instance3.merge(instance2);
        assertEquals(3, instance3.getCoveredCount());
        assertEquals(5, instance3.getTotalCount());
    }

    public static Test suite() {
        return new TestSuite(LineCoverageMetricTest.class);
    }
}
