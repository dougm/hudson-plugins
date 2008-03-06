package hudson.plugins.googlecode;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GoogleCodeProjectPropertyTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGoogleCodeProjectPropertyNotEndingWithSlash() {
        GoogleCodeProjectProperty property = new GoogleCodeProjectProperty("http://code.google.com/p/project");
        assertEquals("http://code.google.com/p/project/", property.googlecodeWebsite);
    }

    @Test
    public void testGoogleCodeProjectPropertyEndingWithSlash() {
        GoogleCodeProjectProperty property = new GoogleCodeProjectProperty("http://code.google.com/p/project/");
        assertEquals("http://code.google.com/p/project/", property.googlecodeWebsite);
    }

}
