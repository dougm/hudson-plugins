package hudson.plugins.coverage.model;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 27-Jun-2008 17:13:08
 */
public interface Recorder {

    /**
     * Find all the source code files.
     *
     * @param root The instance to record results against.
     */
    void identifySourceFiles(Instance root);

    /**
     * Parse the source file results, populating any sub-file level results.
     *
     * @param sourceFile The source file instance to parse the results for.
     * @patam memo The memo object that the recorder registered against this instance when it was attached.
     */
    void parseSourceResults(Instance sourceFile, Object memo);
}
