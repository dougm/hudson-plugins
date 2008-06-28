package hudson.plugins.coverage.model.measurements;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 27-Jun-2008 00:06:37
 */
public class LineCoverage extends BasicCoverage {

    public LineCoverage(int count, int cover) {
        super(cover, count);
    }

}
