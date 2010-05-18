package ch.ethz.origo;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * Client for the origo api, abstracts the xmlrpc interface.
 * 
 * @author Patrick Ruckstuhl
 */
public class OrigoApiClient {

	private static final Logger LOGGER = Logger.getLogger(OrigoApiClient.class.getName());

	private final XmlRpcClient client;

	/**
	 * Create with an apiUrl
	 * 
	 * @param apiUrl
	 *            url
	 */
	public OrigoApiClient(final URL apiUrl) {
		client = new XmlRpcClient();
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(apiUrl);
		client.setConfig(config);
	}

	/**
	 * Execute an xmlrpc call.
	 * 
	 * @param method
	 *            the method to call
	 * @param params
	 *            the parameters for the call
	 * @return the result of the call
	 * @throws XmlRpcException
	 *             if error occurs
	 */
	protected synchronized Object call(final String method, final Object... params) throws XmlRpcException {

		LOGGER.finest("Executing call " + method + " " + Arrays.toString(params));
		return client.execute(method, params);
	}

    /**
     * Login for a user
     * @param userKey the user key
     * @param applicationKey the application key
     * @return login
     * @throws XmlRpcException if error occurs
     */
    public String login(final String userKey, final String applicationKey) throws XmlRpcException {
        return (String) call("user.login_key", userKey, applicationKey);
    }

    /**
     * Retrieve the project id.
     * @param session a session
     * @param projectName name of the project
     * @return projectID
     * @throws XmlRpcException if error occurs
     */
    public Integer retrieveProjectId(final String session, final String projectName)
            throws XmlRpcException {
        return (Integer) call("project.retrieve_id", session, projectName);
    }

    /**
     * Search an issue.
     * @param session a session
     * @param projectId a project id
     * @param searchArgs search arguments
     * @return issues
     * @throws XmlRpcException if error occurs
     */
    public Object[] searchIssue(final String session, final Integer projectId, final HashMap<String, String> searchArgs)
            throws XmlRpcException {

        return (Object[]) call("issue.search", session, projectId, searchArgs);
    }

    /**
     * Extended comment for an issue.
     * @param session a session
     * @param projectId a project id
     * @param bugId a bug id
     * @param description description
     * @param tags tags to add/set
     * @throws XmlRpcException if error occurs
     */
    public void extendedCommentIssue(final String session, final Integer projectId, final Integer bugId,
                                     final String description, final String tags)
            throws XmlRpcException {
        call("issue.comment_extended_2", session, projectId, bugId, description, tags, 0, 0);
    }

    /**
     * Add a new issue.
     * @param session a session
     * @param projectId a project id
     * @param issueSubject subject of the issue
     * @param issueDescription description of the issue
     * @param issueTag tag of the issue
     * @param issuePrivate is the issue private?
     * @throws XmlRpcException if error occurs
     */
    public void addIssue(final String session, final Integer projectId, final String issueSubject,
                         final String issueDescription, final String issueTag, final Boolean issuePrivate)
            throws XmlRpcException {
        call("issue.add_2", session, projectId, issueSubject, issueDescription, issueTag, issuePrivate, 0, 0);
    }
	
}
