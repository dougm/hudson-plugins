package hudson.plugins.coverage.model;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 26-Jun-2008 23:49:04
 */
public interface Model {
    /**
     * Applies the model to the instance, setting the metric measurements based on the metric measurements of the
     * instance's children.
     *
     * @param instance The instance to apply the model to.
     */
    void apply(Instance instance);
}
