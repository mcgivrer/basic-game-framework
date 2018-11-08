/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */

package fr.snapgames.bgf.core.states;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.snapgames.bgf.core.App;
import fr.snapgames.bgf.core.entity.GameObject;
import fr.snapgames.bgf.core.gfx.Render;

/**
 * A Default implementation for the GameState interface providing basic
 * services.
 * 
 * @author Frédéric Delorme
 * @since 2018
 * 
 */
public class GameStateDefault {

	private static final Logger logger = LoggerFactory.getLogger(GameStateDefault.class);

	protected long uid = 0;
	protected String stateName = "NoName";

	protected App app;

	protected Map<String, GameObject> objects = new ConcurrentHashMap<>();

	/**
	 * retrieve the name for this state.
	 * 
	 * @return the name of the GameState implementation.
	 */
	public String getName() {
		return stateName;
	}

	/**
	 * return the UID for the instance of this implementation.
	 * 
	 * @return a long value identifying this instance.
	 */
	public long getUID() {
		return uid;
	}

	public void initialize(App app) {
		this.app = app;
	}

	/**
	 * Add a GameObject to the game.
	 * 
	 * @param go
	 */
	public void add(GameObject go) {
		app.suspendRendering(true);
		objects.put(go.name, go);
		app.getRender().addObject(go);
		app.suspendRendering(false);
		logger.debug("Add object %s", go);
	}

	/**
	 * remove GameObject from management.
	 * 
	 * @param go
	 */
	public void remove(GameObject go) {
		objects.remove(go.name);
		app.getRender().removeObject(go);
		logger.debug("Object %s removed", go);
	}

	/**
	 * return all objects managed by this GameState.
	 * 
	 * @return a list of GameObject's.
	 */
	public Map<String, GameObject> getObjects() {
		return objects;
	}


	/**
	 * The game state rendering phase.
	 * @param app
	 * @param render
	 */
	public void render(App app, Render render){
		render.clearRenderBuffer();
		render.drawToRenderBuffer();
		render.drawRenderBufferToScreen();
	}
}