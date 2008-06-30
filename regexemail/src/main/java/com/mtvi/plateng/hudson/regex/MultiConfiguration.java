/**
 * Copyright (c) 2008, MTV Networks
 */

/**
 * 
 */
package com.mtvi.plateng.hudson.regex;

import java.util.ArrayList;
import java.util.List;

/**
 * A MultiConfiguration object wraps some number of Configuration objects and
 * will iterate through them (in order) to match a username and transform it to
 * an email address.
 * 
 * @author justinedelson
 * 
 */
public class MultiConfiguration implements IConfiguration {

    /**
     * The list of configuration objects contained.
     */
    private List<Configuration> configurations;

    /**
     * {@inheritDoc}
     */
    public String findMailAddressFor(String userName) {
        for (Configuration config : configurations) {
            String address = config.findMailAddressFor(userName);
            if (address != null) {
                return address;
            }
        }
        return null;
    }

    protected synchronized List<Configuration> getConfigurations() {
        if (configurations == null) {
            configurations = new ArrayList<Configuration>();
            configurations.add(new Configuration());
        }
        return configurations;
    }

    protected synchronized void addConfiguration(Configuration config) {
        if (configurations == null) {
            configurations = new ArrayList<Configuration>();
        }
        configurations.add(config);
    }

    /**
     * A MultiConfiguration object is valid if at least one of the underlying
     * Configuration objects is valid.
     * 
     * {@inheritDoc}
     */
    public boolean isValid() {
        boolean retval = false;
        for (Configuration config : getConfigurations()) {
            retval = retval || config.isValid();
        }
        return retval;
    }

}
