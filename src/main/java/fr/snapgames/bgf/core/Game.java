/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.snapgames.bgf.core.audio.SoundControl;
import fr.snapgames.bgf.core.entity.Camera;
import fr.snapgames.bgf.core.entity.GameEntity;
import fr.snapgames.bgf.core.gfx.Render;
import fr.snapgames.bgf.core.gfx.Window;
import fr.snapgames.bgf.core.io.InputListener;
import fr.snapgames.bgf.core.io.InputListener.KeyBinding;
import fr.snapgames.bgf.core.resources.ResourceManager;
import fr.snapgames.bgf.core.states.GameStateManager;
import fr.snapgames.bgf.sample.states.SampleGameState;

/**
 * a Simple Application as a Basic Game framework.
 * 
 * @author Frédéric Delorme.
 * @since 2018
 * @see https://github.com/snapgames/basic-game-framework/wiki
 */
public class Game extends JPanel {

	private static final long serialVersionUID = 2924281870738631982L;

	private static final Logger logger = LoggerFactory.getLogger(Game.class.getCanonicalName());

	public String jarPath = "";

	/**
	 * title of the application;
	 */
	private String title = "NoName";

	/**
	 * <p>
	 * Internal flags
	 * </p>
	 * <ul>
	 * <li><code>exit</code> a flag to request exit of the game.</li>
	 * <li><code>pause</code> a flag to request pause</li>
	 * <li><code>pauseRendering</code> a flag to request a pause of the rendering
	 * process.</li>
	 * <li><code>fullScreen</code> a flag to request switching between full screen
	 * and window.</li>
	 * </ul>
	 */
	private boolean exit = false;
	private boolean pause = false;
	private boolean pauseRendering = false;
	private boolean fullScreen = false;
	private boolean audioMode = false;

	/**
	 * Graphical and loop parameters
	 */
	private int debug = 0;

	private long FPS = 60;
	private long timeFrame = (1000 / FPS);
	private long realFPS = 0;

	public Rectangle backupRectangle = new Rectangle();

	private Window win;

	/**
	 * The InputListener to manage all input !
	 */
	private InputListener inputListener;

	/**
	 * the internal render component.
	 */
	private Render render;

	/**
	 * the manager to switch GameState's.
	 */
	private GameStateManager gsm;

	public ResourceManager resManager;

	public SoundControl soundCtrl;

	/**
	 * Translated Messages
	 */
	private ResourceBundle msg = ResourceBundle.getBundle("messages");

	/**
	 * Create a new Application with <code>title</code> as main title.
	 * 
	 * @param title the title of this app.
	 */
	public Game(String title, String[] args) {
		super();
		this.title = title;
		render = new Render(this, new Rectangle(320, 240));
		parseArgs(args);
		gsm = new GameStateManager(this);
		inputListener = new InputListener(this);
		resManager = new ResourceManager();
		soundCtrl = SoundControl.getInstance(this);

		String path = Game.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			jarPath = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Unable to detect the path for this jar file", e);
		}

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

		// Add a sample State
		gsm.add(new SampleGameState(), true);
		gsm.initialize(this);
		gsm.switchState(this, SampleGameState.NAME);
	}

	/**
	 * Bind all keys for the game.
	 */
	private void prepareKeyBinding() {
		Map<KeyBinding, Integer> myKB = new HashMap<>();
		myKB = loadMappingKey();

		if (myKB.size() == 0) {
			// temporary set key/action mapping
			myKB.put(KeyBinding.UP, KeyEvent.VK_UP);
			myKB.put(KeyBinding.DOWN, KeyEvent.VK_DOWN);
			myKB.put(KeyBinding.LEFT, KeyEvent.VK_LEFT);
			myKB.put(KeyBinding.RIGHT, KeyEvent.VK_RIGHT);

			myKB.put(KeyBinding.FIRE1, KeyEvent.VK_NUMPAD0);
			myKB.put(KeyBinding.FIRE2, KeyEvent.VK_NUMPAD1);
			myKB.put(KeyBinding.FIRE3, KeyEvent.VK_NUMPAD2);
			myKB.put(KeyBinding.FIRE4, KeyEvent.VK_NUMPAD3);

			myKB.put(KeyBinding.PAUSE, KeyEvent.VK_P);
			myKB.put(KeyBinding.QUIT, KeyEvent.VK_ESCAPE);

			myKB.put(KeyBinding.SCREENSHOT, KeyEvent.VK_F3);
			myKB.put(KeyBinding.DEBUG, KeyEvent.VK_D);
			myKB.put(KeyBinding.RESET, KeyEvent.VK_DELETE);
			myKB.put(KeyBinding.FULLSCREEN, KeyEvent.VK_F11);

			writeKeyMapping(myKB);
		}
		inputListener.prepareKeyBinding(myKB);
	}

	/**
	 * Write the key action/mapping
	 * 
	 * @param myKB
	 */
	private void writeKeyMapping(Map<KeyBinding, Integer> myKB) {

		Gson gson = new Gson();
		String path = System.getProperty("user.home");
		logger.info("Keymapping:" + gson.toJson(myKB));
		try {
			Path filePath = Paths.get(path + "/keymapping.json").toAbsolutePath();
			Files.write(filePath, gson.toJson(myKB).getBytes("utf-8"));
			logger.debug("mapping keys:" + "keymapping.json");
		} catch (Exception ioe) {
			logger.error("Unable to write the file", ioe);
		}

	}

	private Map<KeyBinding, Integer> loadMappingKey() {
		Gson gson = new Gson();
		HashMap<KeyBinding, Integer> myKB = new HashMap<>();
		logger.info("Keymapping:" + gson.toJson(myKB));
		try {

			Path filePath = Paths.get(jarPath.substring(1) + "/keymapping.json").toAbsolutePath();
			if(Files.exists(filePath)){
				String file = new String(Files.readAllBytes(filePath));
				myKB = gson.fromJson(file, new TypeToken<Map<KeyBinding, Integer>>() {
				}.getType());
				logger.info("mapping keys:" + "keymapping.json = " + myKB.toString());
			}else{
				logger.info("Keymapping.json file does not exist, will create it.");
			}
		} catch (Exception ioe) {
			logger.error("Unable to write the file", ioe);
		}
		return myKB;
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
				gsm.render(this, render);
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
	 * Switch debug mode.
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

	/**
	 * Reset the size of the game display.
	 * 
	 * @param rect the Dimension of the new viewport to be set.
	 */
	public void setSize(Dimension rect) {
		float wScale = (float) rect.width / WIDTH;
		float hScale = (float) rect.height / HEIGHT;
		render.setScale((hScale > wScale ? hScale : wScale));
		super.setSize(rect);
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
					case "a":
					case "audio":
						audioMode = (argSplit[1].toLowerCase().equals("on") ? true : false);
						logger.debug("audio mode set to {}", audioMode);

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
	public List<GameEntity> getObjects() {
		return gsm.getCurrentState().getObjects().values().stream().collect(Collectors.toList());
	}

	/**
	 * Set Pause mode.
	 * 
	 * @param b a flag to set the Game in pause mode (true).
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
	 * Return the Window created for this Game.
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
	 * @return an InputListener attached to this Game.
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
	 * Return the Render instance for this Game.
	 * 
	 * @return a Render instance.
	 */
	public Render getRender() {
		return render;
	}

	/**
	 * return the GameState manager.
	 * 
	 * @return
	 */
	public GameStateManager getGSM() {
		return gsm;
	}

	/**
	 * Request to Main Game to exit.
	 * 
	 * @param b
	 */
	public void requestExit() {
		exit = true;
	}

	/**
	 * request a full switch between windows and fullscreen mode.
	 */
	public void switchFullScreen() {
		fullScreen = !fullScreen;
		win.switchFullScreen(fullScreen);
	}

	/**
	 * Return true if the main Game is in pause mode.
	 * 
	 * @return true if in pause or not.
	 */
	public boolean isPause() {
		return pause;
	}

	/**
	 * Game execution EntryPoint.
	 * 
	 * @param args the command line arguments passed to the main method.
	 */
	public static void main(String[] args) {
		Game app = new Game("BGF", args);
		app.run();
	}

	/**
	 * Get the current active Camera.
	 * 
	 * @return the current active Camera object in the current active GameState.
	 */
	public Camera getActiveCamera() {
		return gsm.getCurrentState().getActiveCamera();
	}
	
	public boolean getAudioOff() {
		return audioMode;
	}
}
