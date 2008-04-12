/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.ldap;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

public class BadConfigLoadTest extends TestCase {

    public void testBadConfig() throws IOException {
        PluginImpl pi = new PluginImpl();
        Configuration config = pi.loadConfiguration();
        assertFalse(config.isValid());
    }

    public void testLdapMailAddressResolver() throws Exception {
        PluginImpl pi = new PluginImpl();
        Configuration config = pi.loadConfiguration();
        LdapMailAddressResolver resolver = new LdapMailAddressResolver(config);
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
