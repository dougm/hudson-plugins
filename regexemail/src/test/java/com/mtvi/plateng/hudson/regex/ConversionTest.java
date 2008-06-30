package com.mtvi.plateng.hudson.regex;

import org.junit.Assert;
import org.junit.Test;

public class ConversionTest {

    @Test
    public void testSimple() {
        Configuration config = new Configuration();
        config.setUserNameExpression("(.*) (.*)");
        config.setEmailAddressPattern("%s.%s@test.com");
        Assert.assertEquals("justin.edelson@test.com", config.findMailAddressFor("justin edelson"));
    }
}
