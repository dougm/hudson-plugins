/* Copyright (c) 2007, http://www.codeviation.org project 
 * This program is made available under the terms of the MIT License. 
 */

package hudson.plugins.codeviation;

import hudson.model.Project;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.codeviation.model.JavaFile;
import org.codeviation.model.Package;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 *
 * @author pzajac
 */
public class PackageView extends JavaFileIterableView { 
    Package pack;
    public Project project;
    
    public PackageView(Package pack,Project project) {
        this.pack = pack;
        this.project = project;
    }

    public List<JavaFileView> getJavaFileViews() {
        List<JavaFileView> jfs = new ArrayList<JavaFileView>();
        for (JavaFile jf : pack) {
            getLogger().log(Level.SEVERE,"Add:" + jf.getName());
            jfs.add(new JavaFileView(jf,project));
        }
        return jfs;
    }
    protected Date getMinDate() {
        return pack.getSourceRoot().getMinTagDate();
    }

    protected Date getMaxDate() {
        return pack.getSourceRoot().getMaxTagDate();
    }

    protected Iterable<JavaFile> getJavaFiles() {
        return pack;
    }

    public String getDisplayName() {
        return pack.getName();
    }
    public String getUrl() {
        return pack.getName();
    }
    
     public JavaFileView getDynamic(String token, StaplerRequest req, StaplerResponse rsp ) throws IOException {
         JavaFile jf  = pack.getJavaFile(token);
         return  (jf == null) ? null : 
            new JavaFileView(jf,project);
    }

}
