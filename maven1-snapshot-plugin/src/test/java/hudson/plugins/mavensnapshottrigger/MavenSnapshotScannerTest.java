/********************************************************************************
 * CruiseControl, a Continuous Integration Toolkit
 * Copyright (c) 2003, ThoughtWorks, Inc.
 * 200 E. Randolph, 25th Floor
 * Chicago, IL 60601 USA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     + Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     + Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     + Neither the name of ThoughtWorks, Inc., CruiseControl, nor the
 *       names of its contributors may be used to endorse or promote
 *       products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ********************************************************************************/
package hudson.plugins.mavensnapshottrigger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import junit.framework.TestCase;

public class MavenSnapshotScannerTest extends TestCase {

    private static final String PACKAGE_PATH = "hudson/plugins/mavensnapshottrigger";

    private static final String PROJECT_XML_RELATIVE_PATH =
        PACKAGE_PATH + "/maven-project.xml";

    private static final String TEST_PROJECT_XML;
    private static final String TEST_REPOSITORY;

    static {
        
        URL projectUrl = MavenSnapshotScannerTest.class.getClassLoader().getResource(PROJECT_XML_RELATIVE_PATH);
        try {
            TEST_PROJECT_XML = URLDecoder.decode(projectUrl.getPath(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        // Use the parent folder of the project xml as repository folder
        TEST_REPOSITORY = new File(TEST_PROJECT_XML).getParentFile().getAbsolutePath();
    }
    
    public MavenSnapshotScannerTest(String name) {
        super(name);
    }

    public void testGetProjectXml() throws Exception {
        MavenSnapshotScanner dep = new MavenSnapshotScanner();

        dep.setProjectFile(TEST_PROJECT_XML);
        dep.setLocalRepository(TEST_REPOSITORY);
        File testProjectFile = new File(TEST_PROJECT_XML);
        List filenames = new ArrayList();
        dep.getSnapshotFilenames(filenames, testProjectFile, new ArrayList());
        assertEquals("Filename list is not the correct size", 2, filenames.size());
        String filename = (String) filenames.get(0);
        String expectedFilename = TEST_REPOSITORY + "/maven/jars/cc-maven-test-1.0-SNAPSHOT.jar";
        File expectedFile = new File(expectedFilename);
        assertEquals("Unexpected filename", expectedFile.getAbsolutePath(), filename);
        filename = (String) filenames.get(1);
        expectedFilename = TEST_REPOSITORY + "/maven/jars/maven-1.0-SNAPSHOT.jar";
        expectedFile = new File(expectedFilename);
        assertEquals("Unexpected filename", expectedFile.getAbsolutePath(), filename);
    }

    public void testGettingModifications() throws Exception {
        MavenSnapshotScanner dep = new MavenSnapshotScanner();

        dep.setProjectFile(TEST_PROJECT_XML);
        dep.setLocalRepository(TEST_REPOSITORY);
        Date epoch = new Date(0);
        List modifications = dep.getModifications(epoch);
        assertEquals("Modification list is not the correct size", 2, modifications.size());
    }

    public void testGettingModificationsFromPOMWithoutNamespace() throws Exception {
        MavenSnapshotScanner dep = new MavenSnapshotScanner();

        dep.setProjectFile(getPOMPath("without-namespace-project.xml"));
        dep.setLocalRepository(TEST_REPOSITORY);
        Date epoch = new Date(0);
        List modifications = dep.getModifications(epoch);
        assertEquals("Modification list is not the correct size", 3, modifications.size());
    }

    public void testGettingModificationsFromNamespacedPOM() throws Exception {
        MavenSnapshotScanner dep = new MavenSnapshotScanner();

        dep.setProjectFile(getPOMPath("with-namespace-project.xml"));
        dep.setLocalRepository(TEST_REPOSITORY);
        Date epoch = new Date(0);
        List modifications = dep.getModifications(epoch);
        assertEquals("Modification list is not the correct size", 3, modifications.size());
    }

    public void testGettingModificationsFromSpecialPOM() throws Exception {
        MavenSnapshotScanner dep = new MavenSnapshotScanner();

        dep.setProjectFile(getPOMPath("special-project.xml"));
        dep.setLocalRepository(TEST_REPOSITORY);
        Date epoch = new Date(0);
        List modifications = dep.getModifications(epoch);
        assertEquals("Modification list is not the correct size", 3, modifications.size());
    }

    public void testGettingModificationsFromExtendedPOM() throws Exception {
        MavenSnapshotScanner dep = new MavenSnapshotScanner();

        dep.setProjectFile(getPOMPath("extended-project.xml"));
        dep.setLocalRepository(TEST_REPOSITORY);
        Date epoch = new Date(0);
        List modifications = dep.getModifications(epoch);
        assertEquals("Modification list is not the correct size", 4, modifications.size());
    }

    public void testGettingModificationsFromChildPOM() throws Exception {
        MavenSnapshotScanner dep = new MavenSnapshotScanner();

        dep.setProjectFile(getPOMPath("maven/child-project.xml"));
        dep.setLocalRepository(TEST_REPOSITORY);
        Date epoch = new Date(0);
        List modifications = dep.getModifications(epoch);
        assertEquals("Modification list is not the correct size", 5, modifications.size());
    }
    
    private String getPOMPath(String file) throws UnsupportedEncodingException {
        String resource = PACKAGE_PATH + "/" + file;
        URL url = MavenSnapshotScannerTest.class.getClassLoader().getResource(resource);
        return URLDecoder.decode(url.getPath(), "UTF-8");
    }

    public void testReplaceVariables() throws Exception {
        MavenSnapshotScanner dep = new MavenSnapshotScanner();

        Properties p = new Properties();
        p.put("env", "dev");
        p.put("a", "alphabetagamma");
        p.put("b", "b");
        p.put("c", "cecilia");
        p.put("foobarsnafu", "x");
        p.put("cvs.user", "donmike");
        p.put("cvsroot", ":ext:${cvs.user}@host:/cvs");
        p.put("foo", "${bar}");
        p.put("bar", "${foo}");
        assertEquals("01. Variable replacement failed",
               "foo-dev-1.0.jar", dep.replaceVariables(p, "foo-${env}-1.0.jar"));
        assertEquals("02. Variable replacement failed for variable-only-string",
               "dev", dep.replaceVariables(p, "${env}"));
        assertEquals("03. Variable replacement failed for an empty string",
               "", dep.replaceVariables(p, ""));
        assertEquals("04. Variable replacement failed for partial variable",
               "${ffffffff", dep.replaceVariables(p, "${ffffffff"));
        assertEquals("05. Multivariable replacement failed",
               "alphabetagammabxcecilia", dep.replaceVariables(p, "${a}${b}${foobarsnafu}${c}"));
        assertEquals("06. Multireplacement failed",
               "CVSROOT=:ext:donmike@host:/cvs!alphabetagamma", dep.replaceVariables(p, "CVSROOT=${cvsroot}!${a}"));
        assertEquals("07. Infinite loop",
               "${bar}${foo}", dep.replaceVariables(p, "${foo}${bar}"));
        assertTrue("08. System environment variable replacement failed",
               dep.replaceVariables(p, "${user.home}")
               .equals(System.getProperty("user.home")));
        assertEquals("09. Null replacement failed",
               null, dep.replaceVariables(p, null));
    }

}
