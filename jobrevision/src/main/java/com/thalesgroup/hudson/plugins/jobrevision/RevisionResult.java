/*******************************************************************************
 * Copyright (c) 2010 Thales Corporate Services SAS                             *
 * Author : Gregory Boissinot                                                   *
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

package com.thalesgroup.hudson.plugins.jobrevision;

import hudson.model.Api;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.IOException;
import java.io.Serializable;

@ExportedBean
public class RevisionResult implements Serializable {

    private static final String REVISION_DESC = "The job revision";


    private String revision;

    private String description;

    public RevisionResult(String revision) {
        this.revision = revision;
        this.description = REVISION_DESC;
    }

    @SuppressWarnings("unused")
    public Object getDynamic(final String link, final StaplerRequest request, final StaplerResponse response) throws IOException {
        response.sendRedirect2("index");
        return null;
    }

    @SuppressWarnings("unused")
    @Exported
    public String getRevision() {
        return revision;
    }

    @SuppressWarnings("unused")
    @Exported
    public String getDescription() {
        return description;
    }

    /**
     * Gets the remote API for the build result.
     *
     * @return the remote API
     */
     @SuppressWarnings("unused")
    public Api getApi() {
        return new Api(this);
    }

}
