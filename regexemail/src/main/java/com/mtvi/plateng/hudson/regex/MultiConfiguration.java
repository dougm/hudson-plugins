/**
 * 
 */
package com.mtvi.plateng.hudson.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 * @author justinedelson
 * 
 */
public class MultiConfiguration implements IConfiguration {

    /**
     * A logger object.
     */
    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

    private List<ConfigurationDetails> configurationDetails;

    /**
     * {@inheritDoc}
     */
    public String findMailAddressFor(String userName) {
        for (ConfigurationDetails details : configurationDetails) {
            if (details.isValid()) {
                LOGGER.info(String.format("Attempting to match %s with regex %s", userName, details
                        .getUserNameExpression()));
                Matcher matcher = details.getUserNamePattern().matcher(userName);
                if (matcher.matches()) {
                    int groupCount = matcher.groupCount();
                    // This array is declared as an Object[] to ensure it's
                    // passed correctly via varargs.
                    Object[] parts = new String[groupCount + 1];
                    for (int i = 0; i < groupCount; i++) {
                        parts[i] = matcher.group(i + 1);
                    }
                    String emailAddress = String.format(details.getEmailAddressPattern(), parts);
                    LOGGER.info(String.format("Match for %s with regex %s, produced", userName,
                            details.getUserNameExpression(), emailAddress));
                    return emailAddress;
                } else {
                    LOGGER.info(String.format("No match for %s with regex %s", userName, details
                            .getUserNameExpression()));
                }

            } else {
                LOGGER.warning(String.format(
                        "RegExMailAddressResolver configuration for pattern %s is not valid.",
                        details.getUserNameExpression()));
            }
        }
        return null;
    }

    protected synchronized List<ConfigurationDetails> getConfigurationDetails() {
        if (configurationDetails == null) {
            configurationDetails = new ArrayList<ConfigurationDetails>();
            configurationDetails.add(new ConfigurationDetails());
        }
        return configurationDetails;
    }

    protected synchronized void addConfigurationDetails(ConfigurationDetails details) {
        if (configurationDetails == null) {
            configurationDetails = new ArrayList<ConfigurationDetails>();
            configurationDetails.add(details);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid() {
        boolean retval = false;
        for (ConfigurationDetails details : getConfigurationDetails()) {
            retval = retval || details.isValid();
        }
        return retval;
    }

}
