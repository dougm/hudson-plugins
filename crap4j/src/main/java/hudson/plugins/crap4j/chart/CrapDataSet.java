package hudson.plugins.crap4j.chart;

import hudson.plugins.crap4j.CrapBuildResult;
import hudson.util.DataSetBuilder;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;

import org.jfree.data.category.CategoryDataset;

public class CrapDataSet {
	
	private static class Row implements Comparable<Row> {
		private final String tag;
		private final int number;

		public Row(String tag, int number) {
			this.tag = tag;
			this.number = number;
		}

		@Override
		public String toString() {
			return this.tag;
		}

		public int compareTo(Row other) {
			return this.number == other.number ? 0
					: this.number < other.number ? 1 : -1;
		}
	}

	private final CrapBuildResult report;

	public CrapDataSet(CrapBuildResult report) {
		this.report = report;
	}

	public CategoryDataset buildCategoryDataSet(ChartSeriesDefinition extractor) {
		Row dataRow = new Row(extractor.getDenotation(), 0);
		DataSetBuilder<Row, NumberOnlyBuildLabel> builder = new DataSetBuilder<Row, NumberOnlyBuildLabel>();
		CrapBuildResult currentReport = this.report;
		while (null != currentReport) {
			builder.add(extractor.extractNumberFrom(currentReport.getResultData()), dataRow,
					new NumberOnlyBuildLabel(currentReport.getOwner()));
			currentReport = currentReport.getPrevious();
		}
		return builder.build();
	}
}
