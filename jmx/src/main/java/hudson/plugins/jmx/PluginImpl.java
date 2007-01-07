package hudson.plugins.jmx;

import hudson.Plugin;
import hudson.model.Hudson;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

/**
 * Entry point of the plugin. This is responsible for registering listeners
 * with Hudson and create/find the MBeanServer.
 *
 * @author Renaud Bruyeron
 * @version $Id$
 * @plugin
 */
public class PluginImpl extends Plugin {
	
	JmxJobListener jjl = null;
	
	public void start() throws Exception {
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		jjl = new JmxJobListener(server);
		Hudson.getInstance().getJobListeners().add(jjl);
    }

	/**
	 * @see hudson.Plugin#stop()
	 */
	@Override
	public void stop() throws Exception {
		Hudson.getInstance().getJobListeners().remove(jjl);
		jjl.unregister();
		jjl = null;
	}
}
