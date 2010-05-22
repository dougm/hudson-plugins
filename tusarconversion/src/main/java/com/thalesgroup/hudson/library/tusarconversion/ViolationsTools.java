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


public class ViolationsTools extends Tools {

    private static String VIOLATIONS_TYPE = "violations";

    public static InputType CPPCHECK = new InputType(VIOLATIONS_TYPE, "cppcheck", "inputtype.violations.cppcheck", "violations/cppcheck-1.40-to-checkstyle.xsl");
    public static InputType QA_C = new InputType(VIOLATIONS_TYPE, "qa_c", "inputtype.violations.qa_c", "violations/qa-c-to-checkstyle.xsl");
    public static InputType CPPTEST = new InputType(VIOLATIONS_TYPE, "cpptest", "inputtype.violations.cpptest", "violations/cpptest-7.3-to-checkstyle.xsl");
    public static InputType KLOCWORK = new InputType(VIOLATIONS_TYPE, "klockwork", "inputtype.violations.klocwork", "violations/klocwork-9.0-to-checkstyle.xsl");
    public static InputType COVERITY = new InputType(VIOLATIONS_TYPE, "coverity", "inputtype.violations.coverity", "violations/coverity-to-checkstyle.xsl");
    public static InputType GNATCHECK = new InputType(VIOLATIONS_TYPE, "gnatcheck", "inputtype.violations.gnatcheck", "violations/gnatcheck-to-checkstyle.xsl");

    @Override
    public List<InputType> getTools() {
        return Arrays.asList(new InputType[]{CPPCHECK, QA_C, CPPTEST, KLOCWORK, COVERITY, GNATCHECK});
    }
}
