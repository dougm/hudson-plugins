/*******************************************************************************
* Copyright (c) 2009 Thales Corporate Services SAS                             *
* Author : Gregory Boissinot                                                   *
*                                                                              *
* Permission is hereby granted, free of charge, to any person obtaining a copy *
* of this software and associated documentation files (the "Software"), to deal*
* in the Software without restriction, including without limitation the rights *
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell    *
* copies of the Software, and to permit persons to whom the Software is        *
* furnished to do so, subject to the following conditions:                     *
*                                                                              *
* The above copyright notice and this permission notice shall be included in   *
* all copies or substantial portions of the Software.                          *
*                                                                              *
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR   *
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,     *
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  *
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER       *
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,*
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN    *
* THE SOFTWARE.                                                                *
*******************************************************************************/

package com.thalesgroup.hudson.plugins.cppcheck;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.matrix.MatrixProject;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

import java.io.PrintStream;

import org.kohsuke.stapler.DataBoundConstructor;

import com.thalesgroup.hudson.plugins.cppcheck.util.CppcheckBuildResultEvaluator;
import com.thalesgroup.hudson.plugins.cppcheck.util.Messages;

public class CppcheckPublisher extends Publisher {
	
    private final String cppcheckReportPattern;

    private final String threshold;

    private final String newThreshold;

    private final String failureThreshold;

    private final String newFailureThreshold;

    private final String healthy;

    private final String unHealthy;

    private final String thresholdLimit;

    @Override
    public CppcheckDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    @DataBoundConstructor
    public CppcheckPublisher(final String cppcheckReportPattern,
                               final String threshold,
                               final String newThreshold,
                               final String failureThreshold,
                               final String newFailureThreshold,
                               final String healthy,
                               final String unHealthy,
                               final String thresholdLimit) {

        this.cppcheckReportPattern = cppcheckReportPattern;
        this.threshold=threshold;
        this.newFailureThreshold=newFailureThreshold;
        this.newThreshold=newThreshold;
        this.failureThreshold=failureThreshold;
        this.healthy=healthy;
        this.unHealthy=unHealthy;
        this.thresholdLimit=thresholdLimit;
    }

    @Override
    public Action getProjectAction(AbstractProject<?,?> project){
        return new CppcheckProjectAction(project);
    }

    protected boolean canContinue(final Result result) {
        return result != Result.ABORTED && result != Result.FAILURE;
    }

    
    @Override
    public boolean perform(AbstractBuild<?,?> build, Launcher launcher, BuildListener listener){
    	
        if(this.canContinue(build.getResult())){
        	PrintStream logger = listener.getLogger();
        	
        	Messages.log(logger,"Starting the cppcheck analysis.");
        	
            final FilePath[] moduleRoots= build.getProject().getModuleRoots();
            final boolean multipleModuleRoots= moduleRoots != null && moduleRoots.length > 1;
            final FilePath moduleRoot= multipleModuleRoots ? build.getProject().getWorkspace() : build.getProject().getModuleRoot();
        	
        	
            CppcheckParserResult parser = new CppcheckParserResult(logger, getCppcheckReportPattern());            
            CppcheckReport cppcheckReport= null;
            try{
            	cppcheckReport= moduleRoot.act(parser);            	
            }
            catch(Exception e){
            	Messages.log(logger,"Error on cppcheck analysis: " + e);
            	build.setResult(Result.FAILURE);
                return false;            
            }
            
            if (cppcheckReport == null){
            	build.setResult(Result.FAILURE);
                return false;            	
            }
            


            CppcheckHealthReportThresholds cppcheckHealthReportThresholds= 
            	new CppcheckHealthReportThresholds(threshold,newThreshold,failureThreshold,newFailureThreshold,healthy,unHealthy, thresholdLimit);
            CppcheckResult result = new CppcheckResult(cppcheckReport, build);
            CppcheckBuildAction buildAction = new CppcheckBuildAction(build, result, cppcheckHealthReportThresholds);
            build.addAction(buildAction);

            Result buildResult = new CppcheckBuildResultEvaluator().evaluateBuildResult(
                   logger, buildAction.getNumberErrors(thresholdLimit,false), buildAction.getNumberErrors(thresholdLimit,true),cppcheckHealthReportThresholds);

            if (buildResult != Result.SUCCESS) {
                build.setResult(buildResult);
            }

            Messages.log(logger,"End of the cppcheck analysis.");
        }
        return true;
    }

    @Extension    
    public static final CppcheckDescriptor DESCRIPTOR = new CppcheckDescriptor();

    public static final class CppcheckDescriptor extends BuildStepDescriptor<Publisher> {

        public CppcheckDescriptor() {
            super(CppcheckPublisher.class);
        }
               
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return FreeStyleProject.class.isAssignableFrom(jobType) || MatrixProject.class.isAssignableFrom(jobType);
        }

        @Override
        public String getDisplayName() {
            return "Publish Cppcheck test result report";
        }

        @Override
        public final String getHelpFile() {
            return getPluginRoot() + "help.html";
        }

        public String getPluginRoot() {
            return "/plugin/cppcheck/";
        }
    }

	public String getCppcheckReportPattern() {
		return cppcheckReportPattern;
	}

    public String getThreshold() {
        return threshold;
    }

    public String getNewThreshold() {
        return newThreshold;
    }

    public String getFailureThreshold() {
        return failureThreshold;
    }

    public String getNewFailureThreshold() {
        return newFailureThreshold;
    }

    public String getHealthy() {
        return healthy;
    }

    public String getUnHealthy() {
        return unHealthy;
    }

    public String getThresholdLimit() {
        return thresholdLimit;
    }
}
