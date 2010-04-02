package hudson.plugins.jsgames.game;

/**
 * This class provides details of a game.
 * @author cliffano
 */
public interface Game {

    /**
     * The game title, displayed on the game menu options below the game icon. 
     * @return the game title.
     */
    String getTitle();
    
    /**
     * The game ID, used in the URL path.
     * E.g. /plugin/jsgames/<game_id>/icon.png
     * @return the game ID
     */
    String getId();
}
