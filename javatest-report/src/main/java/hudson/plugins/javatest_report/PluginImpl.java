package hudson.plugins.javatest_report;

import hudson.Plugin;
import hudson.tasks.BuildStep;

/**
 * @author Rama Pulavarthi
 * @plugin
 */
public class PluginImpl extends Plugin {
    public void start() throws Exception {
        BuildStep.PUBLISHERS.addRecorder(JavaTestReportPublisher.DescriptorImpl.DESCRIPTOR);
    }
}
