/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.ldap;

import hudson.model.User;
import hudson.tasks.MailAddressResolver;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * Implementation of hudson.tasks.MailAddressResolver that looks up the email
 * address for a user based on information in an LDAP directory.
 * 
 * @author justinedelson
 * 
 */
public class LdapMailAddressResolver extends MailAddressResolver {

    /**
     * A logger object.
     */
    private static final Logger LOGGER = Logger.getLogger(LdapMailAddressResolver.class.getName());

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
    public LdapMailAddressResolver(Configuration config) {
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
     * Look up the email address for a user in the directory.
     * 
     * @param userName
     *            the user's username, generally corresponds to the uid
     *            attribute.
     * @return the corresponding email address from the directory, or null if
     *         none can be found.
     */
    protected String findMailAddressFor(String userName) {
        if (configuration.isValid()) {
            try {
                Hashtable<String, String> env = new Hashtable<String, String>();
                env.put(Context.INITIAL_CONTEXT_FACTORY, configuration
                        .getInitialContextFactoryName());
                env.put(Context.PROVIDER_URL, configuration.getServer());

                if (configuration.isBindCredentialsProvided()) {
                    env.put(Context.SECURITY_PRINCIPAL, configuration.getBindDN());
                    env.put(Context.SECURITY_CREDENTIALS, configuration.getBindPassword());
                }
                DirContext ctx = new InitialDirContext(env);
                Attributes attrs = ctx.getAttributes(configuration.makeUserDN(userName),
                        new String[] { configuration.getEmailAttribute() });
                Attribute attr = attrs.get("mail");
                if (attr != null) {
                    return (String) attr.get();
                } else {
                    return null;
                }
            } catch (NamingException e) {
                LOGGER.log(Level.SEVERE, "Unable to run LDAP query", e);
            }
        }
        return null;
    }
}
