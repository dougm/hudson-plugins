/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

/**
 * Interface defining common methods between different configuration
 * implementation classes. Normally I hate the I* naming convention for
 * interface, but in this case, we need to retain backwards compatibility. So
 * here we are.
 * 
 * @author justinedelson
 * 
 */
public interface IConfiguration {

    /**
     * Determine if this configuration object is valid.
     * 
     * @return true if this object is valid
     */
    public boolean isValid();

    /**
     * Transform a username into a email address using regular expressions and
     * java.lang.String.format().
     * 
     * @param userName
     *            the user's username
     * @return the corresponding email address
     */
    public String findMailAddressFor(String userName);
}
