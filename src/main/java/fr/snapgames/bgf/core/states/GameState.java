/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.states;

import java.util.Map;

import fr.snapgames.bgf.core.App;
import fr.snapgames.bgf.core.entity.GameObject;
import fr.snapgames.bgf.core.gfx.Render;
import fr.snapgames.bgf.core.io.InputListener;
import fr.snapgames.bgf.core.io.InputListener.KeyBinding;

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
     * @param g   the Render to be used to render this state.
     */
    void render(App app, Render r);

    /**
     * return the list of GameObject for this state.
     * 
     * @return
     */
    Map<String, GameObject> getObjects();
}