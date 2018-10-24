package fr.snapgames.bgf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a Simple Application as a Basic Game framework.
 * 
 * @author Frédéric Delorme.
 * @year 2018
 * @see https://github.com/snapgames/basic-game-framework/wiki
 */
public class App extends JPanel implements KeyListener {

	private static final long serialVersionUID = 2924281870738631982L;

	private static final Logger logger = LoggerFactory.getLogger(App.class.getCanonicalName());

	/**
	 * This an enum listing all possible input actions
	 */
	private enum KeyBinding {
		UP, DOWN, LEFT, RIGHT, DELETE, PAUSE, QUIT, DEBUG, FIRE1, FIRE2, FIRE3, FN1, FN2, FN3, SCREENSHOT, RESET,
		FULLSCREEN;
	}

	/**
	 * Map for keybinding.
	 */
	private Map<KeyBinding, Integer> keyBinding = new HashMap<>();

	/**
	 * default path to store image captures.
	 */
	private static String path = System.getProperty("user.home");

	/**
	 * Game display size and scale.
	 */
	public static int WIDTH = 320;
	public static int HEIGHT = 240;
	public static float SCALE = 2;

	/**
	 * title of the application;
	 */
	private String title = "NoName";

	/**
	 * internal flags.
	 */
	private boolean exit = false;
	private boolean pause = false;
	private boolean pauseRendering = false;

	private boolean fullScreen = false;
	private int debug = 0;
	private Font dbgFont;

	/**
	 * Rendering pipeline
	 */
	private BufferedImage buffer;
	private Graphics2D g;
	private Rectangle viewport;

	public Rectangle backupRectangle = new Rectangle();

	private Window win;

	private long FPS = 60;
	private long timeFrame = (1000 / FPS);
	private long realFPS = 0;

	private int score = 0;

	/**
	 * Translated Messages
	 */
	private ResourceBundle msg = ResourceBundle.getBundle("messages");

	/**
	 * Key processing arrays.
	 */
	private boolean[] keys = new boolean[65536];
	private Queue<KeyEvent> keyQueue = new ConcurrentLinkedQueue<KeyEvent>();

	/**
	 * GameObject list managed by the game.
	 */
	private Map<String, GameObject> objects = new ConcurrentHashMap<String, GameObject>();
	/**
	 * List of object to be rendered.
	 */
	private List<GameObject> renderingList = new CopyOnWriteArrayList<>();

	private GameObject player;

	private UIText scoreUI;

	private Font scoreFont;

	private JFrame frame;

	/**
	 * Create a new Application with <code>title</code> as main title.
	 * 
	 * @param title the title of this app.
	 */
	public App(String title, String[] args) {
		super();
		this.title = title;
		parseArgs(args);
		this.addKeyListener(this);
		win = new Window(this);
		prepareKeyBinding();
	}

	/**
	 * Bind all keys for the game.
	 */
	private void prepareKeyBinding() {
		keyBinding.clear();
		keyBinding.put(KeyBinding.UP, KeyEvent.VK_UP);
		keyBinding.put(KeyBinding.DOWN, KeyEvent.VK_DOWN);
		keyBinding.put(KeyBinding.LEFT, KeyEvent.VK_LEFT);
		keyBinding.put(KeyBinding.RIGHT, KeyEvent.VK_RIGHT);

		keyBinding.put(KeyBinding.FIRE1, KeyEvent.VK_PAGE_UP);
		keyBinding.put(KeyBinding.FIRE2, KeyEvent.VK_PAGE_DOWN);
		keyBinding.put(KeyBinding.FIRE3, KeyEvent.VK_SPACE);

		keyBinding.put(KeyBinding.SCREENSHOT, KeyEvent.VK_F3);
		keyBinding.put(KeyBinding.PAUSE, KeyEvent.VK_P);
		keyBinding.put(KeyBinding.QUIT, KeyEvent.VK_ESCAPE);
		keyBinding.put(KeyBinding.DEBUG, KeyEvent.VK_D);
		keyBinding.put(KeyBinding.RESET, KeyEvent.VK_DELETE);
		keyBinding.put(KeyBinding.FULLSCREEN, KeyEvent.VK_F11);
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
		buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) buffer.getGraphics();
		viewport = new Rectangle(buffer.getWidth(), buffer.getHeight());

		dbgFont = g.getFont().deriveFont(9.0f);
		scoreFont = g.getFont().deriveFont(16.0f);
		this.addKeyListener(this);

		scoreUI = (UIText) UIText.builder("score").setFont(scoreFont).setText("00000").setThickness(1)
				.setPosition(12, 24).setLayer(10).setElasticity(0.98f).setFriction(0.98f).setLayer(20);
		add(scoreUI);

		player = GameObject.builder("player").setSize(24, 24).setPosition(0, 0).setColor(Color.GREEN)
				.setVelocity(0.0f, 0.0f).setLayer(10).setPriority(100).setElasticity(0.98f).setFriction(0.98f);
		add(player);

		createGameObjects("enemy_", 10);

	}

	/**
	 * Create nbEnemies in the playground.
	 * 
	 * @param nbEnemies
	 */
	private void createGameObjects(String baseName, int nbEnemies) {
		pauseRendering = true;
		for (int i = 0; i < nbEnemies; i++) {
			GameObject enemy = GameObject.builder(baseName + objects.size() + 1).setSize(16, 16)
					.setPosition((int) (Math.random() * WIDTH), (int) (Math.random() * HEIGHT)).setColor(randomColor())
					.setVelocity((float) (Math.random() * 0.2f) - 0.1f, (float) (Math.random() * 0.2f) - 0.1f)
					.setPriority(i).setLayer(1).setElasticity(0.98f).setFriction(0.98f);
			add(enemy);
		}
		pauseRendering = false;
	}

	/**
	 * Generate a random Color.
	 * 
	 * @return
	 */
	private Color randomColor() {
		return new Color((float) Math.random(), 0.0f, (float) Math.random());
	}

	/**
	 * Remove {@link GameObject} from the {@link App} where name contains with
	 * nameFilter if nbToRemove equals -1, all corresponding
	 * <code>nameFilter</code>ed object will be removed.
	 * 
	 * @param nameFilter
	 * @param i
	 */
	private void removeGameObjects(String nameFilter, int nbToRemove) {
		pauseRendering = true;

		// parse Object map and remove matching object with filtering string.
		// Map -> Stream -> Filter -> MAP
		Map<String, GameObject> collect = objects.entrySet().stream()
				// filter object on their name
				.filter(x -> x.getKey().contains(nameFilter))
				// add a limit if nbToRemove different of -1.
				.limit((nbToRemove == -1 ? objects.size() : nbToRemove))
				// remap result to a new map.
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

		// remove all matching objects from objects buffer.
		objects.entrySet().removeAll(collect.entrySet());

		// re-fulfill the rendering buffer.
		renderingList.clear();
		renderingList.addAll(objects.values());
		sortRenderingList();
		pauseRendering = false;

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
				input();
				update(elapsed);
			}
			if (!pauseRendering) {
				clearRenderBuffer(g);
				renderToBuffer(g);
				drawBufferToScreen();
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
	 * <p>
	 * Where the is an opportunity to manage async user input:
	 * </p>
	 * <ul>
	 * <li>player movement (all directional keys (up, down, left, right) but also
	 * joypads or joystick (e.g. jinput)</li>
	 * <li>mulitple firing keys</li>
	 * </ul>
	 */
	private void input() {
		GameObject goPlayer = objects.get("player");

		goPlayer.dy *= goPlayer.friction;
		goPlayer.dx *= goPlayer.friction;

		if (keys[KeyEvent.VK_LEFT]) {
			goPlayer.dx = -0.1f;
		}
		if (keys[KeyEvent.VK_RIGHT]) {
			goPlayer.dx = 0.1f;
		}
		if (keys[KeyEvent.VK_UP]) {
			goPlayer.dy = -0.1f;
		}
		if (keys[KeyEvent.VK_DOWN]) {
			goPlayer.dy = 0.1f;
		}
	}

	/**
	 * Update the game mechanism.
	 * 
	 * @param elapsed
	 */
	private void update(long elapsed) {
		for (Entry<String, GameObject> entry : objects.entrySet()) {
			entry.getValue().update(elapsed);
			constrains(entry.getValue());
		}
		score++;
		scoreUI.setText(String.format("%05d", score));
	}

	/**
	 * Contained object to App viewport display.
	 * 
	 * @param o
	 */
	private void constrains(GameObject o) {
		if (o.x + o.width > viewport.width || o.x < 0) {
			o.dx = -o.dx * o.friction * o.elasticity;
		}
		if (o.y + o.height > viewport.height || o.y < 0) {
			o.dy = -o.dy * o.friction * o.elasticity;
		}
		if (Math.abs(o.dx) < 0.01f) {
			o.dx = 0.0f;
		}
		if (Math.abs(o.dy) < 0.01f) {
			o.dy = 0.0f;
		}
	}

	/**
	 * clear the graphic buffer.
	 * 
	 * @param g
	 */
	private void clearRenderBuffer(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * Render the game screen.
	 * 
	 * @param g
	 */
	private void renderToBuffer(Graphics2D g) {

		// prepare pipeline anti-aliasing.
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// render anything game ?
		for (GameObject o : renderingList) {
			o.render(g);
			if (debug >= 2) {
				renderObjectDebugInfo(g, o);
			}
		}

		// render pause status
		if (pause) {
			String pauseLabel = getLabel("app.label.pause");
			g.setColor(new Color(0.3f, 0.3f, 0.3f, 0.8f));
			g.fillRect(0, HEIGHT / 2, WIDTH, 32);
			g.setColor(Color.WHITE);
			g.drawString(pauseLabel, (WIDTH - g.getFontMetrics().stringWidth(pauseLabel)) / 2,
					(HEIGHT + g.getFontMetrics().getHeight()) / 2);
		}

		// render debug information
		if (debug > 0) {
			drawDebugInformation(g);
		}
	}

	/**
	 * Render debug information for the object <code>o</code> to the Graphics2D
	 * <code>g</code>.
	 * 
	 * @param g the Graphics2D to render things.
	 * @param o the object to be debugged.
	 */
	private void renderObjectDebugInfo(Graphics2D g, GameObject o) {
		g.setFont(dbgFont);

		g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.80f));
		g.fillRect(o.x + o.width + 2, o.y, 80, 60);

		g.setColor(Color.DARK_GRAY);
		g.drawRect(o.x + o.width + 2, o.y, 80, 60);

		g.setColor(Color.GREEN);
		g.drawString(String.format("Name:%s", o.name), o.x + o.width + 4, o.y + (12 * 1));
		g.drawString(String.format("Pos:%03d,%03d", o.x, o.y), o.x + o.width + 4, o.y + (12 * 2));
		g.drawString(String.format("Size:%03d,%03d", o.width, o.height), o.x + o.width + 4, o.y + (12 * 3));
		g.drawString(String.format("Vel:%03.2f,%03.2f", o.dx, o.dy), o.x + o.width + 4, o.y + (12 * 4));
		g.drawString(String.format("L/P:%d/%d", o.layer, o.priority), o.x + o.width + 4, o.y + (12 * 5));
	}

	/**
	 * Draw debug information.
	 * 
	 * @param g
	 */
	private void drawDebugInformation(Graphics2D g) {
		g.setFont(dbgFont);
		String debugString = String.format("dbg:%s | FPS:%d | Objects:%d | Rendered:%d",
				(debug == 0 ? "off" : "" + debug), realFPS, objects.size(), renderingList.size());
		int dbgStringHeight = g.getFontMetrics().getHeight() + 8;
		g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.8f));
		g.fillRect(0, HEIGHT - (dbgStringHeight + 8), WIDTH, (dbgStringHeight));
		g.setColor(Color.ORANGE);
		g.drawString(debugString, 4, HEIGHT - dbgStringHeight);
	}

	/**
	 * Draw graphic Buffer to screen with the appropriate scaling.
	 */
	private void drawBufferToScreen() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(buffer, 0, 0, (int) (WIDTH * SCALE), (int) (HEIGHT * SCALE), null);
		g2.dispose();
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
	 * Process key pressed event.
	 * 
	 * @param e
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		logger.debug(e.getKeyCode() + " has been pressed");
	}

	/**
	 * process the key released event.
	 * 
	 * @param e
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		logger.debug(e.getKeyCode() + " has been released");

		for (KeyBinding keyBind : keyBinding.keySet()) {
			if (e.getKeyCode() == keyBinding.get(keyBind)) {
				action(keyBind);
			}
		}
	}

	private void action(KeyBinding keyBind) {
		switch (keyBind) {
		/**
		 * process the exit request.
		 */
		case QUIT:
			this.exit = true;
			logger.debug("Request exiting");
			break;
		/**
		 * process the pause request.
		 */
		case PAUSE:
			this.pause = !pause;
			logger.debug(String.format("Pause reuqest %b", this.pause));
			break;
		/**
		 * Manage Enemies set.
		 */
		case FIRE1:
			createGameObjects("enemy_", 10);
			break;
		case FIRE2:
			removeGameObjects("enemy_", 10);
			break;
		/**
		 * remove all enemies
		 */
		case RESET:
			removeGameObjects("enemy_", -1);
			break;
		/**
		 * 
		 * Write a screenshot to User home folder.
		 */
		case SCREENSHOT:
			pause = true;
			screenshot(this,buffer);
			pause = false;
			break;

		/**
		 * Manage Debug level.
		 */
		case DEBUG:
			debug = (debug < 5 ? debug + 1 : 0);
			break;

		/**
		 * Switch between window and fullscreen mode.
		 */
		case FULLSCREEN:
			fullScreen = !fullScreen;
			win.switchFullScreen(fullScreen);
			break;
				
		default:
			break;
		}
	}

	/**
	 * process the key typed event.
	 * 
	 * @param e
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		keyQueue.add(e);
	}

	/**
	 * retrieve status of a specific key.
	 * 
	 * @param keyCode
	 * @return
	 */
	public boolean getKey(int keyCode) {
		return keys[keyCode];
	}

	/**
	 * get the last event from the KeyEvent queue.
	 * 
	 * @return KeyEvent
	 */
	public KeyEvent getKey() {
		return keyQueue.poll();
	}

	/**
	 * Set frame for this app.
	 * 
	 * @param frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
		this.backupRectangle = this.frame.getBounds();
	}

	public void setSize(Dimension rect) {
		float wScale = (float) rect.width / WIDTH;
		float hScale = (float) rect.height / HEIGHT;
		SCALE = (hScale > wScale ? hScale : wScale);
		super.setSize(rect);

	}

	/**
	 * Add a GameObject to the game.
	 * 
	 * @param go
	 */
	public void add(GameObject go) {
		pauseRendering = true;
		objects.put(go.name, go);
		renderingList.add(go);
		sortRenderingList();
		pauseRendering = false;
		logger.debug("Add object %s",go);
	}

	private void sortRenderingList() {
		renderingList.sort(new Comparator<GameObject>() {
			public int compare(GameObject o1, GameObject o2) {
				//System.out.printf("comparison (%s,%s) => %d\r\n",o1,o2,(o1.layer < o2.layer ? -1 : (o1.priority < o2.priority ? -1 : 1)));
				return (o1.layer < o2.layer ? -1 : (o1.priority < o2.priority ? -1 : 1));
			}
		});
	}

	/**
	 * remove GameObject from management.
	 * 
	 * @param go
	 */
	public void remove(GameObject go) {
		objects.remove(go.name);
		renderingList.remove(go);
		logger.debug("Object %s removed",go);
	}

	/**
	 * Take a screenshot from the image to the default `user.dir`.
	 * 
	 * @param image image to be saved to disk.
	 */
	public static void screenshot(App app,BufferedImage image) {
		int scindex=0;
		app.suspendRendering(true);
		try {
			File out = new File(path + File.separator + "screenshot-" + System.nanoTime() +"-"+(scindex++)+ ".png");
			javax.imageio.ImageIO.write(image.getSubimage(0, 0, App.WIDTH, App.HEIGHT), "PNG", out);
		} catch (Exception e) {
			System.err.println("Unable to write screenshot to " + path);
		}
		app.suspendRendering(false);
	}

	private void parseArgs(String[] args) {
		if (args.length > 0) {
			for (String arg : args) {
				if (arg.contains("=")) {
					String[] argSplit = arg.split("=");
					switch (argSplit[0].toLowerCase()) {
					case "w":
					case "width":
						WIDTH = Integer.parseInt(argSplit[1]);
						break;
					case "h":
					case "height":
						HEIGHT = Integer.parseInt(argSplit[1]);
						break;
					case "s":
					case "scale":
						SCALE = Float.parseFloat(argSplit[1]);
						break;
					case "d":
					case "debug":
						debug = (Integer.parseInt(argSplit[1]) < 5 && Integer.parseInt(argSplit[1]) >= 0
								? Integer.parseInt(argSplit[1])
								: 0);
						break;
					case "f":
					case "fps":
						setFPS(Integer.parseInt(argSplit[1]));
						break;

					case "k":
					case "fullscreen":
						fullScreen = (argSplit[1].toLowerCase().equals("on") ? true : false);
						if (win != null) {
							win.switchFullScreen(fullScreen);
						}
						break;
					default:
						break;
					}
				}
			}
		}
	}

	private void setFPS(long fps) {
		FPS = fps;
		timeFrame = (long) (1000 / FPS);
	}

	public List<GameObject> getObjects(){
		// List<Value> values = map.values().stream().collect(Collectors.toList());
		return objects.values().stream().collect(Collectors.toList());
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
	 * Set Pause mode.
	 * @param b
	 */
	public void setPause(boolean b) {
		this.pause = b;
	}

	/**
	 * Set rendering pause mode.
	 * @param b
	 */
	public void suspendRendering(boolean b) {
		this.pauseRendering=b;
	}

	/**
	 * App execution EntryPoint.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		App app = new App("MyApp", args);
		app.run();
	}


}
