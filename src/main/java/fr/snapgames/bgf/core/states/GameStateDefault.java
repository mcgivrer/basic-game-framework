/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */

package fr.snapgames.bgf.core.states;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.snapgames.bgf.core.Game;
import fr.snapgames.bgf.core.entity.Camera;
import fr.snapgames.bgf.core.entity.GameEntity;
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

	protected Camera activeCamera;

	protected Game app;

	protected Map<String, Camera> cameras = new ConcurrentHashMap<>();
	protected Map<String, GameEntity> objects = new ConcurrentHashMap<>();

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

	/**
	 * Return the active camera.
	 * 
	 * @return
	 */
	public Camera getActiveCamera() {
		return activeCamera;
	}

	/**
	 * define the active camera.
	 * 
	 * @param activeCamera
	 */
	public void setActiveCamera(Camera activeCamera) {
		this.activeCamera = activeCamera;
	}

	public void initialize(Game app) {
		this.app = app;
	}

	/**
	 * Add a GameObject to the game.
	 * 
	 * @param go
	 */
	public void add(GameObject go) {
		app.suspendRendering(true);
		objects.put(go.getName(), go);
		app.getRender().addObject(go);
		app.suspendRendering(false);
		logger.debug("Add object %s", go);
	}

	/**
	 * Add a caemra to the State. if first camera, becomes the default active one.
	 * 
	 * @param camera
	 */
	public void add(Camera camera) {
		if (!this.cameras.containsKey(camera.getName())) {
			this.cameras.put(camera.getName(), camera);

		} else {
			logger.error("A camera name {} already exists", camera.getName());
		}
		/**
		 * define this camera as default active one if first one.
		 */
		if (cameras.size() == 1) {
			activeCamera = camera;
		}
	}

	/**
	 * remove GameObject from management.
	 * 
	 * @param go
	 */
	public void remove(GameEntity go) {
		objects.remove(go.getName());
		app.getRender().removeObject(go);
		logger.debug("Object %s removed", go);
	}

	/**
	 * return all objects managed by this GameState.
	 * 
	 * @return a list of GameObject's.
	 */
	public Map<String, GameEntity> getObjects() {
		return objects;
	}

	/**
	 * The game state rendering phase.
	 * 
	 * @param app
	 * @param render
	 */
	public void render(Game app, Render render) {
		render.clearRenderBuffer();
		render.drawToRenderBuffer(app);
		render.drawRenderBufferToScreen();
	}

	/**
	 * Update all the GameObject and camera from the current active GameState.
	 * 
	 * @param app
	 * @param dt
	 */
	public void update(Game app, long dt) {
		for (Entry<String, GameEntity> entry : objects.entrySet()) {
			entry.getValue().update(dt);
		}
		for (Camera cam : cameras.values()) {
			cam.update(dt);
		}
	}

}