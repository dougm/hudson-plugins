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

import java.util.Arrays;
import java.util.List;


public class MeasuresTools extends Tools {

    private static String MEASURES_TYPE = "measures";

    public static InputType SLCNT = new InputType(MEASURES_TYPE, "slcnt", "inputtype.measures.slcnt_anaqua_to_measures", "slcnt-anaqua-to-measures.xsl");
    public static InputType LOGISCOPE = new InputType(MEASURES_TYPE, "logiscope", "inputtype.measures.logiscope_anaqua", "logiscope-anaqua-to-measures.xsl");
    public static InputType GNATMETRIC = new InputType(MEASURES_TYPE, "gnatmetric", "inputtype.measures.gnatmetric", "gnatmetric-to-measures.xsl");

    @Override
    public List<InputType> getTools() {
        return Arrays.asList(new InputType[]{SLCNT, LOGISCOPE, GNATMETRIC});
    }
}
