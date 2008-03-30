package org.jvnet.hudson.plugins.fit;

import hudson.Plugin;
import hudson.tasks.BuildStep;

/**
 * Entry point of a plugin.
 * 
 * @author Eric Lefevre
 * @plugin
 */
public class PluginImpl extends Plugin {
	public void start() throws Exception {
		// add this plugin to the publishers for a free-style build
		BuildStep.PUBLISHERS.add(FitArchiver.DESCRIPTOR);
	}
}
