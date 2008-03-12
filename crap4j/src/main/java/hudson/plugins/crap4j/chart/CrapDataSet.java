package hudson.plugins.crap4j.chart;

import hudson.plugins.crap4j.model.ProjectCrapBean;
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

	private final ProjectCrapBean report;

	public CrapDataSet(ProjectCrapBean report) {
		this.report = report;
	}

	public CategoryDataset buildCategoryDataSet(ChartSeriesDefinition extractor) {
		Row dataRow = new Row(extractor.getDenotation(), 0);
		DataSetBuilder<Row, NumberOnlyBuildLabel> builder = new DataSetBuilder<Row, NumberOnlyBuildLabel>();
		ProjectCrapBean crap = this.report;
		while (crap != null) {
			builder.add(extractor.extractNumberFrom(crap), dataRow,
					new NumberOnlyBuildLabel(crap.getBuild()));
			crap = crap.getPrevious();
		}
		return builder.build();
	}
}
