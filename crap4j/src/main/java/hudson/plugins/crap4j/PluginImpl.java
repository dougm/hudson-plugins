package hudson.plugins.crap4j;

import hudson.Plugin;
import hudson.tasks.Publisher;

/**
 * Entry point for plugin.
 * Add a publisher for crap.
 * @plugin crap4j
 */
public class PluginImpl extends Plugin {
	
	@Override
	public void start() throws Exception {
		Publisher.PUBLISHERS.addRecorder(Crap4JPublisher.DESCRIPTOR);
	}
}
