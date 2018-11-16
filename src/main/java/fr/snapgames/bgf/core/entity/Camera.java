/**
 * 
 */
package fr.snapgames.bgf.core.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import fr.snapgames.bgf.core.App;

/**
 * @author frede
 *
 */
public class Camera extends GameObject implements GameEntity {

	private GameObject target;

	private float tween = 1.0f;

	/**
	 * @param name
	 */
	public Camera(String name) {
		super(name);
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
	 * Define the target for this camera.
	 * 
	 * @param target
	 */
	public Camera setTarget(GameObject target) {
		this.target = target;
		return this;
	}

	/**
	 * get the target for this camera.
	 * 
	 * @return
	 */
	public GameObject getTarget() {
		return target;
	}

	/**
	 * Define the tween factor for this camera, defining the "following speed" delay
	 * for the camera to track the target object.
	 * 
	 * @param tween
	 */
	public Camera setTween(float tween) {
		this.tween = tween;
		return this;
	}

	/**
	 * retrieve the tween factor for this camera, defining the "following speed"
	 * delay to track the target object.
	 * 
	 * @return
	 */
	public float getTween() {
		return tween;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameObject#update(long)
	 */
	@Override
	public void update(long dt) {

	}

	/**
	 * Operation executed before rendering of other objects
	 * 
	 * @param app
	 * @param g
	 */
	public void preRender(App app, Graphics2D g) {

	}

	/**
	 * 
	 * Operation executed after rendering of other objects
	 * 
	 * @param app
	 * @param g
	 */
	public void postRender(App app, Graphics2D g) {

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

}
