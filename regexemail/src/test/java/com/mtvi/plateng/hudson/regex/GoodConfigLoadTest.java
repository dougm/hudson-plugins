/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

import com.mtvi.plateng.hudson.regex.Configuration;
import com.mtvi.plateng.hudson.regex.RegexMailAddressResolver;
import com.mtvi.plateng.hudson.regex.PluginImpl;

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
                HudsonUtil.root, RegexMailAddressResolver.class.getName() + ".xml"));
    }

    @Override
    protected void tearDown() throws Exception {
        HudsonUtil.hudson.cleanUp();
        FileUtils.deleteDirectory(HudsonUtil.root);
        super.tearDown();
    }
}
