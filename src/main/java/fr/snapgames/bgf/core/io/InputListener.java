/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.snapgames.bgf.core.Game;

/**
 * The class InputListener is the main input manager for the game. Here are
 * processed all the input events to inject those events into the game.
 */
public class InputListener implements KeyListener {

    private static final Logger logger = LoggerFactory.getLogger(InputListener.class);


	/**
	 * This an enum listing all possible input actions
	 */
	public enum KeyBinding {
		// direction
		UP, DOWN, LEFT, RIGHT,
		// basic operation
		DELETE, PAUSE, QUIT, DEBUG,
		// command actions
		FIRE1, FIRE2, FIRE3, FIRE4,
		// functions
		FN1, FN2, FN3,
		// tools
		SCREENSHOT, 
		RESET,
		FULLSCREEN;
	}

    private Game app;

	/**
	 * Map for key binding.
	 */
	private Map<KeyBinding, Integer> keyBinding = new HashMap<>();

	/**
	 * Key processing arrays.
	 */
	private boolean[] keys = new boolean[65536];
	private Queue<KeyEvent> keyQueue = new ConcurrentLinkedQueue<KeyEvent>();




    /**
     * This is the main constructor for the InputListener, initializing its parent
     * component, the <code>app</code>.
     * 
     * @param app
     */
    public InputListener(Game app) {
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

		for (Entry<KeyBinding,Integer> keyBind : keyBinding.entrySet()) {
			if (e.getKeyCode()==keyBind.getValue()) {
				app.getGSM().action(keyBind.getKey());
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