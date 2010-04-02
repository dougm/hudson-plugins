package hudson.plugins.jsgames;

import hudson.Extension;
import hudson.model.RootAction;
import hudson.plugins.jsgames.game.Game;
import hudson.plugins.jsgames.game.MarioKart;
import hudson.plugins.jsgames.game.Tetris;

import java.util.ArrayList;
import java.util.List;

/**
 * This class allows JS Games option to appear on Hudson dashboard menu.
 * @author cliffano
 */
@Extension
public class JsGamesRootAction implements RootAction {

    private static List<Game> games;
    static {
        games = new ArrayList<Game>();
        games.add(new MarioKart());
        games.add(new Tetris());
    }

    /**
     * Gets the action display name.
     * @return the display name
     */
    public String getDisplayName() {
        return "JS Games";
    }

    /**
     * Gets the icon file name.
     * @return the icon file name
     */
    public String getIconFileName() {
        return "/plugin/jsgames/icon.png";
    }

    /**
     * Gets the URL name.
     * @return the URL name
     */
    public String getUrlName() {
        return "/jsgames";
    }

    /**
     * Gets the list of games.
     * @return the list of games
     */
    public List<Game> getGames() {
        return games;
    }
}
