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

import com.thalesgroup.hudson.library.tusarconversion.exception.ConversionException;
import com.thalesgroup.hudson.library.tusarconversion.model.InputType;
import junit.framework.Assert;
import org.custommonkey.xmlunit.Diff;
import org.xml.sax.SAXException;

import java.io.*;

public class AbstractTest {

    protected void conversion(InputType type, String inputPath, String resultPath) throws ConversionException, IOException, SAXException {

        // define the streams (input/output)
        InputStream inputStream = this.getClass().getResourceAsStream(inputPath);

        File target = File.createTempFile("result", "xml");
        OutputStream outputStream = new FileOutputStream(target);

        // convert the input xml file
        ConversionUtil.convert(type, inputStream, outputStream);

        // compare with expected result
        InputStream expectedResult = this.getClass().getResourceAsStream(resultPath);
        InputStream fisTarget = new FileInputStream(target);

        Diff myDiff = new Diff(XSLUtil.readXmlAsString(expectedResult), XSLUtil.readXmlAsString(fisTarget));

        Assert.assertTrue("XSL transformation did not work" + myDiff, myDiff.similar());

        fisTarget.close();
    }

}