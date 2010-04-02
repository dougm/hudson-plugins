package hudson.plugins.jsgames.game;

/**
 * Tetris game details.
 * @author cliffano
 */
public class Tetris implements Game {

    public String getId() {
        return "tetris";
    }

    public String getTitle() {
        return "Tetris";
    }

}
