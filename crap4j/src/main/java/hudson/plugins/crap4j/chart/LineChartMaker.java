package hudson.plugins.crap4j.chart;

import hudson.util.ColorPalette;

import java.awt.BasicStroke;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;

public class LineChartMaker extends AbstractChartMaker {
	
	public LineChartMaker() {
		super();
	}
	
	@Override
	protected JFreeChart createRawChart(CategoryDataset dataset,
			String rangeAxisTitle) {
        return ChartFactory.createLineChart(
                null,                   // chart title
                null,                   // unused
                rangeAxisTitle,          // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                false,                     // include legend
                true,                     // tooltips
                false                     // urls
        );
	}
	
	@Override
	protected void setupPlot(CategoryPlot plot) {
		super.setupPlot(plot);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseStroke(new BasicStroke(2.0f));
        ColorPalette.apply(renderer);
	}
	
	@Override
	protected void setupRangeAxis(NumberAxis rangeAxis) {
		super.setupRangeAxis(rangeAxis);
		rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
	}
}
