package hudson.plugins.coverage.model;

import junit.framework.TestCase;

/**
 * TODO javadoc.
 *
 * @author Stephen Connolly
 * @since 27-Jun-2008 08:27:25
 */
public class ElementTest extends TestCase {
    private static final String TEN = "unittests";
    private static final String TER = "/" + TEN;

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSmokes() throws Exception {
        Element r = Element.getRootElement();
        assertEquals("project", r.getName());
        assertNull(r.getParent());
        assertFalse(r.isFileLevel());
        assertFalse(r.isSubfileLevel());
        assertEquals(r, r);
    }

    public void testNew() throws Exception {
        Element r = Element.getRootElement();
        assertNull(Element.getElement(TER));
        Element c = r.newChild(TEN, false, StandardModel.getInstance());
        assertEquals(c, Element.getElement(TER));
    }

    protected void tearDown() throws Exception {
        final Element element = Element.getElement(TER);
        if (element != null) {
            element.destroy();
        }
        super.tearDown();
    }
}
