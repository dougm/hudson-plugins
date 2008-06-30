/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

import hudson.model.User;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

public class BadConfigLoadTest extends TestCase {

    public void testBadConfig() throws IOException {
        PluginImpl pi = new PluginImpl();
        Configuration config = (Configuration) pi.loadConfiguration();
        assertFalse(config.isValid());
    }

    public void testRegexMailAddressResolver() throws Exception {
        PluginImpl pi = new PluginImpl();
        Configuration config = (Configuration) pi.loadConfiguration();
        RegexMailAddressResolver resolver = new RegexMailAddressResolver(config);
        User u = User.get("username");
        assertNull(resolver.findMailAddressFor(u));
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
