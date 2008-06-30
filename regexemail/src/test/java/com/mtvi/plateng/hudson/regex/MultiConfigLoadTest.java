/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author edelsonj
 * 
 */
public class MultiConfigLoadTest {

    @Test
    public void testGoodConfig() throws IOException {
        PluginImpl pi = new PluginImpl();
        IConfiguration config = pi.loadConfiguration();
        Assert.assertEquals(MultiConfiguration.class, config.getClass());
        Assert.assertTrue(config.isValid());

    }

    @Before
    public void setUp() throws Exception {
        FileUtils.copyFile(new File("src/test/resources/unit/multi-config.xml"), new File(
                HudsonUtil.root, RegexMailAddressResolver.class.getName() + ".xml"));
    }

    @After
    public void tearDown() throws Exception {
        HudsonUtil.hudson.cleanUp();
        FileUtils.deleteDirectory(HudsonUtil.root);
    }
}
