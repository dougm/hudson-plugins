/*
 * The MIT License
 * 
 * Copyright (c) 2004-2010, Andrew Bayer, Alan Harder
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
package hudson.plugins.hgca;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.DataBoundConstructor;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

/**
 * Property for {@link AbstractProject} that stores the HGCA pattern/URL pairs. Also handles setting
 * them globally.
 *
 * @author Andrew Bayer, Alan Harder
 */
public final class HGCAProjectProperty extends JobProperty<AbstractProject<?,?>> {
    
    /**
     * HGCA Pattern/URL pairs for this project - overrides global settings.
     */
    public final HashMap<String,String> annoPats;
    private final Boolean applyGlobal;

    @DataBoundConstructor
    public HGCAProjectProperty(List<Entry> annoPats, Boolean applyGlobal) {
        this.annoPats = toMap(annoPats);
        this.applyGlobal = applyGlobal;
    }

    public HGCAProjectProperty(boolean applyGlobal, Entry... annoPats) {
        this(Arrays.asList(annoPats), Boolean.valueOf(applyGlobal));
    }

    public HashMap<String,String> getAnnotations() {
        // Get global annotations first, then project annotations.
        HashMap<String,String> allAnnos = new HashMap<String,String>();
        if (getApplyGlobal())
            allAnnos.putAll(DescriptorImpl.get().getGlobalAnnotations());
        allAnnos.putAll(annoPats);
        return allAnnos;
    }

    public boolean getApplyGlobal() {
        return applyGlobal == null || applyGlobal.booleanValue();
    }

    @Extension
    public static final class DescriptorImpl extends JobPropertyDescriptor {
        // We basically rip off the same logic for per-project annotation pairs for use in setting
        // global annotation pairs.
        private HashMap<String,String> globalAnnotations = new HashMap<String,String>();
        private Boolean alwaysApply;

        public DescriptorImpl() {
            load();
        }

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return AbstractProject.class.isAssignableFrom(jobType);
        }

        public HashMap<String,String> getGlobalAnnotations() {
            return globalAnnotations;
        }

        public boolean getAlwaysApply() {
            return alwaysApply != null && alwaysApply.booleanValue();
        }

        public String getDisplayName() {
            return Messages.DisplayName();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject o) throws FormException {
            HGCAProjectProperty hpp = req.bindJSON(HGCAProjectProperty.class,o);
            // Check the temporary project property to see if it has any pairs - make
            // sure to use annoPats directly and not getAnnotations(), 'cos that may include
            // existing global annotation pairs, and therefore breaks deletion.
            this.globalAnnotations = hpp.annoPats;
            this.alwaysApply = o.getBoolean("alwaysApply");
            save();
            return super.configure(req, o);
        }
        
        @Override
        public JobProperty<?> newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            HGCAProjectProperty hpp = null;
            if (formData.has("annoBlock"))
                hpp = req.bindJSON(HGCAProjectProperty.class, formData.getJSONObject("annoBlock"));
            return hpp;
        }

        static DescriptorImpl get() {
            return Hudson.getInstance().getDescriptorByType(DescriptorImpl.class);
        }
    }

    public static class Entry {
        public String key, value;
        
        @DataBoundConstructor
        public Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private static HashMap<String,String> toMap(List<Entry> entries) {
        HashMap<String,String> map = new HashMap<String,String>();
        if (entries!=null && entries.size() > 0) {
            for (Entry entry: entries) {
                map.put(entry.key,entry.value);
            }
        }
        return map;
    }
}
