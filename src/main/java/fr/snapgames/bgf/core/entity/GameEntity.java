package fr.snapgames.bgf.core.entity;

import java.awt.Graphics2D;

public interface GameEntity {

	/**
	 * compute object mechanism.
	 * 
	 * @param dt
	 */
	void update(long dt);

	/**
	 * Draw object to buffer.
	 * 
	 * @param g
	 */
	void render(Graphics2D g);

	/**
	 * Rendering some debug information.
	 * 
	 * @param g
	 */
	void renderDebugInfo(Graphics2D g);

	/**
	 * @return the id
	 */
	int getId();

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @return the boundingBox
	 */
	BoundingBox getBoundingBox();

	/**
	 * @return the layer
	 */
	int getLayer();

	/**
	 * @return the priority
	 */
	int getPriority();

	/**
	 * return the Layer Fixed attribute.
	 * @return
	 */
	boolean getFixed();
}