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

package com.thalesgroup.hudson.library.tusarconversion;

import com.thalesgroup.hudson.library.tusarconversion.model.InputType;
import net.sf.saxon.s9api.*;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.OutputStream;


public class ConversionUtil {

    /**
     * Launches a XSLT conversion from an InputStream to an OutputStream.
     * This methods uses the net.sf.saxon packages.
     *
     * @param type   : the type of xml file to convert. It is one the 4 types shipped within the library (Unit Tests, Coverage, Violations, Measures)
     * @param source
     * @param output
     * @throws SaxonApiException
     */
    public static void convert(InputType type, Source source, OutputStream output) throws SaxonApiException {

        convert(type.getXsl(), source, output);
    }

    /**
     * Launches a XSLT conversion from an InputStream to an OutputStream.
     * This methods uses the net.sf.saxon packages.
     *
     * @param type   : the type of xml file to convert. It is one the 4 types shipped within the library (Unit Tests, Coverage, Violations, Measures)
     * @param input
     * @param output
     * @throws SaxonApiException
     */
    public static void convert(InputType type, InputStream input, OutputStream output) throws SaxonApiException {

        convert(type.getXsl(), new StreamSource(input), output);
    }

    /**
     * Launches a XSLT conversion from a source to an OutputStream.
     * This methods uses the net.sf.saxon packages.
     *
     * @param stylesheetName : the name of the stylesheet
     * @param input
     * @param output
     * @throws SaxonApiException
     */
    public static void convert(String stylesheetName, InputStream input, OutputStream output) throws SaxonApiException {

        convert(stylesheetName, new StreamSource(input), output);
    }

    /**
     * Launches a XSLT conversion from a source to an OutputStream.
     * This methods uses the net.sf.saxon packages.
     *
     * @param stylesheetName : the name of the stylesheet
     * @param source
     * @param output
     * @throws SaxonApiException
     */
    public static void convert(String stylesheetName, Source source, OutputStream output) throws SaxonApiException {

        // create the conversion processor with a Xslt compiler
        Processor processor = new Processor(false);
        XsltCompiler compiler = processor.newXsltCompiler();

        // compile and load the XSL file
        XsltExecutable xsltExecutable = compiler.compile(new StreamSource(ConversionUtil.class.getResourceAsStream(stylesheetName)));
        XsltTransformer xsltTransformer = xsltExecutable.load();

        // create the input
        XdmNode xdmNode = processor.newDocumentBuilder().build(source);

        // create the output with its options
        Serializer out = new Serializer();
        out.setOutputProperty(Serializer.Property.METHOD, "xml");
        out.setOutputProperty(Serializer.Property.INDENT, "yes");
        out.setOutputStream(output);

        // run the conversion
        xsltTransformer.setInitialContextNode(xdmNode);
        xsltTransformer.setDestination(out);
        xsltTransformer.transform();
    }

}
