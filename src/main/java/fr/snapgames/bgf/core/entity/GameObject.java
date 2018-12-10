/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */

package fr.snapgames.bgf.core.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import fr.snapgames.bgf.core.Game;
import fr.snapgames.bgf.core.entity.BoundingBox.BoundingBoxType;

/**
 * Internal object managed by the {@link Game}.
 * 
 * This <code>GameObject</code> is a graphical entity to be moved and animated
 * by the parent Game.
 * 
 * The <code>GameObject</code> is referenced into 2 {@link Game} attributes :
 * <ul>
 * <li>into the {@link Game#objects} map for an object's management purpose,</li>
 * <li>into the {@link Game#renderingList} as a sort rendering list of graphical
 * object.</li>
 * </ul>
 * 
 * <p>
 * This object basically contains attributes for
 * <ul>
 * <li>position: <code>x,y</code></li>
 * <li>size:. <code>width,height</code></li>
 * <li>speed: <code>dx,dy</code></li>
 * <li>some Physic computation parameters (WoAwW :)
 * <ul>
 * <li><code>friction</code> a factor to attenuate speed on contact</li>
 * <li><code>elasticity</code> a factor to compute resulting speed after
 * collision.</li>
 * </ul>
 * </li>
 * <li>graphical rendering:
 * <ul>
 * <li><code>layer</code> to organize object per layer (for future needs),</li>
 * <li><code>priority</code> to sort object in the rendering pipeline and having
 * a front/back filtering.</li>
 * <li><code>color</code> the rendering color for a simple rectangle object
 * (default).</li>
 * <li><code>image</code> a BufferedImage to be rendered.</li>
 * </ul>
 * </li>
 * </ul>
 * </p>
 * <p>
 * And 2 majors methods:
 * </p>
 * <ul>
 * <li>{@link GameObject#update(long)}</li> to manage and compute object
 * position/speed and mainly, behavior.
 * <li>{@link GameObject#render(Graphics2D)}</li> to render this graphical
 * object to the rendering pipeline.
 * </ul>
 * <p>
 * And some useful {@link GameObject#builder(String)} and accessors to create
 * easily new <code>GameObject</code>, like {@link GameObject#moveTo(int, int)}
 * or {@link GameObject#setVelocity(float, float)}.
 * 
 * </p>
 * 
 * @author Frédéric Delorme
 * @since 2018
 * 
 */
public class GameObject implements GameEntity {

	public int id;
	public String name;

	public int layer = 0;
	public int priority = 0;

	public float x = 0.0f;
	public float y = 0.0f;
	public float width = 16.0f;
	public float height = 16.0f;
	public float scale = 1.0f;
	public float dx = 0.0f;
	public float dy = 0.0f;
	public float friction = 0.13f;
	public float elasticity = 0.98f;

	public BufferedImage image;
	public Color color;

	public BoundingBox bBox = new BoundingBox(BoundingBoxType.RECTANGLE);

	/**
	 * Create a new GameObject.
	 * 
	 * @param name
	 */
	public GameObject(String name) {
		this.name = name;
	}

	/**
	 * Create a new GObject with parameters !
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @param image
	 */
	public GameObject(String name, int x, int y, BufferedImage image) {
		this(name);
		moveTo(x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameEntity#update(long)
	 */
	@Override
	public void update(long dt) {
		moveTo((int) (x + (dx * dt)), (int) (y + (dy * dt)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameEntity#render(java.awt.Graphics2D)
	 */
	@Override
	public void render(Graphics2D g) {
		if (image != null) {
			g.drawImage(image, (int) x, (int) y, (int) (width * scale), (int) (height * scale), null);
		} else {
			g.setColor(color);
			g.fillRect((int) x, (int) y, (int) width, (int) height);
			g.setColor(Color.BLACK);
			g.drawRect((int) x, (int) y, (int) width, (int) height);
		}
	}

	private void computeBoundingBox(GameObject o) {
		bBox.setBox((int) o.x, (int) o.y, (int) o.width, (int) o.height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.snapgames.bgf.core.entity.GameEntity#renderDebugInfo(java.awt.Graphics2D)
	 */
	@Override
	public void renderDebugInfo(Graphics2D g) {
		// maybe and child entity inheriting from Gameobject will override this method
		// to add some useful debug information.
	}

	/**
	 * Set Position.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public GameObject moveTo(float x, float y) {
		this.x = x;
		this.y = y;
		computeBoundingBox(this);
		return this;
	}

	/**
	 * Set the velocity for this `GameObject`.
	 * 
	 * @param dx
	 * @param dy
	 * @return
	 */
	public GameObject setVelocity(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
		return this;
	}

	/**
	 * Set size.
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public GameObject setSize(int width, int height) {
		this.width = width;
		this.height = height;
		computeBoundingBox(this);
		return this;
	}

	/**
	 * Set image
	 * 
	 * @param image
	 * @return
	 */
	public GameObject setImage(BufferedImage image) {
		this.image = image;
		return this;
	}

	/**
	 * Set the drawing color for this `GameObject`.
	 * 
	 * @param color
	 * @return
	 */
	public GameObject setColor(Color color) {
		this.color = color;
		return this;
	}

	/**
	 * Set the rendering layer for this `GameObject`.
	 * 
	 * @param layer
	 * @return
	 */
	public GameObject setLayer(int layer) {
		this.layer = layer;
		return this;
	}

	/**
	 * Set the rendering priority in the pipe for this `GameObject`.
	 * 
	 * @param priority
	 * @return
	 */
	public GameObject setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	/**
	 * Set the physic elasticity parameter for this `GameObject`.
	 * 
	 * @param elasticity
	 * @return
	 */
	public GameObject setElasticity(float elasticity) {
		this.elasticity = elasticity;
		return this;
	}

	/**
	 * Set the physic friction parameter for this `GameObject`.
	 * 
	 * @param friction
	 * @return
	 */
	public GameObject setFriction(float friction) {
		this.friction = friction;
		return this;
	}

	/**
	 * Define the type of Bounding Box for this GameObject.
	 * 
	 * @param type the bounding box type to be set for this GameObject.
	 */
	public GameObject setBoundingType(BoundingBoxType type) {
		this.bBox.setType(type);
		return this;
	}

	/**
	 * Builder pattern for the GameObject.
	 * 
	 * @param name
	 * @return
	 */
	public static GameObject builder(String name) {
		return new GameObject(name);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder().append("GameObject{").append("name='").append(name).append("'")
				.append(",layer=").append(layer).append(",priority=").append(priority).append("}");
		return builder.toString();
	}

	public GameObject setScale(float f) {
		this.scale = f;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameEntity#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameEntity#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameEntity#getBoundingBox()
	 */
	@Override
	public BoundingBox getBoundingBox() {
		return bBox;
	}

	/**
	 * @param boundingBox the boundingBox to set
	 */
	public void setBoundingBox(Rectangle boundingBox) {
		this.bBox = bBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameEntity#getLayer()
	 */
	@Override
	public int getLayer() {
		return layer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameEntity#getPriority()
	 */
	@Override
	public int getPriority() {
		return priority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameEntity#getX()
	 */
	@Override
	public float getX() {
		return x;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameEntity#getY()
	 */
	@Override
	public float getY() {
		return y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameEntity#getHeight()
	 */
	@Override
	public float getHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.snapgames.bgf.core.entity.GameEntity#getWidth()
	 */
	@Override
	public float getWidth() {
		return width;
	}

}
