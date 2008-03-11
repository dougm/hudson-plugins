package com.onedash.common.impl;

import com.onedash.common.api.Namer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 13-Jan-2008 21:05:35
 */
public class Namer2Impl implements Namer {
    private static final String[] FIRST_NAMES = {
            "Alan",
            "Brenda",
            "Clare",
            "David",
            "Eric",
            "Fran",
            "Gerr",
            "Harry",
            "Imelda",
            "Jo",
            "Karl",
            "Lee",
            "Mark",
            "Nora",
            "Orlaith",
            "Paul",
            "Quentin",
            "Rachel",
            "Simon",
            "Terry",
            "Ulick",
            "Vera",
            "William",
            "Xavier",
            "Yvonne",
            "Zeta"
    };

    private static final String[] SURNAMES = {
            "Adams",
            "Burke",
            "Carroll",
            "d'Arcy",
    };

    private static final Random entropy = new Random();

    /** {@inheritDoc} */
    public String newName() {
        synchronized (entropy) {
            return FIRST_NAMES[entropy.nextInt(FIRST_NAMES.length)]
                    + " "
                    + SURNAMES[entropy.nextInt(SURNAMES.length)];
        }
    }
}