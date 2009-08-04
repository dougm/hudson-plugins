package hudson.plugins.cppunit;

import hudson.FilePath;
import hudson.Util;

import java.io.File;

public abstract class AbstractWorkspaceTest {

    protected File parentFile;
    protected FilePath workspace;

    protected void createWorkspace() throws Exception {
        parentFile = Util.createTempDir();
        workspace = new FilePath(parentFile);
        if (workspace.exists()) {
            workspace.deleteRecursive();
        }
        workspace.mkdirs();
    }

    protected void deleteWorkspace() throws Exception {
        workspace.deleteRecursive();
    }
}
