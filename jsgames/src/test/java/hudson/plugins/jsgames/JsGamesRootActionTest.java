package hudson.plugins.jsgames;

import junit.framework.TestCase;

public class JsGamesRootActionTest extends TestCase {

    private JsGamesRootAction action;

    public void setUp() {
        action = new JsGamesRootAction();
    }

    public void testGetDisplayNameShouldReturnExpectedValue() {
        assertEquals("JS Games", action.getDisplayName());
    }

    public void testGetIconFileNameShouldReturnExpectedValue() {
        assertEquals("/plugin/jsgames/icon.png", action.getIconFileName());
    }

    public void testGetUrlNameShouldReturnExpectedValue() {
        assertEquals("/jsgames", action.getUrlName());
    }

    public void testGetGamesShouldReturnNonEmptyList() {
        assertFalse(action.getGames().isEmpty());
    }
}
