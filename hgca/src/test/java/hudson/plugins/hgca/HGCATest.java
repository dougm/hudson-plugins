/*
 * The MIT License
 *
 * Copyright (c) 2004-2010, Sun Microsystems, Inc., Alan Harder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.hgca;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import hudson.model.Hudson;
import hudson.model.Job;
import java.util.Collections;
import org.jvnet.hudson.test.Bug;
import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.recipes.LocalData;

/**
 * Test interaction of hgca plugin with Hudson core.
 * @author Alan.Harder@sun.com
 */
public class HGCATest extends HudsonTestCase {

    @LocalData
    public void testPlugin() throws Exception {
        // ensure no NPE before plugin is configured:
        HGCAProjectProperty.DescriptorImpl DESCRIPTOR =
                hudson.getDescriptorByType(HGCAProjectProperty.DescriptorImpl.class);
        assertTrue(DESCRIPTOR.getGlobalAnnotations().isEmpty());

        Job job = (Job)Hudson.getInstance().getItem("test-job");
        assertNotNull("job missing.. @LocalData problem?", job);

        WebClient wc = new WebClient();
        HtmlForm form = wc.goTo("configure").getFormByName("config");
        HtmlElement row = find(form, "//tr[td='List of pattern/URL pairs']");
        ((HtmlButton)find(row, ".//button")).click();                      // "Add" button
        findInput(row, "key").setValueAttribute("TEST-(\\d+)");                 // Pattern
        findInput(row, "value").setValueAttribute("http://jira.foo.com/browse/$1"); // URL
        // Don't automatically apply global patterns to all projects:
        ((HtmlInput)row.getByXPath("..//input[@name='alwaysApply']").get(0)).setChecked(false);
        submit(form);
        assertEquals(1, DESCRIPTOR.getGlobalAnnotations().size());
        assertFalse(DESCRIPTOR.getAlwaysApply());

        // With no HGCAProjectProperty and global "alwaysApply" == false, no annotation
        String xml = getChanges(wc, job);
        assertTrue(xml, xml.contains("fix TEST-123")); // No annotation added

        // Add property just to specify "applyGlobal" (no project-specific annoatations)
        job.addProperty(new HGCAProjectProperty(true));
        xml = getChanges(wc, job);
        assertTrue(xml, xml.contains(
                "fix <a href=\"http://jira.foo.com/browse/123\"> TEST-123 </a>"));

        form = wc.goTo("configure").getFormByName("config");
        row = find(form, "//tr[td='List of pattern/URL pairs']");
        // DO automatically apply global patterns to all projects:
        ((HtmlInput)row.getByXPath("..//input[@name='alwaysApply']").get(0)).setChecked(true);
        submit(form);
        assertEquals(1, DESCRIPTOR.getGlobalAnnotations().size());
        assertTrue(DESCRIPTOR.getAlwaysApply());

        // Now with no HGCAProjectProperty it will apply annotation
        job.removeProperty(HGCAProjectProperty.class);
        xml = getChanges(wc, job);
        assertTrue(xml, xml.contains(
                "fix <a href=\"http://jira.foo.com/browse/123\"> TEST-123 </a>"));

        // Add property to override global setting and omit global patterns:
        job.addProperty(new HGCAProjectProperty(false));
        xml = getChanges(wc, job);
        assertTrue(xml, xml.contains("fix TEST-123")); // No annotation added

        // Add property with project-specific pattern, but omit global:
        job.removeProperty(HGCAProjectProperty.class);
        job.addProperty(new HGCAProjectProperty(false,
                new HGCAProjectProperty.Entry("CR(\\d{5})", "bugdb/$1/view")));
        xml = getChanges(wc, job);
        assertTrue(xml, xml.contains("fix TEST-123")); // No annotation added
        assertTrue(xml, xml.contains("<a href=\"bugdb/97531/view\"> CR97531 </a>"));

        // Add property with project-specific pattern, include global:
        job.removeProperty(HGCAProjectProperty.class);
        job.addProperty(new HGCAProjectProperty(true,
                new HGCAProjectProperty.Entry("CR(\\d{5})", "bugdb/$1/view")));
        xml = getChanges(wc, job);
        assertTrue(xml, xml.contains(
                "fix <a href=\"http://jira.foo.com/browse/123\"> TEST-123 </a>"));
        assertTrue(xml, xml.contains("<a href=\"bugdb/97531/view\"> CR97531 </a>"));

        // Test NUM and ANYWORD tokens, and that project can override global pattern
        job.removeProperty(HGCAProjectProperty.class);
        job.addProperty(new HGCAProjectProperty(true,
                new HGCAProjectProperty.Entry("\\bCRNUM(:)?", "buggy$28080/$1"),
                new HGCAProjectProperty.Entry("\\[ANYWORD\\]", "mywiki/$1"),
                new HGCAProjectProperty.Entry("TEST-(\\d+)", "http://myproject.com/$1")));
        xml = getChanges(wc, job);
        assertTrue(xml, xml.contains("<a href=\"buggy:8080/97531\"> CR97531: </a>"));
        assertTrue(xml, xml.contains("<a href=\"mywiki/My-wiki_page.doc\"> "
                                     + "[My-wiki_page.doc] </a> for details [or not]"));
        assertTrue(xml, xml.contains(
                "fix <a href=\"http://myproject.com/123\"> TEST-123 </a>"));
    }

    private static HtmlElement find(HtmlElement e, String xpath) {
        return (HtmlElement)e.getByXPath(xpath).get(0);
    }

    private static HtmlInput findInput(HtmlElement e, String name) {
        return (HtmlInput)find(e, ".//input[@name='" + name + "']");
    }

    private static String getChanges(WebClient wc, Job job) throws Exception {
        return wc.getPage(job, "changes").getElementById("main-panel")
                 .asXml().replaceAll("\\s+", " ");
    }

    @Bug(6367)
    public void testUpgrade() {
        // applyGlobal can be null in upgrade from previous HGCA
        HGCAProjectProperty pp = new HGCAProjectProperty(
                Collections.<HGCAProjectProperty.Entry>emptyList(), null);
        // getAnnotations got NPE in HGCA 1.2
        assertEquals(0, pp.getAnnotations().size());
    }
}
