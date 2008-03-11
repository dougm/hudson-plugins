package com.onedash.common;

import com.onedash.common.api.Namer;
import com.onedash.common.impl.NamerImpl;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 13-Jan-2008 21:03:41
 */
public class Factory {
    private static final Factory INSTANCE = new Factory();
    /** Do not instantiate Factory. */
    private Factory() {

    }

    /**
     * Getter for property 'instance'.
     *
     * @return Value for property 'instance'.
     */
    public static Factory getInstance() {
        return INSTANCE;
    }

    public Namer newNamer() {
        return new NamerImpl();
    }
}
