package hudson.plugins.javanet_uploader;

/**
 * Instruction of how to upload one file.
 *
 * @author Kohsuke Kawaguchi
 */
public final class Entry {
    private String filePath;
    private String description;
    private String status;
    private String sourceFile;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }
}
