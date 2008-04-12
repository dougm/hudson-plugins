/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.ldap;

import hudson.Plugin;
import hudson.XmlFile;
import hudson.model.Hudson;
import hudson.tasks.MailAddressResolver;

import java.io.File;
import java.io.IOException;

/**
 * Entry point of a plugin.
 * 
 * <p>
 * There must be one {@link Plugin} class in each plugin. See javadoc of
 * {@link Plugin} for more about what can be done on this class.
 * 
 * @author edelsonj
 */
public class PluginImpl extends Plugin {

    /**
     * Plugin lifecycle method. Loads configuration and adds configured instance
     * of LdapMailAddressResolver to MailAddressResolver list.
     * 
     * @see hudson.Plugin#start()
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        Configuration config = loadConfiguration();
        MailAddressResolver.LIST.add(new LdapMailAddressResolver(config));
    }

    /**
     * Loads confiugration file from
     * com.mtvi.plateng.hudson.ldap.LdapMailAddressResolver.xml
     * 
     * @return a populated Configuration object
     * @throws IOException
     */
    protected Configuration loadConfiguration() throws IOException {
        Hudson hudson = Hudson.getInstance();
        File rootDirectory = hudson.getRootDir();
        String fileName = LdapMailAddressResolver.class.getName() + ".xml";
        XmlFile xmlFile = new XmlFile(Hudson.XSTREAM, new File(rootDirectory, fileName));
        Configuration config = null;
        if (xmlFile.exists()) {
            config = (Configuration) xmlFile.read();
        } else {
            config = new Configuration();
        }
        return config;
    }
}
