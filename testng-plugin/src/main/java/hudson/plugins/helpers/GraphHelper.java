package hudson.plugins.helpers;

import hudson.util.ChartUtil;
import hudson.util.ShiftedCategoryAxis;
import hudson.util.StackedAreaRenderer2;

import java.awt.Color;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class GraphHelper {
   /**
    * Do not instantiate GraphHelper.
    */
   private GraphHelper() {
   }

   /**
    * Getter for property 'graphUnsupported'.
    *
    * @return Value for property 'graphUnsupported'.
    */
   public static boolean isGraphUnsupported() {
      return ChartUtil.awtProblemCause != null;
   }

   public static void redirectWhenGraphUnsupported(StaplerResponse rsp, StaplerRequest req) throws IOException {
      // not available. send out error message
      rsp.sendRedirect2(req.getContextPath() + "/images/headless.png");
   }

   public static JFreeChart buildChart(CategoryDataset dataset) {

      final JFreeChart chart = ChartFactory.createLineChart(
            null,                   // chart title
            null,                   // unused
            "Test count",                    // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
      );

      // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

      final LegendTitle legend = chart.getLegend();
      legend.setPosition(RectangleEdge.RIGHT);

      chart.setBackgroundPaint(Color.white);

      final CategoryPlot plot = chart.getCategoryPlot();

      // plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
      plot.setBackgroundPaint(Color.WHITE);
      plot.setOutlinePaint(null);
      plot.setRangeGridlinesVisible(true);
      plot.setRangeGridlinePaint(Color.black);

      CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
      plot.setDomainAxis(domainAxis);
      domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
      domainAxis.setLowerMargin(0.0);
      domainAxis.setUpperMargin(0.0);
      domainAxis.setCategoryMargin(0.0);

      final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

      plot.setRenderer(new StackedAreaRenderer2());
      // crop extra space around the graph
      plot.setInsets(new RectangleInsets(5.0, 0, 0, 5.0));

      return chart;
   }
}
