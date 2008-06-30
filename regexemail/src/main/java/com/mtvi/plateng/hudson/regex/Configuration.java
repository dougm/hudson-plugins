/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;

/**
 * Class to store configuration for the plugin.
 * 
 * @author justinedelson
 * 
 */
public class Configuration implements IConfiguration {

    /**
     * A logger object.
     */
    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

    /**
     * The expression used to parse a user's username. Expected to be a regular
     * expression.
     */
    private String userNameExpression;

    /**
     * The format string used to create an email address.
     * 
     * @see http://java.sun.com/j2se/1.5.0/docs/api/java/util/Formatter.html#syntax
     */
    private String emailAddressPattern;

    /**
     * The compiled regular expression Pattern.
     */
    private Pattern userNamePattern;

    /**
     * Zero-argument constructor.
     */
    public Configuration() {
    }

    /**
     * Constructor with two arguments.
     * 
     * @param userNameExpression
     *            the user name expression (gets compiled to a regex)
     * @param emailAddressPattern
     *            the email address pattern
     */
    public Configuration(String userNameExpression, String emailAddressPattern) {
        this.userNameExpression = userNameExpression;
        this.emailAddressPattern = emailAddressPattern;
        getUserNamePattern();
    }

    /**
     * {@inheritDoc}
     */
    public String findMailAddressFor(String userName) {
        if (isValid()) {
            LOGGER.info(String.format("Attempting to match %s with regex %s", userName,
                    userNameExpression));
            Matcher matcher = getUserNamePattern().matcher(userName);
            if (matcher.matches()) {
                int groupCount = matcher.groupCount();
                // This array is declared as an Object[] to ensure it's passed
                // correctly via varargs.
                Object[] parts = new String[groupCount + 1];
                for (int i = 0; i < groupCount; i++) {
                    parts[i] = matcher.group(i + 1);
                }
                String emailAddress = String.format(emailAddressPattern, parts);
                LOGGER.info(String.format("Match for %s with regex %s, produced %s", userName,
                        userNameExpression, emailAddress));
                return emailAddress;
            } else {
                LOGGER.info(String.format("No match for %s with regex %s", userName,
                        userNameExpression));

                return null;
            }
        } else {
            LOGGER
                    .warning(String
                            .format(
                                    "RegExMailAddressResolver configuration for regex %s and email pattern %s is not valid.",
                                    userNameExpression, emailAddressPattern));
            return null;
        }
    }

    /**
     * Configuration is valid if the user name pattern can be compiled and the
     * email address pattern isn't blank.
     * 
     * {@inheritDoc}
     */
    public boolean isValid() {
        getUserNamePattern();
        return (userNamePattern != null) && (StringUtils.isNotBlank(emailAddressPattern));
    }

    /**
     * If necessary, compile the userNameExpression property into a RegEx
     * pattern.
     * 
     * @return the compiled Pattern.
     */
    protected Pattern getUserNamePattern() {
        if (userNamePattern == null && StringUtils.isNotBlank(userNameExpression)) {
            try {
                userNamePattern = Pattern.compile(userNameExpression);
            } catch (PatternSyntaxException e) {
                LOGGER.log(Level.WARNING, "Bad username expression: " + userNameExpression, e);
            }
        }
        return userNamePattern;
    }
}
