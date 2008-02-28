package hudson.plugins.javancss.parser;

import junit.framework.TestCase;

import java.net.URL;
import java.io.File;
import java.util.Collection;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 25-Feb-2008 22:37:25
 */
public class StatisticTest extends TestCase {

    public StatisticTest(String name) {
        super(name);
    }

    public void testAntSmoke() throws Exception {
        File inputFile = new File(getClass().getResource("ant-javancss-report.xml").getFile()).getAbsoluteFile();

        Collection<Statistic> r = Statistic.parse(inputFile);

        Statistic expected = new Statistic("");
        expected.setClasses(5);
        expected.setFunctions(8);
        expected.setNcss(46);
        expected.setJavadocs(9);
        expected.setJavadocLines(37);
        expected.setSingleCommentLines(0);
        expected.setMultiCommentLines(0);

        assertEquals(expected, Statistic.total(r));
    }

    public void testMaven2Smoke() throws Exception {
        File inputFile = new File(getClass().getResource("m2-javancss-report.xml").getFile()).getAbsoluteFile();

        Collection<Statistic> r = Statistic.parse(inputFile);

        Statistic expected = new Statistic("");
        expected.setClasses(5);
        expected.setFunctions(8);
        expected.setNcss(46);
        expected.setJavadocs(9);
        expected.setJavadocLines(37);
        expected.setSingleCommentLines(0);
        expected.setMultiCommentLines(0);

        assertEquals(expected, Statistic.total(r));
    }

    public void testMerge() throws Exception {
        File inputFile = new File(getClass().getResource("ant-javancss-report.xml").getFile()).getAbsoluteFile();

        Collection<Statistic> r1 = Statistic.parse(inputFile);

        inputFile = new File(getClass().getResource("m2-javancss-report.xml").getFile()).getAbsoluteFile();

        Collection<Statistic> r2 = Statistic.parse(inputFile);

        Statistic expected = new Statistic("");
        expected.setClasses(10);
        expected.setFunctions(16);
        expected.setNcss(92);
        expected.setJavadocs(18);
        expected.setJavadocLines(74);
        expected.setSingleCommentLines(0);
        expected.setMultiCommentLines(0);

        assertEquals(expected, Statistic.total(Statistic.merge(r1, r2)));

    }
}
