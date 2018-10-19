/**
 * SnapGames
 * 
 * Game Development Java
 * 
 * basic-game-framework
 * 
 * @year 2018
 */
package fr.snapgames.bgf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Internal object managed by the {@link App}.
 * 
 * This <code>GameObject</code> is a graphical entity to be moved and animated
 * by the parent Game.
 * 
 * The <code>GameObject</code> is referenced into 2 {@link App} attributes :
 * <ul>
 * <li>into the {@link App#objects} map for an object's management purpose,</li>
 * <li>into the {@link App#renderingList} as a sort rendering list of graphical
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
 * easily new <code>GameObject</code>, like
 * {@link GameObject#setPosition(int, int)} or
 * {@link GameObject#setVelocity(float, float)}.
 * </p>
 * 
 */
public class GameObject {

	public int id;
	public String name;

	public int layer = 0;
	public int priority = 0;

	public int x = 0, y = 0, width = 16, height = 16;

	public float dx = 0, dy = 0;
	public float friction = 0.13f;
	public float elasticity = 0.98f;

	public BufferedImage image;
	public Color color;

	public Rectangle boundingBox = new Rectangle(0, 0, 0, 0);

	/**
	 * Create a new Gobject.
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
		setPosition(x, y); 
	}

	/**
	 * compute object mechanism.
	 * 
	 * @param dt
	 */
	public void update(long dt) {
		setPosition((int) (x + (dx * dt)), (int) (y + (dy * dt)));
	}

	/**
	 * Draw object to buffer.
	 * 
	 * @param g
	 */
	public void render(Graphics2D g) {
		if (image != null) {
			g.drawImage(image, x, y, null);
		} else {
			g.setColor(color);
			g.fillRect(x, y, width, height);
		}
	}

	/**
	 * Set Position.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public GameObject setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		boundingBox.x = x;
		boundingBox.x = y;
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
		boundingBox.width = width;
		boundingBox.height = height;
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
	 * Builder pattern for the GameObject.
	 * 
	 * @param name
	 * @return
	 */
	public static GameObject builder(String name) {
		return new GameObject(name);
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder()
		.append("GameObject{")
		.append("name='").append(name).append("'")
		.append(",layer=").append(layer)
		.append(",priority=").append(priority)
		.append("}");
		return builder.toString();
	}

}