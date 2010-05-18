package ch.ethz.origo;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.reset;
import static org.easymock.classextension.EasyMock.verify;
import hudson.model.AbstractBuild;
import hudson.model.FreeStyleProject;
import hudson.tasks.Builder;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.xmlrpc.XmlRpcException;
import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.FailureBuilder;
import org.jvnet.hudson.test.UnstableBuilder;

public class OrigoIssuePublisherTest extends HudsonTestCase {

	private static final String API_URL = "API_URL";
	private static final String PROJECT_NAME = "PROJECT_NAME";
	private static final String USER_KEY = "USER_KEY";
	private static final String ISSUE_SUBJECT = "ISSUE_SUBJECT";
	private static final String ISSUE_TAG = "ISSUE_TAG";
	private static final boolean ISSUE_PRIVATE = false;
	private static final String SESSION = "SESSION";
	private static final Integer PROJECT_ID = 13;
	private static final String HUDSON_URL = "HUDSON_URL";
	private static final Integer ISSUE_ID = 43;

	private OrigoApiClient ORIGO_API_CLIENT_MOCK = createMock("ORIGO_API_CLIENT_MOCK", OrigoApiClient.class);
	private Object[] MOCKS = new Object[] { ORIGO_API_CLIENT_MOCK };

	public void testNewFailure() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		expectNewIssue(1);
		replay(MOCKS);

		publisher.perform(createOneBuild(new FailureBuilder()), null, null);
		verify(MOCKS);
	}

	public void testSuccessThenFailure() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		expectNewIssue(2);
		replay(MOCKS);

		publisher.perform(createTwoBuilds(null, new FailureBuilder()), null, null);
		verify(MOCKS);
	}

	public void testNewSuccessful() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		replay(MOCKS);

		publisher.perform(createOneBuild(null), null, null);
		verify(MOCKS);
	}

	public void testSuccessThenSuccess() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		replay(MOCKS);

		publisher.perform(createTwoBuilds(null, null), null, null);
		verify(MOCKS);
	}

	public void testFailureThenFailure() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		replay(MOCKS);

		publisher.perform(createTwoBuilds(new FailureBuilder(), new FailureBuilder()), null, null);
		verify(MOCKS);
	}

	public void testFailureThenSuccess() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		expectCloseIssue();
		replay(MOCKS);

		publisher.perform(createTwoBuilds(new FailureBuilder(), null), null, null);
		verify(MOCKS);
	}

	
	
	
	public void testNewUnstable() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		expectNewIssue(1);
		replay(MOCKS);

		publisher.perform(createOneBuild(new UnstableBuilder()), null, null);
		verify(MOCKS);
	}

	public void testSuccessThenUnstable() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		expectNewIssue(2);
		replay(MOCKS);

		publisher.perform(createTwoBuilds(null, new UnstableBuilder()), null, null);
		verify(MOCKS);
	}

	public void testUnstableThenUnstable() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		replay(MOCKS);

		publisher.perform(createTwoBuilds(new UnstableBuilder(), new UnstableBuilder()), null, null);
		verify(MOCKS);
	}

	public void testUnstableThenFailure() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		replay(MOCKS);

		publisher.perform(createTwoBuilds(new UnstableBuilder(), new FailureBuilder()), null, null);
		verify(MOCKS);
	}
	
	public void testFailureThenUnstable() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		replay(MOCKS);

		publisher.perform(createTwoBuilds(new FailureBuilder(), new UnstableBuilder()), null, null);
		verify(MOCKS);
	}
	
	public void testUnstableThenSuccess() throws Exception {
		OrigoIssuePublisher publisher = setupPublisher();

		reset(MOCKS);
		expectCloseIssue();
		replay(MOCKS);

		publisher.perform(createTwoBuilds(new UnstableBuilder(), null), null, null);
		verify(MOCKS);
	}
	
	
	private AbstractBuild<?, ?> createTwoBuilds(Builder first, Builder second) throws IOException,
			InterruptedException, ExecutionException {
		FreeStyleProject project = createFreeStyleProject();
		if (first != null) {
			project.getBuildersList().add(first);
		}
		project.scheduleBuild2(0).get();
		project.getBuildersList().clear();
		if (second != null) {
			project.getBuildersList().add(second);
		}
		return project.scheduleBuild2(0).get();
	}

	private AbstractBuild<?, ?> createOneBuild(Builder first) throws IOException, InterruptedException,
			ExecutionException {
		FreeStyleProject project = createFreeStyleProject();
		if (first != null) {
			project.getBuildersList().add(first);
		}

		return project.scheduleBuild2(0).get();
	}

	private String buildDescription(int i) {
		return "Build failed see: " + createLink(i);
	}

	private void expectNewIssue(int build) throws XmlRpcException {
		expect(ORIGO_API_CLIENT_MOCK.login(USER_KEY, OrigoIssuePublisher.APPLICATION_KEY)).andReturn(SESSION);
		expect(ORIGO_API_CLIENT_MOCK.retrieveProjectId(SESSION, PROJECT_NAME)).andReturn(PROJECT_ID);
		ORIGO_API_CLIENT_MOCK.addIssue(SESSION, PROJECT_ID, ISSUE_SUBJECT, buildDescription(build), "status::open,"
				+ ISSUE_TAG, ISSUE_PRIVATE);
	}

	private void expectCloseIssue() throws XmlRpcException {
		expect(ORIGO_API_CLIENT_MOCK.login(USER_KEY, OrigoIssuePublisher.APPLICATION_KEY)).andReturn(SESSION);
		expect(ORIGO_API_CLIENT_MOCK.retrieveProjectId(SESSION, PROJECT_NAME)).andReturn(PROJECT_ID);
		HashMap<String, String> searchArgs = new HashMap<String, String>();
		searchArgs.put("status", "open");
		searchArgs.put("tags", ISSUE_TAG);
		expect(ORIGO_API_CLIENT_MOCK.searchIssue(SESSION, PROJECT_ID, searchArgs)).andReturn(
				new Object[] { Collections.singletonMap("issue_id", ISSUE_ID) });
		ORIGO_API_CLIENT_MOCK.extendedCommentIssue(SESSION, PROJECT_ID, ISSUE_ID, "Build fixed see: " + createLink(2),
				"status::closed," + ISSUE_TAG);
	}

	private String createLink(int i) {
		return HUDSON_URL + "job/test0/" + i + "/";
	}

	private OrigoIssuePublisher setupPublisher() {
		OrigoIssuePublisher publisher = new OrigoIssuePublisher(API_URL, PROJECT_NAME, USER_KEY, ISSUE_SUBJECT,
				ISSUE_TAG, ISSUE_PRIVATE, ORIGO_API_CLIENT_MOCK);
		publisher.getDescriptor().setHudsonUrl(HUDSON_URL);
		return publisher;
	}
}
