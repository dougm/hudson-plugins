package hudson.plugins.googlecode;

import static org.junit.Assert.*;

import java.net.URL;

import hudson.scm.SubversionChangeLogSet.LogEntry;
import hudson.scm.SubversionChangeLogSet.Path;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

public class GoogleCodeRepositoryBrowserTest {

    private GoogleCodeRepositoryBrowser googleCodeRepositoryBrowser;
    
    private Mockery context;
    private GoogleCodeProjectProperty.PropertyRetriever urlRetriever;
    private GoogleCodeProjectProperty property;

    private LogEntry entry;
    private Path path;

    @Before
    public void setUp() throws Exception {
        property = new GoogleCodeProjectProperty("http://code.google.com/p/mockito/");
        
        entry = new LogEntry();
        path = new Path();
        path.setLogEntry(entry);
        
        context = new Mockery();
        urlRetriever = context.mock(GoogleCodeProjectProperty.PropertyRetriever.class);
        context.checking(new Expectations() { {
            one(urlRetriever).getProperty(entry); will(returnValue(property));
        } });
        
        googleCodeRepositoryBrowser = new GoogleCodeRepositoryBrowser(urlRetriever);
    }

    @Test
    public void testGetDiffLinkPath() throws Throwable {
        entry.setRevision(448);
        path.setValue("/trunk/src/org/mockito/ArgumentMatcher.java");
        path.setAction("EDIT");
        URL actual = googleCodeRepositoryBrowser.getDiffLink(path);
        URL expected = new URL("http://code.google.com/p/mockito/source/diff?r=448&format=side&path=/trunk/src/org/mockito/ArgumentMatcher.java");
        assertEquals(expected, actual);
        context.assertIsSatisfied();
    }

    @Test
    public void testGetDiffLinkPathForAddAction() throws Throwable {
        path.setAction("ADD");
        URL actual = googleCodeRepositoryBrowser.getDiffLink(path);
        assertNull(actual);
    }

    @Test
    public void testGetFileLinkPath() throws Throwable {
        entry.setRevision(446);
        path.setValue("/trunk/src/org/mockito/ArgumentMatcher.java");
        path.setAction("EDIT");
        URL actual = googleCodeRepositoryBrowser.getFileLink(path);
        URL expected = new URL("http://code.google.com/p/mockito/source/browse/trunk/src/org/mockito/ArgumentMatcher.java?r=446#1");
        assertEquals(expected, actual);
        context.assertIsSatisfied();
    }

    @Test
    public void testGetChangeSetLinkLogEntry() throws Throwable {
        entry.setRevision(450);
        URL actual = googleCodeRepositoryBrowser.getChangeSetLink(entry);
        URL expected = new URL("http://code.google.com/p/mockito/source/detail?r=450");
        assertEquals(expected, actual);
        context.assertIsSatisfied();
    }
}
