/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf;

import java.awt.Graphics2D;
import java.util.Map;

import fr.snapgames.bgf.InputListener.KeyBinding;

/**
 * The interface for any Game State managed by the GameStateManager.
 * 
 * @since 2018
 * @author Frédéric Delorme
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
public interface GameState {
    /**
     * retrieve the name for this state.
     * 
     * @return the name of the GameState implementation.
     */
    String getName();

    /**
     * return the UID for the instance of this implementation.
     * 
     * @return a long value identifying this instance.
     */
    long getUID();

    /**
     * Initialize the State
     * 
     * @param app the main application managing this state.
     */
    void initialize(App app);

    /**
     * Create all necessaru dependencies for this state.
     * 
     * @param app the main application managing this state.
     */
    void create(App app, long uid);

    /**
     * release all the dependencies for this state.
     * 
     * @param app the main application managing this state.
     */
    void dispose(App app);

    /**
     * manage input for this state.
     * 
     * @param app the main application managing this state.
     * @param il  the input Listener managing all the input.
     */
    void input(App app, InputListener il);

    /**
     * Process KeyBinding actions.
     * 
     * @param kb the keyBinding action requested.
     */
    void action(KeyBinding kb);

    /**
     * Update this state according the elapsed time (dt).
     * 
     * @param app the main application managing this state.
     * @param dt  the elapsed time since previous call.
     */
    void update(App app, long dt);

    /**
     * Render this state with the Graphics2D api g.
     * 
     * @param app the main application managing this state.
     * @param g   the Graphics2D API available to render this state.
     */
    void render(App app, Graphics2D g);

    /**
     * return the list of GameObject for this state.
     * 
     * @return
     */
    Map<String, GameObject> getObjects();
}