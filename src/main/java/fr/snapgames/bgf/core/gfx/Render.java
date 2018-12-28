/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.gfx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.snapgames.bgf.core.Game;
import fr.snapgames.bgf.core.entity.Camera;
import fr.snapgames.bgf.core.entity.GameEntity;
import fr.snapgames.bgf.core.entity.GameObject;

/**
 * The Render class is the rendering processor for all GameObject to an internal
 * buffer. In a second step, this rendered buffer will be later copy to window
 * frame.
 * 
 * @author Frédéric Delorme
 *
 */
public class Render {

	private final static Logger logger = LoggerFactory.getLogger(Render.class);

	public Game app;

	private int WIDTH = 320;
	private int HEIGHT = 240;
	private float SCALE = 2;

	private DebugEngine dEngine;

	private Rectangle viewport;
	private Dimension dimension;
	private Camera camera;

	private int debug = 0;
	private Font dbgFont;

	private boolean pause;
	private BasicStroke basicStroke = new BasicStroke(0.5f);

	/**
	 * Rendering pipeline
	 */
	private BufferedImage buffer;
	private Graphics2D g;

	/**
	 * List of object to be rendered.
	 */
	public List<GameEntity> renderingList = new CopyOnWriteArrayList<>();

	/**
	 * default path to store image captures.
	 */
	private static String path = System.getProperty("user.home") + File.separator + "screenshots";

	/**
	 * Initialize the renderer with a viewport size.
	 * 
	 * @param app      the parent Game
	 * @param viewPort the requested viewPort.
	 */
	public Render(Game app, Rectangle viewPort) {
		this.app = app;
		this.viewport = viewPort;
		this.dimension = new Dimension(viewPort.width, viewPort.width);
		this.buffer = new BufferedImage(viewPort.width, viewPort.width, BufferedImage.TYPE_INT_ARGB);
		this.g = (Graphics2D) this.buffer.getGraphics();
		dbgFont = g.getFont().deriveFont(9.0f);
		dEngine = new DebugEngine(g);
	}

	/**
	 * clear the graphic buffer.
	 */
	public void clearRenderBuffer() {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * Render the game screen.
	 */
	public void drawToRenderBuffer(Game app) {

		// prepare pipeline anti-aliasing.
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Camera preRender operation
		if (app.getActiveCamera() != null) {
			app.getActiveCamera().preRender(app, g);
		} else if (camera != null) {
			camera.preRender(app, g);
		}
		// render anything game ?
		int previousLayer = 0, layer = 0;
		for (GameEntity o : renderingList) {
			layer = o.getLayer();
			if (previousLayer == layer) {
				logger.debug("Draw objects from layer {}", layer);
			}
			o.render(g);
			if (debug >= 2) {
				o.getBoundingBox().render(g);
			}
			if (debug >= 3) {
				dEngine.drawObjectDebugInfo(this,g, o);
			}
			previousLayer = layer;
		}

		if (debug >= 2) {
			drawViewPort(app, g);
		}

		// Camera postRender operation
		if (app.getActiveCamera() != null) {
			app.getActiveCamera().postRender(app, g);
		} else if (camera != null) {
			camera.postRender(app, g);
		}

		// render pause status
		if (pause) {
			String pauseLabel = app.getLabel("app.label.pause");

			g.setColor(new Color(0.3f, 0.3f, 0.3f, 0.8f));
			g.fillRect(0, HEIGHT / 2, WIDTH, 32);

			g.setColor(Color.GRAY);
			g.drawRect(-2, HEIGHT / 2, WIDTH + 4, 32);

			g.setColor(Color.WHITE);
			g.setFont(g.getFont().deriveFont(18.0f));

			Render.drawOutlinedString(g, (WIDTH - g.getFontMetrics().stringWidth(pauseLabel)) / 2,
					(HEIGHT + g.getFontMetrics().getHeight() + 24) / 2, pauseLabel, 2, Color.WHITE, Color.BLACK);
		}

		// render debug information
		if (debug > 0) {
			dEngine.drawGlobalDebugInformation(this,g);

		}
	}

	private void drawViewPort(Game app, Graphics2D g) {
		g.setColor(Color.ORANGE);
		g.setStroke(basicStroke);
		g.drawRect(viewport.x, viewport.y, viewport.width, viewport.height);
	}

	/**
	 * Draw graphic Buffer to screen with the appropriate scaling.
	 */
	public void drawRenderBufferToScreen() {
		Graphics g2 = app.getGraphics();
		g2.drawImage(buffer, 0, 0, (int) (WIDTH * SCALE), (int) (HEIGHT * SCALE), null);
		g2.dispose();
	}

	/**
	 * Draw <code>text</code> with <code>foreground</code> color adding outlines
	 * width a <code>shadow</code> color, of <code>thickness</code> draw.
	 * 
	 * @param g          Graphics interface to be use.
	 * @param x          the X position of the text.
	 * @param y          the Y position of the text.
	 * @param text       the text to be drawn.
	 * @param thickness  the thickness of the outline.
	 * @param foreground the foreground color.
	 * @param shadow     the shadow color.
	 */
	public static void drawOutlinedString(Graphics2D g, int x, int y, String text, int thickness, Color foreground,
			Color shadow) {
		g.setColor(shadow);
		for (int i = -thickness; i <= thickness; i++) {
			g.drawString(text, x + i, y);
		}
		for (int i = -thickness; i <= thickness; i++) {
			g.drawString(text, x, y + i);
		}
		g.setColor(foreground);
		g.drawString(text, x, y);
	}

	/**
	 * Add an object to the rendering List.
	 * 
	 * @param go the GameObject to be added.
	 */
	public void addObject(GameObject go) {
		renderingList.add(go);
		renderingList.sort(new Comparator<GameEntity>() {
			public int compare(GameEntity o1, GameEntity o2) {
				// System.out.printf("comparison (%s,%s) => %d\r\n",o1,o2,(o1.layer < o2.layer ?
				// -1 : (o1.priority < o2.priority ? -1 : 1)));
				return (o1.getLayer() < o2.getLayer() ? -1 : (o1.getPriority() < o2.getPriority() ? -1 : 1));
			}
		});
	}

	/**
	 * Add All objects to the rendering pipe.
	 * 
	 * @param l the list of GameObject to ad dthe the rendering pipeline.
	 */
	public void addAllObjects(Collection<GameEntity> l) {
		renderingList.addAll(l);
	}

	/**
	 * Remove an object from the rendering list.
	 * 
	 * @param go remove an object from the rendering pipeline.
	 */
	public void removeObject(GameEntity go) {
		renderingList.remove(go);
	}

	/**
	 * Clear the rendering list.
	 */
	public void clearRenderingList() {
		renderingList.clear();
	}

	/**
	 * Return Graphics2D object for this render.
	 * 
	 * @return
	 */
	public Graphics2D getGraphics() {
		return (Graphics2D) buffer.getGraphics();
	}

	/**
	 * get Rendering viewport.
	 * 
	 * @return
	 */
	public Rectangle getViewport() {
		return viewport;
	}

	/**
	 * get the internal buffer
	 * 
	 * @return
	 */
	public BufferedImage getBuffer() {
		return buffer;
	}

	/**
	 * get the Bounds of this render.
	 * 
	 * @return
	 */
	public Dimension getBounds() {
		return dimension;
	}

	/**
	 * Set the debug mode
	 * 
	 * @param the new debug mode to switch to.
	 */
	public void setDebugMode(int debug) {
		this.debug = debug;
	}

	/**
	 * return the debug mode status.
	 * 
	 * @return
	 */
	public int getDebugMode() {
		return this.debug;
	}

	/**
	 * Set the Pixel Scale
	 * 
	 * @param scale new pixel scale to set.
	 */
	public void setScale(float scale) {
		this.SCALE = scale;
	}

	public void setCamera(Camera cam) {
		this.camera = cam;
	}

	/**
	 * return the pixel scale.
	 * 
	 * @return value of the current pixel scale.
	 */
	public float getScale() {
		return SCALE;
	}

	/**
	 * Initialize the rendering pipe and the Rendering area.
	 * 
	 * @param w the new Width of the buffer for the rendering pipeline
	 * @param h the new Height of the buffer for the rendering pipeline
	 * @param s the new pixel scale of the buffer for the rendering pipeline
	 */
	public void initialize(int w, int h, float s) {
		this.dimension = new Dimension(w, h);
		this.SCALE = s;
		this.viewport = new Rectangle(0, 0, w, h);
		this.buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		this.g = (Graphics2D) this.buffer.getGraphics();
		clearRenderBuffer();
		clearRenderingList();
	}

	/**
	 * return the scaled width for the game.
	 * 
	 * @return
	 */
	public int getDisplayWidth() {
		return (int) (WIDTH * SCALE);
	}

	/**
	 * return the scaled height for the game.
	 * 
	 * @return
	 */
	public int getDisplayHeight() {
		return (int) (HEIGHT * SCALE);
	}

	/**
	 * return the rendering list of objects.
	 * 
	 * @return a list of GameObject.
	 */
	public List<GameEntity> getRenderingList() {
		return renderingList;
	}

	/**
	 * Take a screenshot from the image to the default `user.dir`.
	 * 
	 * @param image image to be saved to disk.
	 */
	public static void screenshot(Game app) {
		int scindex = 0;
		app.suspendRendering(true);

		Path targetDir = Paths.get(path + File.separator);
		String filename = path + File.separator + app.getTitle() + "-sc-" + System.nanoTime() + "-" + (scindex++)
				+ ".png";
		try {
			if (!Files.exists(targetDir)) {
				Files.createDirectory(targetDir);
			}
			File out = new File(filename);
			javax.imageio.ImageIO.write(app.getRender().getBuffer(), "PNG", out);
		} catch (IOException e) {
			logger.error("Unable to write screenshot to " + filename, e);
		}
		app.suspendRendering(false);
	}

}
