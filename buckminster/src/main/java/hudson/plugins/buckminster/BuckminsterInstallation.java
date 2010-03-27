/**
 * 
 */
package hudson.plugins.buckminster;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.EnvironmentSpecific;
import hudson.model.Hudson;
import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.model.DownloadService.Downloadable;
import hudson.plugins.buckminster.command.CommandLineBuilder;
import hudson.plugins.buckminster.install.BuckminsterInstallable;
import hudson.plugins.buckminster.install.BuckminsterInstallable.BuckminsterInstallableList;
import hudson.plugins.buckminster.install.BuckminsterInstallable.Feature;
import hudson.plugins.buckminster.install.BuckminsterInstallable.Repository;
import hudson.plugins.buckminster.util.ReadDelegatingTextFile;
import hudson.slaves.NodeSpecific;
import hudson.tools.DownloadFromUrlInstaller;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolInstaller;
import hudson.tools.ToolProperty;
import hudson.util.TextFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Johannes Utzig
 *
 */
public class BuckminsterInstallation extends ToolInstallation implements EnvironmentSpecific<BuckminsterInstallation>, NodeSpecific<BuckminsterInstallation> {
	

	private String version;
	private String params;
	
	@DataBoundConstructor
	public BuckminsterInstallation(String name, String home, String version, String params, List<ToolProperty<?>> properties) {
		super(name, home, properties);
		this.version = version;
		this.params = params;
	}

	public String getParams() {
		return params;
	}

	public String getVersion() {
		return version;
	}

    public BuckminsterInstallation forEnvironment(EnvVars environment) {
        return new BuckminsterInstallation(getName(), environment.expand(getHome()), version, params, getProperties().toList());
    }

    public BuckminsterInstallation forNode(Node node, TaskListener log) throws IOException, InterruptedException {
        return new BuckminsterInstallation(getName(), translateFor(node, log), version, params, getProperties().toList());
    }
    
    public boolean exists()
    {
    	String home = getHome();
    	File f = new File(home);
    	File buckyDir = new File(f,"buckminster");
    	if(!buckyDir.exists())
    		return false;
    	File executableWin = new File(buckyDir,"buckminster.bat");
    	File executableUnix = new File(buckyDir,"buckminster");
    	return executableWin.exists() || executableUnix.exists();
    }
    
    @Extension
    public static class DescriptorImpl extends ToolDescriptor<BuckminsterInstallation>
    {

		@Override
		public String getDisplayName() {
			return "Buckminster";
		}
		
		@Override
		public List<? extends ToolInstaller> getDefaultInstallers() {
			return Collections.singletonList(new BuckminsterInstaller(null,false));
		}
//		
        // for compatibility reasons, the persistence is done by Ant.DescriptorImpl  
        @Override
        public BuckminsterInstallation[] getInstallations() {
            return Hudson.getInstance().getDescriptorByType(EclipseBuckminsterBuilder.DescriptorImpl.class).getBuckminsterInstallations();
        }

        @Override
        public void setInstallations(BuckminsterInstallation... installations) {
        	Hudson.getInstance().getDescriptorByType(EclipseBuckminsterBuilder.DescriptorImpl.class).setBuckminsterInstallations(installations);
        }
	
    }

	public static class BuckminsterInstaller extends DownloadFromUrlInstaller {

		private boolean update;
		
		@DataBoundConstructor
		public BuckminsterInstaller(String id, boolean update) {
			super(id);
			this.update = update;
		}

		@Override
		protected FilePath findPullUpDirectory(FilePath root)
				throws IOException, InterruptedException {
			return null;
		}
		
		public boolean isUpdate() {
			return update;
		}
		
		@Override
		public FilePath performInstallation(ToolInstallation tool, Node node,
				TaskListener log) throws IOException, InterruptedException {
			FilePath director = super.performInstallation(tool, node, log);
			FilePath buckminsterDir = director.child("buckminster");
			FilePath directorDir = director.child("director");
	        BuckminsterInstallable inst = (BuckminsterInstallable) getInstallable();
			if(buckminsterDir.exists())
			{
		    	FilePath executableWin = buckminsterDir.child("buckminster.bat");
		    	FilePath executableUnix = buckminsterDir.child("buckminster");
		    	if(executableUnix.exists() || executableWin.exists())
		    	{
		    		if(isUpdate())
		    		{
		    			//it exists and should be updated so execute the update script
		    			log.getLogger().println("Checking for Buckminster Updates");
		    			Map<String, Set<String>> installedFeatures = readInstalledFeatures(buckminsterDir, log);
		    			String command = CommandLineBuilder.createUpdateScript(inst, director, node, log, installedFeatures);
		    			executeScript(node, log, directorDir, command);
		    	        writeInstallationDetails(node,log,buckminsterDir,inst);
		    		}
		    		return buckminsterDir;	
		    	}
			}

			//the tool did not exist, so we install it freshly
	        String command = CommandLineBuilder.createInstallScript(inst, director, node, log);
	        executeScript(node, log, buckminsterDir, command);
	        writeInstallationDetails(node,log,buckminsterDir,inst);
	        return buckminsterDir;
			
		}

		private void writeInstallationDetails(Node node, TaskListener log,
				FilePath buckminsterDir, BuckminsterInstallable inst) throws InterruptedException, IOException {
			FilePath installedFeatures = buckminsterDir.child(".installedFeatures");
			StringBuilder installed = new StringBuilder();
			for (Repository repo : inst.repositories) {
				installed.append(repo.url);
				installed.append("\n");
				for (Feature feature : repo.features) {
					installed.append("-");
					installed.append(feature.id);
					installed.append("\n");
				}
			}
			try {
				installedFeatures.write(installed.toString(), "UTF-8");
			} catch (InterruptedException e) {
				installedFeatures.delete();
				throw e;
			}
			
		}
		
		/**
		 * reads the contents of DIRECTOR_DIR/.installedFeatures and returns a map that 
		 * contains the repository url as key and the set of features installed from that url as value
		 * @param log 
		 * @return
		 * @throws IOException 
		 * @throws InterruptedException 
		 */
		private Map<String, Set<String>> readInstalledFeatures(FilePath buckminsterDir, TaskListener log) throws IOException, InterruptedException
		{
			Map<String, Set<String>> installed = new HashMap<String, Set<String>>();
			FilePath installedFeatures = buckminsterDir.child(".installedFeatures");
			if(!installedFeatures.exists())
			{
				String message = "{0} is missing. This file contains the information which features have already been installed into buckminster. The Update will not be accurate without this file.";
				message = MessageFormat.format(message, installedFeatures.toURI().getPath());
				log.error(message);
				return installed;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(installedFeatures.read(),"UTF-8"));
			String s = null;
			String url = null;
			Set<String> features = new HashSet<String>();
			while((s=reader.readLine())!=null)
			{
				if(s.startsWith("-"))
				{
					features.add(s.substring(1));
				}
				else
				{
					if(url!=null && features.size()>0)
					{
						installed.put(url, features);
						features = new HashSet<String>();
					}
					url = s;
				}
			}
			if(url!=null && features.size()>0)
			{
				installed.put(url, features);
				features = new HashSet<String>();
			}
			url = s;
			return installed;
		}

		private void executeScript(Node node, TaskListener log,
				FilePath directorDir, String command) throws IOException,
				InterruptedException {
			FilePath script = directorDir.createTextTempFile("hudson", ".sh", command);
	        try {
	            String[] cmd = {"sh", "-e", script.getRemote()};
	            int r = node.createLauncher(log).launch().cmds(cmd).stdout(log).pwd(directorDir).join();
	            if (r != 0) {
	                throw new IOException("Command returned status " + r);
	            }
	        } finally {
	            script.delete();
	        }
		}
		

		@Extension
		public static final class DescriptorImpl extends
				DownloadFromUrlInstaller.DescriptorImpl<BuckminsterInstaller> {
			public String getDisplayName() {
				return "Install from Eclipse.org and Cloudsmith.com";
			}

			@Override
			public boolean isApplicable(
					Class<? extends ToolInstallation> toolType) {
				return toolType == BuckminsterInstallation.class;
			}

			
			@Override
			protected Downloadable createDownloadable() {
				return new Downloadable(getId()) {
					
				    public TextFile getDataFile() {
				    	//use a delagating text file to allow user to override the server json with a custom one.
				    	TextFile updateFile = super.getDataFile();
				    	TextFile userOverride = new TextFile(new File(Hudson.getInstance().getRootDir(),"userContent/buckminster/buckminster.json"));
				        return new ReadDelegatingTextFile(updateFile, userOverride);
				    }
				};
			}
			@Override
			public List<? extends Installable> getInstallables()
					throws IOException {
	            JSONObject d = Downloadable.get(getId()).getData();
	            if(d==null)     return Collections.emptyList();
	            return Arrays.asList(((BuckminsterInstallableList)JSONObject.toBean(d,BuckminsterInstallableList.class)).buckminsters);

			}
			
		}

	}
    
}
