/* Copyright (c) 2007, http://www.codeviation.org project 
 * This program is made available under the terms of the MIT License. 
 */

package hudson.plugins.codeviation;

import hudson.model.Project;
import java.util.Collections;
import java.util.Date;
import org.codeviation.model.JavaFile;

/**
 *
 * @author pzajac
 */
public class JavaFileView extends JavaFileIterableView{
    public JavaFile jf;
    public Project project;
    
    /**
     * 
     * @param jf 
     * @param project 
     */
    public JavaFileView(JavaFile jf,Project project) {
        this.jf = jf;
        this.project = project;
    }

    protected Date getMinDate() {
        return jf.getPackage().getSourceRoot().getMinTagDate();
    }

    protected Date getMaxDate() {
        return jf.getPackage().getSourceRoot().getMaxTagDate();
    }

    protected Iterable<JavaFile> getJavaFiles() {
        return Collections.singleton(jf);
    }

    public String getDisplayName() {
        return jf.getName();
    }
    public String getUrl() {
        return jf.getName();
    }

}
