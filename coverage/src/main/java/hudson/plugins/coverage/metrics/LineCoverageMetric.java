package hudson.plugins.coverage.metrics;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 16-Aug-2007 09:41:07
 */
public class LineCoverageMetric extends AbstractCoverageMetric {
    private Map<Integer,Integer> lineCoverageCounts = new HashMap<Integer,Integer>();
    private int coveredCount = -1;


    public LineCoverageMetric() {
    }

    public LineCoverageMetric(Map<Integer, Integer> lineCoverageCounts) {
        this.lineCoverageCounts.putAll(lineCoverageCounts);
    }


    public Map<Integer, Integer> getLineCoverageCounts() {
        return Collections.unmodifiableMap(lineCoverageCounts);
    }

    public synchronized void merge(LineCoverageMetric metric) {
        for (Map.Entry<Integer,Integer> entry : metric.lineCoverageCounts.entrySet()) {
            final Integer line = entry.getKey();
            final Integer additionalCoverage = entry.getValue();
            if (additionalCoverage == null) {
                break;
            }
            final Integer existingCoverage = lineCoverageCounts.get(line);
            if (existingCoverage == null) {
                lineCoverageCounts.put(line, additionalCoverage);
            } else {
                lineCoverageCounts.put(line, existingCoverage + additionalCoverage);
            }
        }
        coveredCount = -1;
    }

    public synchronized void addCoverage(int line, int coverageCount) {
        final Integer existingCoverage = lineCoverageCounts.get(line);
        if (existingCoverage == null) {
            lineCoverageCounts.put(line, coverageCount);
        } else {
            lineCoverageCounts.put(line, existingCoverage + coverageCount);
        }
        coveredCount = -1;
    }

    public synchronized void setCoverage(int line, int coverageCount) {
        lineCoverageCounts.put(line, coverageCount);
        coveredCount = -1;
    }

    public Integer getCoverage(int line) {
        return lineCoverageCounts.get(line);
    }

    public int getTotalCount() {
        return lineCoverageCounts.size();
    }

    public synchronized int getCoveredCount() {
        if (coveredCount < 0) {
            int coveredCount = 0;
            for (Integer lineCoveredCount: lineCoverageCounts.values()) {
                if (lineCoveredCount != null && lineCoveredCount > 0) {
                    coveredCount++;
                }
            }
            this.coveredCount = coveredCount;
        }
        return coveredCount;
    }
}
