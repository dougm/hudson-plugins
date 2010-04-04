package hudson.plugins.jsgames.game;

import junit.framework.TestCase;

public class MarioKartTest extends TestCase {

    private MarioKart game;

    public void setUp() {
        game = new MarioKart();
    }

    public void testGetIdShouldReturnExpectedValue() {
        assertEquals("mariokart", game.getId());
    }

    public void testGetTitleShouldReturnExpectedValue() {
        assertEquals("Mario Kart", game.getTitle());
    }
}
