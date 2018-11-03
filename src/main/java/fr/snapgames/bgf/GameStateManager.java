/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf;

import java.awt.Graphics2D;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.snapgames.bgf.InputListener.KeyBinding;

/**
 * The Game state manager is the service to switch smouthlessly between 2
 * gameplays (GameState) for the parent App.
 * 
 * @since 2018
 * @author Frédéric Delorme
 * @see https://github.com/SnapGames/basic-game-framework/wiki/gsm
 */
public class GameStateManager {

	private static final Logger logger = LoggerFactory.getLogger(GameStateManager.class);
	/**
	 * internal State instance counter.
	 */
	private static long uid;

	/**
	 * the parent app.
	 */
	private App app;

	/**
	 * list of declared GameState for this manager
	 */
	private Map<String, GameState> states;

	/**
	 * the currently active GameState.
	 */
	private GameState currentState;

	public GameStateManager(App app) {
		this.app = app;
		states = new ConcurrentHashMap<>();
	}

	/**
	 * Initialize the Game State Manager for the parent app.
	 * 
	 * @param app the parent App instance.
	 */
	public void initialize(App app) {
		if (states.size() == 0) {
			logger.error("need to add State to the GameStateManager");
		}
	}

	/**
	 * Add a new state to the states list.
	 * 
	 * @param state        the instance of the state to be added to the manager
	 *                     state list.
	 * @param defaultState this flag notify the GSM this state is the default one.
	 */
	public void add(GameState state, boolean defaultState) {
		states.put(state.getName(), state);
		if (defaultState) {
			currentState = state;
			currentState.initialize(app);
			currentState.create(app, uid++);
			logger.debug("Set the state {} as a default one.", state.getName());
		}
	}

	/**
	 * Switch to the <code>nameState</code> GameState instance, if exists in the
	 * <code>states</code> map.
	 * 
	 * @param app       the parent App instance.
	 * @param nameState the new state to be activated.
	 */
	public void switchState(App app, String nameState) {
		if (states.containsKey(nameState)) {
			if (currentState != null) {
				currentState.dispose(app);
			}
			currentState = states.get(nameState);
			currentState.initialize(app);
			logger.debug("Initialize the currentState with {}", currentState.getName());
		} else {
			logger.error("Unable to activate {}, state does not exist in states list.", nameState);
		}
	}

	/**
	 * Load from teh states.xml file all the defined GameState for the game.
	 * <p>
	 * TODO implements the load(App) method.
	 * </p>
	 * 
	 * @param app the parent App instance.
	 */
	public void load(App app) {

	}

	/**
	 * Delegate the input management to the current active state.
	 * 
	 * @param app the parent App instance.
	 * @param il  the Inupot listeener instantiated for the parent App.
	 */
	public void input(App app, InputListener il) {
		currentState.input(app, il);
	}

	/**
	 * Delegate the Update action according to the dt elapsed time to the current
	 * active state.
	 * 
	 * @param app the parent App instance.
	 * @param dt  the elasped time since previous call.
	 */
	public void update(App app, long dt) {
		currentState.update(app, dt);
	}

	/**
	 * Delegate the rendering process to the current active state
	 * 
	 * @param app the parent App instance.
	 * @param g   the Graphics2D API to use for the rendering process.
	 */
	public void render(App app, Graphics2D g) {
		currentState.render(app, g);
	}

	/**
	 * Release all resources for the initialized states.
	 * 
	 * @param app the parent App instance.
	 */
	public void dispose(App app) {
		for (Entry<String, GameState> entry : states.entrySet()) {
			entry.getValue().dispose(app);
		}
		states.clear();
		states = null;
		currentState = null;
	}

	/**
	 * Manage action for the current State.
	 * 
	 * @param keyBinding
	 */
	public void action(KeyBinding keyBinding) {
		currentState.action(keyBinding);
	}

	/**
	 * Return the current active state.
	 * 
	 * @return the current GameState.
	 */
	public GameState getCurrentState() {
		// TODO Auto-generated method stub
		return currentState;
	}

}