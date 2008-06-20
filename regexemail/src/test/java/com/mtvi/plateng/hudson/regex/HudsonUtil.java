/**
 * Copyright (c) 2008, MTV Networks
 */

package com.mtvi.plateng.hudson.regex;

import hudson.model.Hudson;

import java.io.File;
import java.io.IOException;

import com.mockobjects.servlet.MockServletContext;

public class HudsonUtil {

    public static final Hudson hudson;
    public static final File root;
    public static final MockServletContext servletContext;

    static {
        root = new File("target/test-hudson");
        root.mkdirs();
        servletContext = new MockServletContext();
        Hudson temp = null;
        try {
            temp = new Hudson(root, servletContext);
        } catch (IOException e) {
            System.err.println("Exception creating Hudson instance.");
            e.printStackTrace();
        }
        hudson = temp;
    }

}
