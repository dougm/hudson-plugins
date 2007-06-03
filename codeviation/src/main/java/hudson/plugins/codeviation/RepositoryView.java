/* Copyright (c) 2007, http://www.codeviation.org project 
 * This program is made available under the terms of the MIT License. 
 */

package hudson.plugins.codeviation;

import hudson.model.ModelObject;
import hudson.model.Project;
import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import hudson.util.ShiftedCategoryAxis;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.codeviation.model.CompilationStatus;
import org.codeviation.model.Repository;
import org.codeviation.model.SourceRoot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 *
 * @author pzajac
 */
public class RepositoryView implements ModelObject {
    public  Repository repository;
    public Project project;
    
    public List<SourceRootView> getSourceRootViews() {
        List<SourceRootView> srcRoots = new ArrayList<SourceRootView>();
        for (SourceRoot srcRoot : repository.getSourceRoots()) {
            srcRoots.add(new SourceRootView(srcRoot,project));
        }
        return srcRoots;
    }
    public RepositoryView(Repository repository,Project project) {
        this.repository = repository;
        this.project = project;
    }
    public static String getSourceRootUrl(String name) {
        return name.replace('/', '.');
    }
    public String getDisplayName() {
        return (repository != null) ? repository.getName() : "Invalid repository"  ;
    }
    public Set<String> getTags() {
       if  (repository != null) {
           return  repository.getAllTags() ;
       } else {
           return Collections.emptySet();
       }
    }
    
     public SourceRootView getDynamic(String token, StaplerRequest req, StaplerResponse rsp ) throws IOException {
         String path = req.getOriginalRequestURI();
         int index2 = path.lastIndexOf('/');
         int index = path.lastIndexOf('/',index2 - 1);
         JavaFileIterableView.updateGraphType(req, rsp);
         String decodedToken = SourceRootView.decodeUrl(token);
         
         SourceRoot srcRoot = null;
         if (decodedToken != null && repository != null) {
             srcRoot = repository.getSourceRoot(decodedToken);
         }
         return (srcRoot != null) ? new SourceRootView(srcRoot,project) : null;
    }
     
     static Logger getLogger() {
         return Logger.getLogger(Repository.class.getName());
     }

    public void doGraph(StaplerRequest req, StaplerResponse rsp) throws IOException {
        CompilationStatus cs = repository.getCompilationStatus();
        List<String> tags = new ArrayList<String>(cs.getTags());
        
        // sort tags
        
        Collections.sort(tags,new Comparator<String>() {
            public int compare(String o1, String o2) {
                Date date1 = repository.getTagDate(o2);
                Date date2 = repository.getTagDate(o2);
                if (date1 != null) {
                    return date1.compareTo(date2);
                }
                return 0;
            }
        });
        
        // create charts 
    
        DataSetBuilder<String,String> dsb = new DataSetBuilder<String,String>();

        int max = 1;
        int passed = 0;
        int failures = 0;
        for( String tag : tags ) {
            passed = 0;
            failures = 0;
            Map<String,Boolean> statuses = cs.getSourceRootCompilationStatuses(tag);
            for (Map.Entry<String,Boolean> entry : statuses.entrySet()) {
                if (entry.getValue()) {
                    passed++;
                } else {
                    failures++;
                }
                        
            }
            dsb.add( passed, "passed", tag);
            dsb.add( failures, "errors", tag);
        }
        max = Math.max(max, passed);
        max = Math.max(max,failures);
        ChartUtil.generateGraph(req,rsp,createChart(dsb.build(),max),400,200);
    }

    private JFreeChart createChart(CategoryDataset dataset,int max) {

        final JFreeChart chart = ChartFactory.createLineChart(
            null,                   // chart title
            null,                   // unused
            "counts",                    // range axis label
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
        rangeAxis.setUpperBound(max*1.2);
        rangeAxis.setLowerBound(0);

        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setStroke(new BasicStroke(4.0f));

        // crop extra space around the graph
        plot.setInsets(new RectangleInsets(5.0,0,0,5.0));

        return chart;
    }

}
