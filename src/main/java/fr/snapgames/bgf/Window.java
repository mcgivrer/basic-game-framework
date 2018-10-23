package fr.snapgames.bgf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Window {

    private static final Logger logger = LoggerFactory.getLogger(Window.class);

    private BufferedImage icon;

    private JFrame frame;
    private Dimension back;

    private App app;

    private String iconpath = "res/app-icon.png";

    private float backScale;

    private static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

    /**
     * Create a window for the App <code>app</code>.
     * 
     * @param app
     */
    public Window(App app) {
        this(app, app.getTitle());
    }

    /**
     * Create a Window for the <code>app</code> with a <code>title</code>.
     * 
     * @param app
     * @param title
     */
    public Window(App app, String title) {
        this.app = app;

        try {
            icon = ImageIO.read(this.getClass().getResourceAsStream("/" + iconpath));
        } catch (IOException e) {
            logger.error("unable to read icon file {}", iconpath);
        }

        initialize(true,false);
    }

    /**
     * Initialize the frame for the window.
     * <ul>
     * <li><code>decoraton</code> will display menu and window border if true.</li>
     * <li><code>resizable</code> as false will set window as not resizable.</li>
     * </ul>
     * 
     * @param decoration flag to acrtovate window decoration.
     * @param resizable  flag to define window as a resizable one or not.
     */
    private void initialize(boolean decoration, boolean resizable) {
        app.setPause(true);
        app.suspendRendering(true);
        frame = new JFrame(app.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(app);
        frame.setLayout(new BorderLayout());
        frame.setUndecorated(!decoration);

        setSize(app);
        frame.setResizable(resizable);
        frame.setIconImage(icon);
        frame.addKeyListener(app);
        frame.pack();
        frame.validate();
        frame.setVisible(true);

        app.setFrame(frame);
        app.suspendRendering(false);
        app.setPause(false);

    }

    /**
     * Switch fullscreen.
     * 
     * @param fullScreen
     */
    public void switchFullScreen(boolean fullScreen) {
        frame.setVisible(false);
        if (fullScreen) {
            back = frame.getSize();
            backScale = App.SCALE;
            frame.dispose();
            initialize(false,false);
            device.setFullScreenWindow(frame);
            app.setSize(new Dimension(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight()));
        } else {
            frame.dispose();
            frame.setSize(back);
            app.setSize(back);
            initialize(true,false);
            device.setFullScreenWindow(null);
            App.SCALE = backScale;
        }

        frame.setVisible(true);
    }

    /**
     * Set Window Size according the {@link App} object.
     * 
     * @param app
     */
    public void setSize(App app) {
        // fix a platform linked issue about window sizing.
        Insets insets = frame.getInsets();
        int addedWidth = insets.left + insets.right;
        int addedHeight = insets.top + insets.bottom;

        final int fWidth = app.getDisplayWidth() + addedWidth;
        final int fHeight = app.getDisplayHeight() + addedHeight;

        Dimension dim = new Dimension(fWidth, fHeight);
        frame.setSize(dim);
        frame.setPreferredSize(dim);
        frame.setMaximumSize(dim);
        frame.setMinimumSize(dim);
    }

    /**
     * Return the dimension as {@link Rectangle} of the current window.
     * 
     * @return Rectangle
     */
    public Rectangle getDimension() {
        return frame.getBounds();
    }
}