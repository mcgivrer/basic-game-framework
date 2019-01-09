/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import fr.snapgames.bgf.core.Game;
import fr.snapgames.bgf.core.entity.BoundingBox.BoundingBoxType;

/**
 * <p>
 * The Camera object is the component that focus th viewport on some specific
 * target(Mainly another GameObject).
 * <p>
 * A sort of delay(the {@link Camera#tweenFactor}) is used in this specific
 * implementation to follow the {@link Camera#trackedObject}.
 * <p>
 * It also use the game viewport ({@link Camera#view}) as the view window for
 * the game.
 * 
 * @author Frederic Delorme<frederic.delorme@snapgames.fr>
 *
 */
public class Camera extends GameObject implements GameEntity {

	/**
	 * The tracked object by camera.
	 */
	private GameObject trackedObject;

	/**
	 * the tracked object following tween factor.
	 */
	private float tweenFactor = 1.0f;

	/**
	 * the reference View to compute camera view.
	 */
	private Rectangle view;

	/**
	 * Creata a new camera with a specific game object <code>name</code>.
	 * 
	 * @param name name of this new Camera object.
	 */
	public Camera(String name) {
		super(name);
		this.bBox = new BoundingBox(BoundingBoxType.RECTANGLE);
	}

	/**
	 * Create a brand new Camera object with a specific <code>name</code>, and
	 * position at(<code>x,y<c/ode>).
	 * 
	 * @param name
	 * @param x
	 * @param y
	 */
	public Camera(String name, int x, int y) {
		super(name, x, y, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameObject#render(java.awt.Graphics2D)
	 */
	@Override
	public void render(Graphics2D g) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.snapgames.bgf.core.entity.GameObject#renderDebugInfo(java.awt.Graphics2D)
	 */
	@Override
	public void renderDebugInfo(Graphics2D g) {
		super.renderDebugInfo(g);
	}

	/**
	 * Define the trackedObject for this camera.
	 * 
	 * @param trackedObject
	 */
	public Camera setTarget(GameObject target) {
		this.trackedObject = target;
		return this;
	}

	/**
	 * get the trackedObject for this camera.
	 * 
	 * @return
	 */
	public GameObject getTarget() {
		return trackedObject;
	}

	/**
	 * Define the tweenFactor factor for this camera, defining the "following speed"
	 * delay for the camera to track the trackedObject object.
	 * 
	 * @param tweenFactor
	 */
	public Camera setTween(float tween) {
		this.tweenFactor = tween;
		return this;
	}

	/**
	 * retrieve the tweenFactor factor for this camera, defining the "following
	 * speed" delay to track the trackedObject object.
	 * 
	 * @return
	 */
	public float getTween() {
		return tweenFactor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameObject#update(long)
	 */
	@Override
	public void update(long dt) {
		/*
		 * Vector2D view2D = new Vector2D("view",view.x,view.y); position =
		 * position.add(trackedObject.position.sub(view2D.multiply(0.5f))).multiply(
		 * tweenFactor*dt/1000.0f);
		 */

		position.x += (trackedObject.position.x - (view.width / 2) - this.position.x) * tweenFactor * dt / 1000;
		position.y += (trackedObject.position.y - (view.height / 2) - this.position.y) * tweenFactor * dt / 1000;

	}

	/**
	 * Operation executed before rendering of other objects
	 * 
	 * @param app
	 * @param g
	 */
	public void preRender(Game app, Graphics2D g) {
		g.translate(-position.x, -position.y);
	}

	/**
	 * 
	 * Operation executed after rendering of other objects
	 * 
	 * @param app
	 * @param g
	 */
	public void postRender(Game app, Graphics2D g) {
		g.translate(position.x, position.y);
	}

	/**
	 * Builder pattern for the GameObject.
	 * 
	 * @param name
	 * @return
	 */
	public static Camera builder(String name) {
		return new Camera(name);
	}

	/**
	 * define the Game view to compute camera position.
	 * 
	 * @param view
	 */
	public Camera setView(Rectangle view) {
		this.view = view;
		this.size.x = this.view.width - 20;
		this.size.y = this.view.height - 20;

		return this;
	}
}
