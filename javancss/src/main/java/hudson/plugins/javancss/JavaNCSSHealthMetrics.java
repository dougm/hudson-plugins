package hudson.plugins.javancss;

import hudson.plugins.helpers.health.HealthMetric;
import org.apache.commons.beanutils.Converter;

/**
 * Created by IntelliJ IDEA. User: stephen Date: 18-Mar-2008 Time: 06:04:17 To change this template use File | Settings
 * | File Templates.
 */
public enum JavaNCSSHealthMetrics implements HealthMetric<JavaNCSSBuildIndividualReport> {

    COMMENT_RATIO {

        public String getName() {
            return "% of lines that are comments";
        }
        public float measure(JavaNCSSBuildIndividualReport report) {
            final float ncss = report.getTotals().getNcss();
            final float comments = report.getTotals().getJavadocLines()
                    + report.getTotals().getMultiCommentLines()
                    + report.getTotals().getSingleCommentLines();
            return (comments * 100) / (ncss + comments);
        }
        public float getBest() {
            return 100;
        }
        public float getWorst() {
            return 0;
        }
    },

    JAVADOC_RATIO {

        public String getName() {
            return "Ratio of javadocs to classes and functions";
        }
        public float measure(JavaNCSSBuildIndividualReport report) {
            final float javadocs = report.getTotals().getJavadocs();
            final float total = report.getTotals().getClasses()
                    + report.getTotals().getFunctions();
            return javadocs / total;
        }
        public float getBest() {
            return 1;
        }
        public float getWorst() {
            return 0;
        }
    };

    static Converter CONVERTER = new Converter() {
        public Object convert(Class aClass, Object o) {
            return valueOf(o.toString());
        }
    };
}
