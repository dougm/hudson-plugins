package hudson.plugins.javanet_uploader;

/**
 * Instruction of how to upload one file.
 *
 * @author Kohsuke Kawaguchi
 */
public final class Entry {
    /**
     * Destination of the copy. If the source file matches
     * multiple files, this will be treated as a directory.
     */
    public String filePath;
    public String description;
    public String status;
    /**
     * File name relative to the workspace root to upload.
     * <p>
     * May contain macro, wildcard.
     */
    public String sourceFile;
}
