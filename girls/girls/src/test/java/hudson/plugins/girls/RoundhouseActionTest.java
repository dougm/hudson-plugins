package hudson.plugins.girls;

import junit.framework.TestCase;

public class RoundhouseActionTest extends TestCase {

	private RoundhouseAction action;

	public void setUp() {
		action = new RoundhouseAction(Style.BAD_ASS,
				"Girls they want, wanna have fun.");
	}

	public void testAccessors() {
		assertEquals(Style.BAD_ASS, action.getStyle());
		assertEquals("Girls they want, wanna have fun.", action
				.getFact());
		assertEquals("Girls", action.getDisplayName());
		assertNull(action.getIconFileName());
		assertEquals("girls", action.getUrlName());
	}
}
