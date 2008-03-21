package hudson.plugins.crap4j.chart;

import hudson.util.ColorPalette;
import hudson.util.StackedAreaRenderer2;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.data.category.CategoryDataset;

public class AreaChartMaker extends AbstractChartMaker {
	
    private static final float ALPHA = 0.8f;
	/**
	 * Will be used to provide hyperlinks in the chart
	 */
	private final String relativeURLPath;

	public AreaChartMaker(String relativeURLPath) {
		super();
		this.relativeURLPath = relativeURLPath;
	}
	
	@Override
	protected JFreeChart createRawChart(CategoryDataset dataset,
			String rangeAxisTitle) {
		return ChartFactory.createStackedAreaChart(
				null, // chart title
				null, // unused
				rangeAxisTitle, // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				false, // include legend
				true, // tooltips
				false // urls
				);
	}
	
	@Override
	protected void setupPlot(CategoryPlot plot) {
		super.setupPlot(plot);
		plot.setForegroundAlpha(ALPHA);
		StackedAreaRenderer renderer = new StackedAreaRenderer2();
		plot.setRenderer(renderer);
		renderer.setSeriesPaint(0, ColorPalette.BLUE);
	}
}
