package hudson.plugins.crap4j.calculation;

import hudson.model.HealthReport;
import hudson.plugins.crap4j.model.ProjectCrapBean;

public class HealthBuilder {
	
	private final double threshold;

	public HealthBuilder() {
		this(15.0d);
	}

	public HealthBuilder(double threshold) {
		super();
		this.threshold = threshold;
		if (this.threshold <= 0.0d) {
			throw new IllegalArgumentException("The threshold needs to be positive, and not " + this.threshold);
		}
	}
	
	public double getThreshold() {
		return this.threshold;
	}

	public HealthReport getHealthReportFor(ProjectCrapBean crap) {
		return new HealthReport(
				(int) Math.round(calculateHealthOf(crap.getCrapMethodPercent())),
				getHealthSummary(crap.getCrapMethodCount(), crap.getCrapMethodPercent()));
	}
	
	protected double calculateHealthOf(double crapMethodPercentage) {
		double result = 100.0d - (100.0d * (crapMethodPercentage / this.threshold));
		return Math.min(100.0d, Math.max(0.0d, result));
	}
	
	protected String getHealthSummary(int crapMethodCount, double crapMethodPercentage) {
		StringBuilder result = new StringBuilder();
		result.append(crapMethodCount);
		result.append(" crappy methods (");
		result.append(crapMethodPercentage);
		result.append("%)");
		return result.toString();
	}
}
