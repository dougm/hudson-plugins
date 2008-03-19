package hudson.plugins.emotional_hudson;

import hudson.Plugin;
import hudson.tasks.BuildStep;

public class PluginImpl extends Plugin {
    public void start() throws Exception {
        BuildStep.PUBLISHERS.add(EmotionalHudsonPublisher.DESCRIPTOR);
    }
}
