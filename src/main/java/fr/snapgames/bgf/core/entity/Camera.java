/**
 * 
 */
package fr.snapgames.bgf.core.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fr.snapgames.bgf.core.Game;
import fr.snapgames.bgf.core.entity.BoundingBox.BoundingBoxType;

/**
 * @author frede
 *
 */
public class Camera extends GameObject implements GameEntity {

	private GameObject trackedObject;

	private float tweenFactor = 1.0f;

	private Rectangle view;

	/**
	 * @param name
	 */
	public Camera(String name) {
		super(name);
		this.bBox = new BoundingBox(BoundingBoxType.RECTANGLE);
	}

	/**
	 * @param name
	 * @param x
	 * @param y
	 * @param image
	 */
	public Camera(String name, int x, int y, BufferedImage image) {
		super(name, x, y, image);
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
		x += (trackedObject.x - (view.width / 2) - this.x) * tweenFactor * dt/1000;
		y += (trackedObject.y - (view.height / 2) - this.y) * tweenFactor * dt/1000;
	}

	/**
	 * Operation executed before rendering of other objects
	 * 
	 * @param app
	 * @param g
	 */
	public void preRender(Game app, Graphics2D g) {
		g.translate(-x, -y);
	}

	/**
	 * 
	 * Operation executed after rendering of other objects
	 * 
	 * @param app
	 * @param g
	 */
	public void postRender(Game app, Graphics2D g) {
		g.translate(x, y);
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
		this.width = this.view.width - 20;
		this.height = this.view.height - 20;

		return this;
	}
}
