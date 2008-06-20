/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

public class BadConfigLoadTest extends TestCase {

    public void testBadConfig() throws IOException {
        PluginImpl pi = new PluginImpl();
        Configuration config = pi.loadConfiguration();
        assertFalse(config.isValid());
    }

    public void testRegexMailAddressResolver() throws Exception {
        PluginImpl pi = new PluginImpl();
        Configuration config = pi.loadConfiguration();
        RegexMailAddressResolver resolver = new RegexMailAddressResolver(config);
        assertNull(resolver.findMailAddressFor("username"));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        HudsonUtil.hudson.getRootUrl();
    }

    @Override
    protected void tearDown() throws Exception {
        HudsonUtil.hudson.cleanUp();
        FileUtils.deleteDirectory(HudsonUtil.root);
        super.tearDown();
    }
}
