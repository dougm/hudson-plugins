/* Copyright (c) 2007, http://www.codeviation.org project 
 * This program is made available under the terms of the MIT License. 
 */

package hudson.plugins.codeviation;

import hudson.model.Project;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.codeviation.model.JavaFile;
import org.codeviation.model.Package;
import org.codeviation.model.SourceRoot;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 *
 * @author pzajac
 */
public class SourceRootView extends JavaFileIterableView {
    public SourceRoot srcRoot;
    public Project project;
    private static final String SLASH_PATTERN = "-.-";
    
    public SourceRootView(SourceRoot srcRoot,Project project) {
        if (srcRoot == null) {
            throw new NullPointerException();
        }
        this.srcRoot = srcRoot;
        this.project = project;
    }

    public String getDisplayName() {
        return srcRoot.getRelPath();
    }
    public String getUrl() {
        return srcRoot.getRelPath().replace("/",SLASH_PATTERN);
    }
    
    
    public static String decodeUrl(String srcRootName) {
        return srcRootName.replace(SLASH_PATTERN,"/");
    }
    public List<PackageView> getPackageViews() {
        List<PackageView> packs =  new ArrayList<PackageView>();
        for (Package pack : srcRoot.getPackages()) {
            packs.add(new PackageView(pack,project));
        }
        return packs;
    }

    protected Date getMinDate() {
        return srcRoot.getMinTagDate();
    }

    protected Date getMaxDate() {
        return srcRoot.getMaxTagDate();
    }

    protected Iterable<JavaFile> getJavaFiles() {
        int size = 0;
        for (JavaFile jf : srcRoot) {
            size++;
        }
        getLogger().info("JavaFiles: " + size);
        return srcRoot;
    }
    
     public PackageView getDynamic(String token, StaplerRequest req, StaplerResponse rsp ) throws IOException {
         Package pack = srcRoot.getPackage(token,false);
         return  (pack == null) ? null : 
            new PackageView(pack,project);
    }
}


