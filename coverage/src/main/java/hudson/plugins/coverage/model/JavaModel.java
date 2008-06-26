package hudson.plugins.coverage.model;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 26-Jun-2008 18:01:36
 */
public class JavaModel {
    public static final Element LANGUAGE = Element.getRootElement().newChild("java", false);
    public static final Element PACKAGE = LANGUAGE.newChild("package", false);
    public static final Element FILE = PACKAGE.newChild("file", true);
    public static final Element CLASS = FILE.newChild("class", false);
    public static final Element METHOD = CLASS.newChild("method", false);

    public static final Metric CLASS_COVERAGE = Metric.newMetric("class");
    public static final Metric METHOD_COVERAGE = Metric.newMetric("method");
    public static final Metric LINE_COVERAGE = Metric.LINE_COVERAGE;
    public static final Metric BRANCH_COVERAGE = Metric.BRANCH_COVERAGE;
}
