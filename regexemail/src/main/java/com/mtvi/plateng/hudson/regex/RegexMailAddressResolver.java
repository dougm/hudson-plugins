/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

import hudson.model.User;
import hudson.tasks.MailAddressResolver;

import java.util.logging.Logger;

/**
 * Implementation of hudson.tasks.MailAddressResolver that looks up the email
 * address for a user based on information in an LDAP directory.
 * 
 * @author justinedelson
 * 
 */
public class RegexMailAddressResolver extends MailAddressResolver {

    /**
     * A logger object.
     */
    private static final Logger LOGGER = Logger.getLogger(RegexMailAddressResolver.class.getName());

    /**
     * Configuration object encapsulating how to connect to the LDAP server.
     */
    private IConfiguration configuration;

    /**
     * Build an instance wrapping a Configuration object.
     * 
     * @param config
     *            the Configuration object
     */
    public RegexMailAddressResolver(IConfiguration config) {
        configuration = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String findMailAddressFor(User user) {
        return configuration.findMailAddressFor(user.getDisplayName());
    }

}
