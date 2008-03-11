package com.onedash.hello;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 13-Jan-2008 20:36:06
 */
public class HelloFest {
    private Hello instance;

    @Before
    public void setUp() {
        instance = new Hello();
    }

    @Test
    public void smoke() {
        assertEquals("Hello John", instance.sayHello("John"));
    }

    @Test
    public void negative() {
        assertEquals("Hello", instance.sayHello(""));
    }
}
