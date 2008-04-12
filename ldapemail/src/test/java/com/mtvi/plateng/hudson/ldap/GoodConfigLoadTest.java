/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.ldap;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

public class GoodConfigLoadTest extends TestCase {

    public void testGoodConfig() throws IOException {
        PluginImpl pi = new PluginImpl();
        Configuration config = pi.loadConfiguration();
        assertTrue(config.isValid());

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FileUtils.copyFile(new File("src/test/resources/unit/config.xml"), new File(
                HudsonUtil.root, LdapMailAddressResolver.class.getName() + ".xml"));
    }

    @Override
    protected void tearDown() throws Exception {
        HudsonUtil.hudson.cleanUp();
        FileUtils.deleteDirectory(HudsonUtil.root);
        super.tearDown();
    }
}
