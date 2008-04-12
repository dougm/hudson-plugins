/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.ldap;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import com.mockobjects.naming.directory.MockDirContext;

public class MockDirContextFactory implements InitialContextFactory {

    private static final ConcurrentMap<String, MockDirContext> INSTANCES = new ConcurrentHashMap<String, MockDirContext>();

    // public static final MockDirContext INSTANCE = new MockDirContext();

    public static final String NAME = MockDirContextFactory.class.getName();

    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return getContext((String) environment.get(Context.PROVIDER_URL));
    }

    public static MockDirContext getContext(String url) {
        System.out.println("requesting - " + url);
        INSTANCES.putIfAbsent(url, new MockDirContext());
        MockDirContext mockContext = INSTANCES.get(url);
        System.out.println("in factory: " + mockContext);
        return mockContext;
    }

}
