/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

/**
 * @author edelsonj
 * 
 */
public class MultiConfigLoadTest extends TestCase {

    public void testGoodConfig() throws IOException {
        PluginImpl pi = new PluginImpl();
        IConfiguration config = pi.loadConfiguration();
        assertEquals(MultiConfiguration.class, config.getClass());
        assertTrue(config.isValid());

    }

    /*
     * public void testFoo() { Configuration config = new Configuration();
     * ConfigurationDetails details = new ConfigurationDetails();
     * details.setEmailAddressPattern("foo");
     * details.setUserNameExpression("bar");
     * config.addConfigurationDetails(details); details = new
     * ConfigurationDetails(); details.setEmailAddressPattern("foo2");
     * details.setUserNameExpression("bar2");
     * config.addConfigurationDetails(details); Hudson.XSTREAM.toXML(config,
     * System.out); }
     */

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FileUtils.copyFile(new File("src/test/resources/unit/multi-config.xml"), new File(
                HudsonUtil.root, RegexMailAddressResolver.class.getName() + ".xml"));
    }

    @Override
    protected void tearDown() throws Exception {
        HudsonUtil.hudson.cleanUp();
        FileUtils.deleteDirectory(HudsonUtil.root);
        super.tearDown();
    }
}
