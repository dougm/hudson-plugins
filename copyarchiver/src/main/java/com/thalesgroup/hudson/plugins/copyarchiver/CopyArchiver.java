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

package com.thalesgroup.hudson.plugins.copyarchiver;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Hudson;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class CopyArchiver extends Notifier implements Serializable {

    @SuppressWarnings("unused")
    private String sharedDirectoryPath;

    @SuppressWarnings("unused")
    private boolean useTimestamp;

    @SuppressWarnings("unused")
    private String datePattern;

    @SuppressWarnings("unused")
    private boolean flatten;

    private List<ArchivedJobEntry> archivedJobList = new ArrayList<ArchivedJobEntry>();

    @SuppressWarnings("unused")
    public String getSharedDirectoryPath() {
        return sharedDirectoryPath;
    }

    @SuppressWarnings("unused")
    public boolean isUseTimestamp() {
        return useTimestamp;
    }

    @SuppressWarnings("unused")
    public String getDatePattern() {
        return datePattern;
    }

    @SuppressWarnings("unused")
    public boolean isFlatten() {
        return flatten;
    }

    @SuppressWarnings("unused")
    public List<ArchivedJobEntry> getArchivedJobList() {
        return archivedJobList;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @SuppressWarnings("unused")
    public static final class CopyArchiverDescriptor extends BuildStepDescriptor<Publisher> {

        //CopyOnWriteList
        @SuppressWarnings("unused")
        private List<AbstractProject> jobs;

        @SuppressWarnings("unused")
        public CopyArchiverDescriptor() {
            super(CopyArchiverPublisher.class);
            load();
        }

        @Override
        public String getDisplayName() {
            return "Aggregate the archived artifacts";
        }

        @Override
        public String getHelpFile() {
            return "/plugin/copyarchiver/help.html";
        }


        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @SuppressWarnings("unused")
        public List<AbstractProject> getJobs() {
            return Hudson.getInstance().getItems(AbstractProject.class);
        }

    }


    @SuppressWarnings("unchecked")
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
                           final BuildListener listener) throws InterruptedException, IOException {
        return copyArchiverPublisher.perform(build, launcher, listener);
    }


    private transient CopyArchiverPublisher copyArchiverPublisher;

    /**
     * Used for backward compatibility
     *
     * @return the new object, an instance of  CopyArchiverPublisher
     */
    private Object readResolve() {
        copyArchiverPublisher = new CopyArchiverPublisher();
        copyArchiverPublisher.setSharedDirectoryPath(sharedDirectoryPath);
        copyArchiverPublisher.setUseTimestamp(useTimestamp);
        copyArchiverPublisher.setDatePattern(datePattern);
        copyArchiverPublisher.setFlatten(flatten);
        copyArchiverPublisher.setDeleteShared(true);
        copyArchiverPublisher.setArchivedJobList(archivedJobList);
        return copyArchiverPublisher;
    }

}
