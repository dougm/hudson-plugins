package hudson.plugins.jsgames.game;

import junit.framework.TestCase;

public class TetrisTest extends TestCase {

    private Tetris game;

    public void setUp() {
        game = new Tetris();
    }

    public void testGetIdShouldReturnExpectedValue() {
        assertEquals("tetris", game.getId());
    }

    public void testGetTitleShouldReturnExpectedValue() {
        assertEquals("Tetris", game.getTitle());
    }
}
