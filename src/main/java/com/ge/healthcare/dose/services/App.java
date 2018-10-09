package com.ge.healthcare.dose.services;

import java.awt.BorderLayout;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * a Simple App.
 *
 */
public class App extends JPanel implements Runnable, KeyListener {

    private static final Logger logger = Logger.getLogger(App.class.getCanonicalName());

    /**
     * Internal object amanged by the {@link App}.
     */
    public class GameObject {

        public int id;
        public String name;

        public int layer = 0;
        public int property = 0;

        public int x = 0, y = 0, width = 16, height = 16;

        BufferedImage image;
        public Color color;

        /**
         * Create a new Gobject.
         * 
         * @param name
         */
        public GameObject(String name) {
            this.name = name;
        }

        /**
         * Create a new GObject woth parameters !
         * 
         * @param name
         * @param x
         * @param y
         * @param image
         */
        public GameObject(String name, int x, int y, BufferedImage image) {
            this(name);
            this.x = x;
            this.y = y;
        }

        /**
         * compute object mechanism.
         * 
         * @param dt
         */
        public void update(long dt) {

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
                g.drawRect(x, y, width, height);
            }
        }

        /**
         * Builder pattern for GObject.
         * 
         * @param name
         * @return
         */
        public GameObject builder(String name) {
            return new GameObject(name);
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

    }

    private static final long serialVersionUID = 2924281870738631982L;

    public static int WIDTH = 320;
    public static int HEIGHT = 240;
    public static int SCALE = 2;

    /**
     * title of the application;
     */
    private String title = "NONAME";

    /**
     * internal tread.
     */
    private Thread thread = null;

    /**
     * internal flags.
     */
    private boolean exit = false;
    private boolean pause = false;
    private int debug = 0;
    private Font dbgFont;

    /**
     * Rendering pipeline
     */
    private BufferedImage buffer;
    private Graphics2D g;
    private Rectangle viewport;

    private long FPS = 60;
    private long timeFrame = (long) (1000 / FPS);
    private long realFPS = 0;

    /**
     * Translated Messages
     */
    private ResourceBundle msg = ResourceBundle.getBundle("messages");

    /**
     * Key processing arrays.
     */
    private boolean[] keys = new boolean[65536];
    private boolean[] prevKeys = new boolean[65536];
    private Queue<KeyEvent> keyQueue = new ConcurrentLinkedQueue<KeyEvent>();

    /**
     * GameObject list managed by the game.
     */
    private Map<String, GameObject> objects = new ConcurrentHashMap<String, App.GameObject>(50);
    /**
     * List of object to be rendered.
     */
    private List<GameObject> renderingList = new ArrayList<GameObject>();

    /**
     * Craet a new App with <code>title</code> as main title.
     * 
     * @param title the title of this app.
     */
    public App(String title) {
        super();
        this.title = title;
        this.thread = new Thread(this);
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
        viewport = new Rectangle(WIDTH, HEIGHT);

        dbgFont = g.getFont().deriveFont(9.0f);
        this.addKeyListener(this);

        GameObject player = new GameObject("player");
        player.setSize(24, 24).setPosition(WIDTH / 2, HEIGHT / 2);
        add(player);

    }

    /**
     * The execution entry point for this Runnable thread.
     */
    public void run() {
        long elapsed = 0;
        long current = System.currentTimeMillis();
        long previous = current;

        long cumulation = 0, frames = 0;
        initialize();

        while (!exit) {
            current = System.currentTimeMillis();
            if (!pause) {
                update(elapsed);
            }
            clearRenderBuffer(g);
            render(g);
            drawToScreen();
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
    }

    /**
     * Update the game mechanism.
     * 
     * @param elapsed
     */
    private void update(long elapsed) {
        for (Entry<String, GameObject> entry : objects.entrySet()) {
            entry.getValue().update(elapsed);
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
    private void render(Graphics2D g) {

        // prepare pipeline antialiasing.
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // render anything game ?
        for (GameObject o : renderingList) {
            o.render(g);
        }

        // render pause status
        // TODO render the Pause status

        // render debug information
        drawDebugInformation(g);
    }

    /**
     * Draw debug information.
     * 
     * @param g
     */
    private void drawDebugInformation(Graphics2D g) {
        g.setFont(dbgFont);
        String debugString = String.format(
                "dbg:%s FPS:%d objs:%d rendered:%d", 
                (debug == 0 ? "off" : "" + debug),
                objects.size(), 
                renderingList.size(), 
                realFPS);
        // int dbgStringWidth = g.getFontMetrics().stringWidth(debugString);
        int dbgStringHeight = g.getFontMetrics().getHeight();
        g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.8f));
        g.fillRect(0, HEIGHT - 32, WIDTH, 32);
        g.setColor(Color.ORANGE);
        g.drawString(debugString, 4, HEIGHT - 32 + dbgStringHeight);
    }

    /**
     * Draw graphic Buffer to screen.
     */
    private void drawToScreen() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(buffer, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g2.dispose();
    }

    /**
     * Retrieve the label from messages.properties file correspondong to
     * <code>key</code> value.
     * 
     * @param key the key of the label from messages.properties.
     * @return the value corresponding to the key.
     */
    public String getLabel(String key) {
        assert (key != null);
        assert (!key.equals(""));
        assert (msg != null);
        return msg.getString(key);
    }

    /**
     * Process key pressed event.
     * 
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        prevKeys[e.getKeyCode()] = keys[e.getKeyCode()];
        keys[e.getKeyCode()] = true;

    }

    /**
     * process the key released event.
     * 
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        prevKeys[e.getKeyCode()] = keys[e.getKeyCode()];
        keys[e.getKeyCode()] = false;

        /**
         * process the exit request.
         */
        if (keys[KeyEvent.VK_ESCAPE]) {
            this.exit = !exit;
            System.out.println("Request exiting");
        }
        /**
         * process the pause request.
         */
        if (keys[KeyEvent.VK_PAUSE] || keys[KeyEvent.VK_P]) {
            this.pause = !pause;
            System.out.printf("Pause reuqest %b", this.pause);
        }
    }

    /**
     * process the key typed event.
     * 
     * @param e
     */
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
     * get the last event from keyevent queue.
     * 
     * @return KeyEvent
     */
    public KeyEvent getKey() {
        return keyQueue.poll();
    }

    /**
     * Add a GameObject to the game.
     * 
     * @param go
     */
    public void add(GameObject go) {
        objects.put(go.name, go);
        renderingList.add(go);
        Collections.sort(renderingList, new Comparator<GameObject>() {
            public int compare(GameObject o1, GameObject o2) {
                return (o1.layer < o2.layer ? -1 : (o1.property < o2.property ? -1 : 1));
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
    }

    /**
     * App execution EntryPoint.
     * 
     * @param args
     */
    public static void main(String[] args) {

        App app = new App("MyApp");
        Dimension dim = new Dimension(App.WIDTH * App.SCALE, App.HEIGHT * App.SCALE);

        JFrame frame = new JFrame(app.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setContentPane(app);
        frame.setSize(dim);
        frame.setPreferredSize(dim);
        frame.setMaximumSize(dim);
        frame.setMinimumSize(dim);
        frame.setResizable(false);
        frame.addKeyListener(app);
        frame.pack();

        frame.setVisible(true);

        app.run();
    }

}
