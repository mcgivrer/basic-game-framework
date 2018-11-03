/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.snapgames.bgf.InputListener.KeyBinding;

/**
 * a Simple Application as a Basic Game framework.
 * 
 * @author Frédéric Delorme.
 * @since 2018
 * @see https://github.com/snapgames/basic-game-framework/wiki
 */
public class App extends JPanel {

	private static final long serialVersionUID = 2924281870738631982L;

	private static final Logger logger = LoggerFactory.getLogger(App.class.getCanonicalName());
	/**
	 * default path to store image captures.
	 */
	private static String path = System.getProperty("user.home") + File.separator + "screenshots";

	/**
	 * title of the application;
	 */
	private String title = "NoName";

	/**
	 * <p>Internal flags</p>
	 * <ul>
	 * <li><code>exit</code> a flag to request exit of the game.</li>
	 * <li><code>pause</code> a flag to request pause</li>
	 * <li><code>pauseRendering</code> a flag to request a pause of the rendering process.</li>
	 * <li><code>fullScreen</code> a flag to request switching between full screen and window.</li>
	 * </ul>
	 */
	private boolean exit = false;
	private boolean pause = false;
	private boolean pauseRendering = false;
	private boolean fullScreen = false;

	/**
	 * Graphical and loop parameters
	 */
	private int debug = 0;
	public Rectangle backupRectangle = new Rectangle();
	private Window win;
	private long FPS = 60;
	private long timeFrame = (1000 / FPS);
	private long realFPS = 0;

	private InputListener inputListener;

	private Render render;

	GameStateManager gsm;

	/**
	 * Translated Messages
	 */
	private ResourceBundle msg = ResourceBundle.getBundle("messages");

	/**
	 * Create a new Application with <code>title</code> as main title.
	 * 
	 * @param title the title of this app.
	 */
	public App(String title, String[] args) {
		super();
		this.title = title;
		render = new Render(this, new Rectangle(320, 240));
		parseArgs(args);
		gsm = new GameStateManager(this);
		gsm.add(new SampleGameState(), true);
		inputListener = new InputListener(this);
	}

	/**
	 * Return the title for this app.
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Initialize rendering pipeline and other stuff.
	 */
	public void initialize() {
		this.addKeyListener(inputListener);
		prepareKeyBinding();

		// render = new Render(this, new Rectangle(WIDTH, HEIGHT));
		win = new Window(this);
		gsm.initialize(this);
		gsm.switchState(this, "SampleGameState");
	}

	/**
	 * Bind all keys for the game.
	 */
	private void prepareKeyBinding() {
		Map<KeyBinding, Integer> myKB = new HashMap<>();
		myKB.put(KeyBinding.UP, KeyEvent.VK_UP);
		myKB.put(KeyBinding.DOWN, KeyEvent.VK_DOWN);
		myKB.put(KeyBinding.LEFT, KeyEvent.VK_LEFT);
		myKB.put(KeyBinding.RIGHT, KeyEvent.VK_RIGHT);

		myKB.put(KeyBinding.FIRE1, KeyEvent.VK_NUMPAD2);
		myKB.put(KeyBinding.FIRE2, KeyEvent.VK_NUMPAD5);
		myKB.put(KeyBinding.FIRE3, KeyEvent.VK_NUMPAD3);
		myKB.put(KeyBinding.FIRE4, KeyEvent.VK_NUMPAD6);

		myKB.put(KeyBinding.SCREENSHOT, KeyEvent.VK_F3);
		myKB.put(KeyBinding.PAUSE, KeyEvent.VK_P);
		myKB.put(KeyBinding.QUIT, KeyEvent.VK_ESCAPE);
		myKB.put(KeyBinding.DEBUG, KeyEvent.VK_D);
		myKB.put(KeyBinding.RESET, KeyEvent.VK_DELETE);
		myKB.put(KeyBinding.FULLSCREEN, KeyEvent.VK_F11);
		inputListener.prepareKeyBinding(myKB);
	}

	/**
	 * The execution entry point for this Runnable thread.
	 */
	public void run() {
		long elapsed = 0;
		long current = System.currentTimeMillis();
		long previous = current;

		long cumulation = 0;
		long frames = 0;
		initialize();

		while (!exit) {
			current = System.currentTimeMillis();
			if (!pause) {
				gsm.input(this, inputListener);
				gsm.update(this, elapsed);
			}
			if (!pauseRendering) {
				render.clearRenderBuffer();
				render.renderToBuffer();
				render.drawBufferToScreen();
			}
			elapsed = System.currentTimeMillis() - previous;
			cumulation += elapsed;
			frames++;
			if (cumulation > 1000) {
				cumulation = 0;
				realFPS = frames;
				frames = 0;
			}
			if (elapsed < timeFrame) {
				try {
					Thread.sleep(timeFrame - elapsed);
				} catch (InterruptedException ie) {
					System.err.printf("unable to wait some millis: %s", ie.getMessage());
				}
			}
			previous = current;
		}
		System.exit(0);
	}

	/**
	 * Retrieve the label from messages.properties file corresponding to
	 * <code>key</code> value.
	 * 
	 * @param key the key of the label from messages.properties.
	 * @return the value corresponding to the key.
	 */
	public String getLabel(String key) {
		assert (key != null);
		assert (!"".equals(key));
		assert (msg != null);
		return msg.getString(key);
	}

	/**
	 * swtch debug mode.
	 */
	public void switchDebugMode() {
		debug = (debug < 5 ? debug + 1 : 0);
		render.setDebugMode(debug);
	}

	/**
	 * switch pause mode.
	 */
	public void switchPause() {
		pause = !pause;
	}

	/**
	 * Set frame for this app.
	 * 
	 * @param frame
	 */
	public void setBoundAs(JFrame frame) {
		this.backupRectangle = frame.getBounds();
	}

	public void setSize(Dimension rect) {
		float wScale = (float) rect.width / WIDTH;
		float hScale = (float) rect.height / HEIGHT;
		render.setScale((hScale > wScale ? hScale : wScale));
		super.setSize(rect);

	}

	/**
	 * Take a screenshot from the image to the default `user.dir`.
	 * 
	 * @param image image to be saved to disk.
	 */
	public static void screenshot(App app, BufferedImage image) {
		int scindex = 0;
		app.suspendRendering(true);
		try {
			File out = new File(path + File.separator + "screenshot-" + System.nanoTime() + "-" + (scindex++) + ".png");
			javax.imageio.ImageIO.write(image.getSubimage(0, 0, App.WIDTH, App.HEIGHT), "PNG", out);
		} catch (Exception e) {
			System.err.println("Unable to write screenshot to " + path);
		}
		app.suspendRendering(false);
	}

	/**
	 * Parse arguments from the command line and set corresponding values.
	 * 
	 * @param args array of parameters from command line.
	 */
	private void parseArgs(String[] args) {
		int areaWidth = 320;
		int areaHeight = 240;
		float renderPixelScale = 2.0f;
		int debugMode = 0;
		boolean fullScreenFlag = false;
		if (args.length > 0) {
			for (String arg : args) {
				if (arg.contains("=")) {
					String[] argSplit = arg.split("=");
					switch (argSplit[0].toLowerCase()) {
					case "w":
					case "width":
						areaWidth = Integer.parseInt(argSplit[1]);
						logger.debug("Width set to {}", areaWidth);
						break;
					case "h":
					case "height":
						areaHeight = Integer.parseInt(argSplit[1]);
						logger.debug("Height set to {}", areaHeight);
						break;
					case "s":
					case "scale":
						renderPixelScale = Float.parseFloat(argSplit[1]);
						logger.debug("pixel Scale set to {}", renderPixelScale);

						break;
					case "d":
					case "debug":
						debugMode = (Integer.parseInt(argSplit[1]) < 5 && Integer.parseInt(argSplit[1]) >= 0
								? Integer.parseInt(argSplit[1])
								: 0);
						logger.debug("debug mode set to {}", debugMode);

						break;
					case "fps":
						setFPS(Integer.parseInt(argSplit[1]));
						break;

					case "f":
					case "fullscreen":
						fullScreenFlag = (argSplit[1].toLowerCase().equals("on") ? true : false);
						break;
					default:
						break;
					}
				}
			}
		}

		// dispatch values to components
		render.initialize(areaWidth, areaHeight, renderPixelScale);
		render.setDebugMode(debugMode);
		if (win != null) {
			win.switchFullScreen(fullScreenFlag);
		}
	}

	/**
	 * set hte Frame Per Seconds.
	 * 
	 * @param fps the value of the frame per second.
	 */
	private void setFPS(long fps) {
		FPS = fps;
		timeFrame = (long) (1000 / FPS);
	}

	/**
	 * return the list of objects for this game..
	 * 
	 * @return a list of GameObject.
	 */
	public List<GameObject> getObjects() {
		// List<Value> values = map.values().stream().collect(Collectors.toList());
		return gsm.currentState.getObjects().values().stream().collect(Collectors.toList());
	}

	/**
	 * Set Pause mode.
	 * 
	 * @param b a flag to set the App in pause mode (true).
	 */
	public void setPause(boolean b) {
		this.pause = b;
	}

	/**
	 * Set rendering pause mode.
	 * 
	 * @param b boolean to freeze the rendering pipeline (true)
	 */
	public void suspendRendering(boolean b) {
		this.pauseRendering = b;
	}

	/**
	 * Return the Window created for this App.
	 * 
	 * @return the Window instance.
	 */
	public Window getWindow() {
		return win;
	}

	/**
	 * return the debug mode.
	 * <ul>
	 * <li>0 debug is off</li>
	 * <li>1 debug display pipeline rendering information</li>
	 * <li>2 debug show all main attributes for managed GameObject's</li>
	 * </ul>
	 * 
	 * @return
	 */
	public int getDebugMode() {
		return debug;
	}

	/**
	 * set args list to be parsed.
	 * 
	 * @param args the list of parameters to be parsed.
	 */
	public void setArgs(String[] args) {
		this.parseArgs(args);
	}

	/**
	 * return the InputListener.
	 * 
	 * @return an InputListener attached to this App.
	 */
	public KeyListener getInputListener() {
		return inputListener;
	}

	/**
	 * return the real Frame per second rendered.
	 * 
	 * @return the number of frames rendered per seconds.
	 */
	public long getRealFPS() {
		return realFPS;
	}

	/**
	 * Return the Render instance for this App.
	 * 
	 * @return a Render instance.
	 */
	public Render getRender() {
		return render;
	}

	/**
	 * Request to Main App to exit.
	 * 
	 * @param b
	 */
	public void requestExit() {
		exit = true;
	}

	public void switchFullScreen() {
		fullScreen = !fullScreen;
		win.switchFullScreen(fullScreen);
	}

	public boolean isPause() {
		return pause;
	}

	/**
	 * App execution EntryPoint.
	 * 
	 * @param args the command line arguments passed to the main method.
	 */
	public static void main(String[] args) {
		App app = new App("BGF", args);
		app.run();
	}

}
