/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

import org.junit.Assert;
import org.junit.Test;

public class ConversionTest {

    @Test
    public void testSimple() {
        Configuration config = new Configuration("(.*) (.*)", "%s.%s@test.com");
        Assert.assertEquals("justin.edelson@test.com", config.findMailAddressFor("justin edelson"));
    }

    @Test
    public void testMulti() {
        MultiConfiguration config = new MultiConfiguration();
        config.addConfiguration(new Configuration("(.*) (.*) (.*)", "%s.%s-%s@test.com"));
        config.addConfiguration(new Configuration("(.*) (.*)", "%s.%s@test.com"));
        Assert.assertEquals("justin.edelson@test.com", config.findMailAddressFor("justin edelson"));
        Assert.assertEquals("justin.c-edelson@test.com", config
                .findMailAddressFor("justin c edelson"));
    }
}
