/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class InputListener is the main input maager for the game. Here are
 * processed all the iput events to inject those envents into the game.
 */
public class InputListener implements KeyListener {

    private static final Logger logger = LoggerFactory.getLogger(InputListener.class);


	/**
	 * This an enum listing all possible input actions
	 */
	public enum KeyBinding {
		UP, DOWN, LEFT, RIGHT, DELETE, PAUSE, QUIT, DEBUG, FIRE1, FIRE2, FIRE3, FIRE4, FN1, FN2, FN3, SCREENSHOT, RESET,
		FULLSCREEN;
	}

    private App app;

	/**
	 * Map for keybinding.
	 */
	private Map<KeyBinding, Integer> keyBinding = new HashMap<>();

	/**
	 * Key processing arrays.
	 */
	private boolean[] keys = new boolean[65536];
	private Queue<KeyEvent> keyQueue = new ConcurrentLinkedQueue<KeyEvent>();




    /**
     * This is the main >Contructor for the InputListener, initializing its parent
     * component, the <code>app</code>.
     * 
     * @param app
     */
    public InputListener(App app) {
        this.app = app;
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
				app.gsm.action(keyBind);
			}
		}
	}


	/**
	 * Bind all keys for the game.
	 */
	public void prepareKeyBinding(Map<KeyBinding,Integer> userKeyBindings) {
        this.keyBinding=userKeyBindings;
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

}