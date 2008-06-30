/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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

    public String getEmailAddressPattern() {
        return emailAddressPattern;
    }

    public String getUserNameExpression() {
        return userNameExpression;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid() {
        getUserNamePattern();
        return (userNamePattern != null) && (emailAddressPattern != null);
    }

    public void setEmailAddressPattern(String emailAddressPattern) {
        this.emailAddressPattern = emailAddressPattern;
    }

    public void setUserNameExpression(String userNameExpression) {
        this.userNameExpression = userNameExpression;
    }

    /**
     * If necessary, compile the userNameExpression property into a RegEx
     * pattern.
     * 
     * @return the compiled Pattern.
     */
    protected Pattern getUserNamePattern() {
        if (userNamePattern == null && userNameExpression != null) {
            try {
                userNamePattern = Pattern.compile(userNameExpression);
            } catch (PatternSyntaxException e) {
                LOGGER.log(Level.WARNING, "Bad username expression: " + userNameExpression, e);
            }
        }
        return userNamePattern;
    }

    /**
     * {@inheritDoc}
     */
    public String findMailAddressFor(String userName) {
        if (isValid()) {
            Matcher matcher = getUserNamePattern().matcher(userName);
            if (matcher.matches()) {
                int groupCount = matcher.groupCount();
                // This array is declared as an Object[] to ensure it's passed
                // correctly via varargs.
                Object[] parts = new String[groupCount + 1];
                for (int i = 0; i < groupCount; i++) {
                    parts[i] = matcher.group(i + 1);
                }
                return String.format(getEmailAddressPattern(), parts);
            }
        } else {
            LOGGER
                    .warning(String
                            .format(
                                    "RegExMailAddressResolver configuration for regex %s and email pattern %s is not valid.",
                                    getUserNameExpression(), getEmailAddressPattern()));
        }
        return null;
    }

}
