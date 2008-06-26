/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

import hudson.model.User;
import hudson.tasks.MailAddressResolver;

import java.util.logging.Logger;
import java.util.regex.Matcher;

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
    private Configuration configuration;

    /**
     * Build an instance wrapping a Configuration object.
     * 
     * @param config
     *            the Configuration object
     */
    public RegexMailAddressResolver(Configuration config) {
        configuration = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String findMailAddressFor(User user) {
        return findMailAddressFor(user.getDisplayName());
    }

    /**
     * Transform a username into a email address using regular expressions and
     * java.lang.String.format().
     * 
     * @param userName
     *            the user's username
     * @return the corresponding email address
     */
    protected String findMailAddressFor(String userName) {
        if (configuration.isValid()) {
            Matcher matcher = configuration.getUserNamePattern().matcher(userName);
            if (matcher.matches()) {
                int groupCount = matcher.groupCount();
                // This array is declared as an Object[] to ensure it's passed
                // correctly via varargs.
                Object[] parts = new String[groupCount + 1];
                for (int i = 0; i < groupCount; i++) {
                    parts[i] = matcher.group(i + 1);
                }
                return String.format(configuration.getEmailAddressPattern(), parts);
            }
        }
        return null;
    }
}
