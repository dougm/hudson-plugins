package com.mtvi.plateng.hudson.regex;

import org.junit.Assert;
import org.junit.Test;

public class ConversionTest {

    @Test
    public void testSimple() {
        Configuration config = new Configuration();
        config.setUserNameExpression("(.*) (.*)");
        config.setEmailAddressPattern("%s.%s@test.com");
        RegexMailAddressResolver resolver = new RegexMailAddressResolver(config);
        Assert.assertEquals("justin.edelson@test.com", resolver
                .findMailAddressFor("justin edelson"));
    }
}
