/*
 * The MIT License
 * 
 * Copyright (c) 2004-2010, Sun Microsystems, Inc., Alan Harder
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.status_view;

import hudson.model.Descriptor.FormException;
import hudson.Extension;
import hudson.model.Job;
import hudson.model.ListView;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TopLevelItem;
import hudson.model.ViewDescriptor;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * List view type filters jobs by status of the latest build.
 *
 * @author Alan Harder <mindless@dev.java.net>
 */
public class StatusView extends ListView {

    private boolean stable, unstable, failed, aborted, running;

    @DataBoundConstructor
    public StatusView(String name) {
        super(name);
    }

    public boolean isStable() { return stable; }
    public boolean isUnstable() { return unstable; }
    public boolean isFailed() { return failed; }
    public boolean isAborted() { return aborted; }
    public boolean isRunning() { return running; }

    @Override
    public synchronized List<TopLevelItem> getItems() {
        List<TopLevelItem> base = super.getItems(),
                           result = new ArrayList<TopLevelItem>(base.size());
        for (TopLevelItem item : base) {
            if (item instanceof Job) {
                if (running && !((Job)item).isBuilding()) continue;
                Run lastBuild = ((Job)item).getLastCompletedBuild();
                Result status = lastBuild!=null ? lastBuild.getResult() : null;
                if ( (stable && status == Result.SUCCESS)
                  || (unstable && status == Result.UNSTABLE)
                  || (failed && status == Result.FAILURE)
                  || (aborted && status == Result.ABORTED)
                  || (running && status == null) /* Show running job if it has no completed builds */) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    @Override
    protected void submit(StaplerRequest req) throws ServletException, FormException {
        stable = req.hasParameter("status_view.stable");
        unstable = req.hasParameter("status_view.unstable");
        failed = req.hasParameter("status_view.failed");
        aborted = req.hasParameter("status_view.aborted");
        running = req.hasParameter("status_view.running");
        super.submit(req);
    }

    @Extension
    public static final class DescriptorImpl extends ViewDescriptor {
        public String getDisplayName() {
            return Messages.DisplayName();
        }
    }
}
