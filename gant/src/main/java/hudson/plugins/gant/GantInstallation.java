package hudson.plugins.gant;

import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;

/**
 * Gant installation.
 * 
 * @author Kohsuke Kawaguchi
*/
public final class GantInstallation {
    private final String name;
    private final String groovyHome;

    @DataBoundConstructor
    public GantInstallation(String name, String home) {
        this.name = name;
        this.groovyHome = home;
    }

    /**
     * install directory.
     */
    public String getGroovyHome() {
        return groovyHome;
    }

    /**
     * Human readable display name.
     */
    public String getName() {
        return name;
    }

    public File getExecutable() {
        String execName;
        if(File.separatorChar=='\\')
            execName = "gant.bat";
        else
            execName = "gant";

        return new File(getGroovyHome(),"bin/"+execName);
    }

    /**
     * Returns true if the executable exists.
     */
    public boolean getExists() {
        return getExecutable().exists();
    }
}
