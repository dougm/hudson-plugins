/*******************************************************************************
 * Copyright (c) 2010 Thales Corporate Services SAS                             *
 * Author : Grégory Boissinot, Guillaume Tanier                                 *
 *                                                                              *
 * Permission is hereby granted, free of charge, to any person obtaining a copy *
 * of this software and associated documentation files (the "Software"), to deal*
 * in the Software without restriction, including without limitation the rights *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell    *
 * copies of the Software, and to permit persons to whom the Software is        *
 * furnished to do so, subject to the following conditions:                     *
 *                                                                              *
 * The above copyright notice and this permission notice shall be included in   *
 * all copies or substantial portions of the Software.                          *
 *                                                                              *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR   *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,     *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER       *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,*
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN    *
 * THE SOFTWARE.                                                                *
 *******************************************************************************/

package com.thalesgroup.hudson.library.tusarconversion.tests;

import com.thalesgroup.hudson.library.tusarconversion.TestsTools;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;

public class BoostTestXSLTest extends AbstractTest {

    @Before
    public void setUp() {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreComments(true);
    }

    @Test
    public void testTransformationAutoTest() throws Exception {
        conversion(TestsTools.BOOSTTEST, "boosttest/autotest/testlog.xml", "boosttest/autotest/junit-result.xml");
    }

    @Test
    public void testTransformationAutoTestMultiple() throws Exception {
        conversion(TestsTools.BOOSTTEST, "boosttest/autotest-multiple/testlog.xml", "boosttest/autotest-multiple/junit-result.xml");
    }

    @Test
    public void testTransformationAutoTestCase1() throws Exception {
        conversion(TestsTools.BOOSTTEST, "boosttest/testcase1/testlog.xml", "boosttest/testcase1/junit-result.xml");
    }

    @Test
    public void testTransformationAutoTestCase2() throws Exception {
        conversion(TestsTools.BOOSTTEST, "boosttest/testcase2/testlog.xml", "boosttest/testcase2/junit-result.xml");
    }

    @Test
    public void testTransformationAutoTestCase3() throws Exception {
        conversion(TestsTools.BOOSTTEST, "boosttest/testcase3/testlog.xml", "boosttest/testcase3/junit-result.xml");
    }
}
