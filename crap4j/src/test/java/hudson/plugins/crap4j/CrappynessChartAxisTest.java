package hudson.plugins.crap4j;

import hudson.plugins.crap4j.chart.LineChartMaker;
import hudson.plugins.crap4j.chart.CrapDataSet.Row;
import hudson.util.DataSetBuilder;

import java.io.IOException;

import junit.framework.TestCase;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;

public class CrappynessChartAxisTest extends TestCase {
	
	public CrappynessChartAxisTest() {
		super();
	}
	
	/**
	 * This test ensures that values lower than 1.0 get meaningful
	 * axis ticks in the chart. It can test this circumstance only indirectly (
	 * Axis getters for ticks are protected in jfreechart).
	 * Related to <a href="https://hudson.dev.java.net/issues/show_bug.cgi?id=1952">Hudson issue #1952</a>
	 */
	public void testFractionalPercentagesAreShownWithTicks() throws IOException {
		LineChartMaker chartMaker = new LineChartMaker();
		JFreeChart chart = chartMaker.createChart(buildFractionalDataset(), "Test Chart");
		assertEquals(CategoryPlot.class, chart.getPlot().getClass());
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		assertEquals(NumberAxis.class, plot.getRangeAxis().getClass());
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		assertNotNull(rangeAxis);
		rangeAxis.configure();
		TickUnitSource standardTickUnits = rangeAxis.getStandardTickUnits();
		assertEquals(NumberAxis.createStandardTickUnits(), standardTickUnits);
		assertEquals(0.945d, rangeAxis.getUpperBound(), 1E-9);
		assertEquals(0.0d, rangeAxis.getLowerBound(), 1E-9);
	}
	
	private CategoryDataset buildFractionalDataset() throws IOException {
		Row dataRow = new Row("Test Crap", 0);
		DataSetBuilder<Row, Integer> builder = new DataSetBuilder<Row, Integer>();
		double currentValue = 0.0d;
		for (int i = 0; i < 10; i++) {
			builder.add(Double.valueOf(currentValue), dataRow, Integer.valueOf(i));
			currentValue += 0.1d;
		}
		return builder.build();
	}
}
