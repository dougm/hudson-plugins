/*******************************************************************************
 * Copyright (c) 2010 Thales Corporate Services SAS                             *
 * Author : Gregory Boissinot, Guillaume Tanier                                 *
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

package com.thalesgroup.hudson.library.tusarconversion;

import com.thalesgroup.hudson.library.tusarconversion.model.InputType;

import java.util.Arrays;
import java.util.List;


public class TestsTools extends Tools {

    private static String TESTS_TYPE = "tests";

    public static InputType CPPUNIT = new InputType(TESTS_TYPE, "cppunit", "inputtype.test.cppunit", "tests/cppunit-to-junit.xsl");
    public static InputType CPPTEST = new InputType(TESTS_TYPE, "cpptest", "inputtype.test.cpptest", "tests/cpptest-to-junit.xsl");
    public static InputType FPCUNIT = new InputType(TESTS_TYPE, "fpcunit", "inputtype.test.fpcunit", "tests/fpcunit-to-junit.xsl");
    public static InputType BOOSTTEST = new InputType(TESTS_TYPE, "boosttest", "inputtype.test.boostest", "tests/boosttest-to-junit.xsl");
    public static InputType MSTEST = new InputType(TESTS_TYPE, "msttest", "inputtype.test.mstest", "tests/mstest-to-junit.xsl");
    public static InputType NUNIT = new InputType(TESTS_TYPE, "nunit", "inputtype.test.nunit", "tests/nunit-to-junit.xsl");
    public static InputType PHPUNIT = new InputType(TESTS_TYPE, "phpunit", "inputtype.test.phpunit", "tests/phpunit-to-junit.xsl");
    public static InputType UNITTEST = new InputType(TESTS_TYPE, "unittest", "inputtype.test.unittest", "tests/unittest-to-junit.xsl");

    @Override
    public List<InputType> getTools() {
        return Arrays.asList(new InputType[]{CPPUNIT, CPPTEST, FPCUNIT, BOOSTTEST, MSTEST, NUNIT, PHPUNIT, UNITTEST});
    }
}
