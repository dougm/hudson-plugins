package hudson.plugins.polarion;

import hudson.model.Descriptor;
import hudson.model.AbstractProject;
import hudson.scm.EditType;
import hudson.scm.RepositoryBrowser;
import hudson.scm.SubversionChangeLogSet.LogEntry;
import hudson.scm.SubversionChangeLogSet.Path;
import hudson.scm.SubversionRepositoryBrowser;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.text.MessageFormat;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
/**
 * {@link SubversionRepositoryBrowser} that produces links to the Polarion Web Client for SVN
 *
 * @author Jonny Wray
 */
public class PolarionRepositoryBrowser extends SubversionRepositoryBrowser {
	
	private static final String CHANGE_SET_FORMAT = "revisionDetails.jsp?location=/&rev=%d";
	private static final String DIFF_FORMAT = "changedResource.jsp?location=/&url=%s&rev=%d&action=%s";
	private static final String FILE_FORMAT = "fileContent.jsp?location=/&url=%s";
	
	private static final Map editTypeMap = new HashMap();
	static{
		editTypeMap.put(EditType.ADD, "add");
		editTypeMap.put(EditType.EDIT, "modify");
		editTypeMap.put(EditType.DELETE, "delete");
		// no replace in EditType which polarion has an action=replace for.
	};
	public final URL url;

	/**
     * @stapler-constructor
     */
    public PolarionRepositoryBrowser(URL url) throws MalformedURLException {
		this.url = normalizeToEndWithSlash(url);
	}

    @Override
    public URL getDiffLink(Path path) throws IOException {
		if(!editTypeMap.containsKey(path.getEditType())){
			return null;
		}
		String editType = (String)editTypeMap.get(path.getEditType());
		return new URL(url, String.format(DIFF_FORMAT, path.getValue(), path.getLogEntry().getRevision(), editType));
    }

    @Override
    public URL getFileLink(Path path) throws IOException {
    	return new URL(url, String.format(FILE_FORMAT, path.getValue()));
    }

    @Override
    public URL getChangeSetLink(LogEntry changeSet) throws IOException {
    	return new URL(url, String.format(CHANGE_SET_FORMAT, changeSet.getRevision()));
    }

    public DescriptorImpl getDescriptor() {
        return DESCRIPTOR;
    }

    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public static final class DescriptorImpl extends Descriptor<RepositoryBrowser<?>> {
        public DescriptorImpl() {
            super(PolarionRepositoryBrowser.class);
        }

        public String getDisplayName() {
            return "Polarion Web Client";
        }

        public PolarionRepositoryBrowser newInstance(StaplerRequest req) throws FormException {
		   return req.bindParameters(PolarionRepositoryBrowser.class, "polarion.");
        }
    }
}