/**
 * Copyright (c) 2010 Cliffano Subagio
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

    /**
     * The list of games to be displayed on JSGames menu.
     */
    private static List<Game> games;
    static {
        games = new ArrayList<Game>();
        games.add(new MarioKart());
        games.add(new Tetris());
    }

    /**
     * @return the display name
     */
    public final String getDisplayName() {
        return "JS Games";
    }

    /**
     * @return the icon file name
     */
    public final String getIconFileName() {
        return "/plugin/jsgames/icon.png";
    }

    /**
     * @return the URL name
     */
    public final String getUrlName() {
        return "/jsgames";
    }

    /**
     * @return the list of games
     */
    public final List<Game> getGames() {
        return games;
    }
}
