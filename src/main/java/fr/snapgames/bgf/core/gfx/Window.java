/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.gfx;

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

import fr.snapgames.bgf.core.Game;

public class Window {

    private static final Logger logger = LoggerFactory.getLogger(Window.class);

    private BufferedImage icon;

    private JFrame frame;
    private Dimension back;
    private boolean fullScreenMode;

    private Game app;

    private String iconpath = "res/app-icon.png";

    private GraphicsDevice device;

    /**
     * Create a window for the Game <code>app</code>.
     * 
     * @param app
     */
    public Window(Game app) {
        this(app, app.getTitle());
    }

    /**
     * Create a Window for the <code>app</code> with a <code>title</code>.
     * 
     * @param app
     * @param title
     */
    private Window(Game app, String title) {
        this.app = app;

        try {
            icon = ImageIO.read(this.getClass().getResourceAsStream("/" + iconpath));
        } catch (IOException e) {
            logger.error("unable to read icon file {}", iconpath);
        }
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        initialize(true, false);
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
        frame.addKeyListener(app.getInputListener());
        frame.pack();
        frame.validate();
        frame.setVisible(true);

        app.setBoundAs(frame);
        app.suspendRendering(false);
        app.setPause(false);

    }

    /**
     * Switch between full screen and windowed mode.
     * 
     * @param fullScreen the fullscreen flag state.
     */
    public void switchFullScreen(boolean fullScreen) {
        this.fullScreenMode = fullScreen;
        frame.setVisible(false);
        if (fullScreen) {
            back = frame.getSize();
            frame.dispose();
            initialize(false, false);
            device.setFullScreenWindow(frame);
        } else {
            frame.dispose();
            frame.setSize(back);
            /*app.setSize(back);*/
            initialize(true, false);
            device.setFullScreenWindow(null);
            /*app.getRender().setScale(backScale);*/
        }

        frame.setVisible(true);
    }

    /**
     * Set Window Size according the {@link Game} object.
     * 
     * @param app the apret app to align size with.
     */
    public void setSize(Game app) {
        // fix a platform linked issue about window sizing.
        Insets insets = frame.getInsets();
        int addedWidth = insets.left + insets.right;
        int addedHeight = insets.top + insets.bottom;

        final int fWidth = app.getRender().getDisplayWidth() + addedWidth;
        final int fHeight = app.getRender().getDisplayHeight() + addedHeight;

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

    /**
     * @return the fullScreenMode
     */
    public boolean isFullScreenMode() {
        return fullScreenMode;
    }

    /**
     * @param fullScreenMode the fullScreenMode to set
     */
    public void setFullScreenMode(boolean fullScreenMode) {
        this.fullScreenMode = fullScreenMode;
    }

}