/*
 * The MIT License
 * 
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., Kohsuke Kawaguchi, Bruce Chapman, Erik Ramfelt, Jean-Baptiste Quenot, Luca Domenico Milanesio
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ch.ethz.origo;

import hudson.Extension;
import hudson.Functions;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.tasks.Mailer;
import hudson.util.FormValidation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * {@link Publisher} that creates/updates an Origo issue based on the build
 * result.
 * 
 * @author Patrick Ruckstuhl
 */
public class OrigoIssuePublisher extends Notifier {

	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
		private String apiUrl;
		private String projectName;
		private String userKey;
		private String issueSubject;
		private String issueTag;
		private boolean issuePrivate = true;

		private String hudsonUrl;

		public DescriptorImpl() {
			load();
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
			req.bindParameters(this, "origo.issues.");
			// hudsonUrl is read from Mailer
			hudsonUrl = Mailer.descriptor().getUrl();
			save();
			return super.configure(req, formData);
		}

		public FormValidation doCheckApiUrl(@QueryParameter String value) {
			try {
				new URL(value);
				return FormValidation.ok();
			} catch (MalformedURLException e) {
				return FormValidation.error("Invalid url specified");
			}
		}

		public FormValidation doCheckIssueSubject(@QueryParameter String value) {
			if (StringUtils.isNotEmpty(value)) {
				return FormValidation.ok();
			} else {
				return FormValidation.error("Issue subject is mandatory");
			}
		}

		public FormValidation doCheckIssueTag(@QueryParameter String value) {
			if (StringUtils.isNotEmpty(value)) {
				return FormValidation.ok();
			} else {
				return FormValidation.error("Issue tag is mandatory");
			}
		}

		public FormValidation doCheckProjectName(@QueryParameter String value) {
			if (StringUtils.isNotEmpty(value)) {
				return FormValidation.ok();
			} else {
				return FormValidation.error("Project name is mandatory");
			}
		}

		public FormValidation doCheckUserKey(@QueryParameter String value) {
			if (StringUtils.isNotEmpty(value)) {
				return FormValidation.ok();
			} else {
				return FormValidation.error("User key is mandatory");
			}
		}

		public String getApiUrl() {
			return apiUrl;
		}

		@Override
		public String getDisplayName() {
			return "Origo Issues";
		}

		public String getHudsonUrl() {
			return hudsonUrl;
		}

		public String getIssueSubject() {
			return issueSubject;
		}

		public String getIssueTag() {
			return issueTag;
		}

		public String getProjectName() {
			return projectName;
		}

		public String getUserKey() {
			return userKey;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

		public boolean isIssuePrivate() {
			return issuePrivate;
		}

		@Override
		public Publisher newInstance(StaplerRequest req, JSONObject formData) throws FormException {
			if (hudsonUrl == null) {
				// if Hudson URL is not configured yet, infer some default
				hudsonUrl = Functions.inferHudsonURL(req);
				save();
			}
			return super.newInstance(req, formData);
		}

		public void setApiUrl(String apiUrl) {
			this.apiUrl = apiUrl;
		}

		public void setHudsonUrl(String hudsonUrl) {
			this.hudsonUrl = hudsonUrl;
		}

		public void setIssuePrivate(boolean issuePrivate) {
			this.issuePrivate = issuePrivate;
		}

		public void setIssueSubject(String issueSubject) {
			this.issueSubject = issueSubject;
		}

		public void setIssueTag(String issueTag) {
			this.issueTag = issueTag;
		}

		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}

		public void setUserKey(String userKey) {
			this.userKey = userKey;
		}
	}

	private static final Logger LOGGER = Logger.getLogger(OrigoIssuePublisher.class.getName());

	static final String APPLICATION_KEY = "KEYFORTHEORIGOHUDSONISSUESPLUGIN";

	private String apiUrl;
	private String projectName;
	private String userKey;
	private String issueSubject;
	private String issueTag;
	private boolean issuePrivate;
	private OrigoApiClient client;

	OrigoIssuePublisher(String apiUrl, String projectName, String userKey, String issueSubject, String issueTag,
			boolean issuePrivate, OrigoApiClient client) {
		super();
		this.apiUrl = apiUrl;
		this.projectName = projectName;
		this.userKey = userKey;
		this.issueSubject = issueSubject;
		this.issueTag = issueTag;
		this.issuePrivate = issuePrivate;
		this.client = client;
	}

	@DataBoundConstructor
	public OrigoIssuePublisher(String apiUrl, String projectName, String userKey, String issueSubject, String issueTag,
			boolean issuePrivate) {
		super();
		this.apiUrl = apiUrl;
		this.projectName = projectName;
		this.userKey = userKey;
		this.issueSubject = issueSubject;
		this.issueTag = issueTag;
		this.issuePrivate = issuePrivate;
	}

	public String getApiUrl() {
		return apiUrl;
	}
	
	@Override
	public DescriptorImpl getDescriptor() {
		return Hudson.getInstance().getDescriptorByType(OrigoIssuePublisher.DescriptorImpl.class);
	}

	public String getIssueSubject() {
		return issueSubject;
	}

	public String getIssueTag() {
		return issueTag;
	}

	public String getProjectName() {
		return projectName;
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.BUILD;
	}

	public String getUserKey() {
		return userKey;
	}

	public boolean isIssuePrivate() {
		return issuePrivate;
	}

	private OrigoApiClient createClient() throws MalformedURLException {
		if(client == null) {
			return new OrigoApiClient(new URL(apiUrl));
		}else{
			return client;
		}
	}
	
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {

		try {
			boolean isNewFailure = isNewFailure(build);
			boolean isRecovery = isRecovery(build);
			if (isNewFailure || isRecovery) {
				OrigoApiClient client= createClient();
				
				// login
				String session = client.login(userKey, APPLICATION_KEY);
				LOGGER.fine("Got session" + session);

				// get project id
				Integer projectId = client.retrieveProjectId(session, projectName);

				if (isNewFailure) {
					openNewIssue(build, client, session, projectId);
				} else if (isRecovery) {
					closeExistingIssue(build, client, session, projectId);
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unable to update origo issue.", e);
		}

		return true;
	}

	private void closeExistingIssue(AbstractBuild<?, ?> build, OrigoApiClient client, String session, Integer projectId)
			throws XmlRpcException {
		// search issue
		HashMap<String, String> searchArgs = new HashMap<String, String>();
		searchArgs.put("status", "open");
		searchArgs.put("tags", issueTag);
		Object[] issues = client.searchIssue(session, projectId, searchArgs);
		if (issues != null && issues.length == 1) {
			// close issue
			Map<Object, Object> issue = (Map<Object, Object>) issues[0];
			Integer issueId = (Integer) issue.get("issue_id");
			LOGGER.fine("Found issue with id " + issueId);

			String issueDescription = "Build fixed see: " + createLinkUrl(build);
			client.extendedCommentIssue(session, projectId, issueId, issueDescription, "status::closed," + issueTag);
		} else {
			LOGGER.warning("Did not find exactly one match.");
		}
	}

	private String createLinkUrl(AbstractBuild<?, ?> build) {
		return getDescriptor().getHudsonUrl() + build.getUrl();
	}

	private boolean isNewFailure(AbstractBuild<?, ?> build) {
		Result previousResult = build.getPreviousBuild() != null ? build.getPreviousBuild().getResult()
				: Result.SUCCESS;
		Result currentResult = build.getResult();
		return previousResult == Result.SUCCESS
				&& (currentResult == Result.FAILURE || build.getResult() == Result.UNSTABLE);
	}

	private boolean isRecovery(AbstractBuild<?, ?> build) {
		Result previousResult = build.getPreviousBuild() != null ? build.getPreviousBuild().getResult()
				: Result.SUCCESS;
		Result currentResult = build.getResult();
		return currentResult == Result.SUCCESS
				&& (previousResult == Result.FAILURE || build.getPreviousBuild().getResult() == Result.UNSTABLE);
	}

	private void openNewIssue(AbstractBuild<?, ?> build, OrigoApiClient client, String session, Integer projectId)
			throws XmlRpcException {
		// create issue
		String issueDescription = "Build failed see: " + createLinkUrl(build);
		System.out.println(issueDescription);
		client.addIssue(session, projectId, issueSubject, issueDescription, "status::open," + issueTag, issuePrivate);
	}
}