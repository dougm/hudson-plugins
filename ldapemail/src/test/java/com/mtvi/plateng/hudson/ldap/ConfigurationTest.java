/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.ldap;

import junit.framework.TestCase;

public class ConfigurationTest extends TestCase {

    public void testInitialContextFactoryName() {
        Configuration config = new Configuration();
        assertEquals(Configuration.DEFAULT_INITIAL_CONTEXT_FACTORY, config
                .getInitialContextFactoryName());

        config.setInitialContextFactoryName("foo");
        assertEquals("foo", config.getInitialContextFactoryName());
    }

    public void testValid() {
        Configuration config = new Configuration();
        assertFalse(config.isValid());
        config.setServer("foo");
        assertFalse(config.isValid());
        config.setBaseDN("foo");
        assertFalse(config.isValid());
        config.setSearchAttribute("foo");
        assertFalse(config.isValid());
        config.setEmailAttribute("foo");
        assertTrue(config.isValid());
    }

}
