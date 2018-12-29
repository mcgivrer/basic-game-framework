/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.audio;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is intend to manage and control Sound play and output.
 * 
 * @author Frédéric Delorme.
 *
 */
public class SoundControl {
	/**
	 * Internal logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SoundControl.class);

	/**
	 * Internal instance for the SoundControl system.
	 */
	private final static SoundControl instance = new SoundControl();

	/**
	 * Max number of SoundClip to be stored in cache.
	 */
	private static final int MAX_SOUNDS_IN_STACK = 40;

	/**
	 * Internal play Stack
	 */
	private Stack<String> soundsStack = new Stack<String>();
	/**
	 * Internal SoundBank.
	 */
	private Map<String, SoundClip> soundBank = new ConcurrentHashMap<String, SoundClip>();

	private boolean mute = true;

	/**
	 * Internal constructor.
	 */
	private SoundControl() {
		soundsStack.setSize(MAX_SOUNDS_IN_STACK);
		logger.info("Initialize SoundControl with {} stack places", MAX_SOUNDS_IN_STACK);
	}

	/**
	 * Load a Sound from <code>filename</code> to the sound bank.
	 * 
	 * @param filename file name of the sound to be loaded to the
	 *                 <code>soundBank</code>.
	 * @return filename if file has been loaded into the sound bank or null.
	 */
	public String load(String code, String filename) {
		if (!soundBank.containsKey(code)) {
			SoundClip sc = new SoundClip(filename);
			if (sc != null) {
				soundBank.put(code, sc);
				logger.debug("Load sound {} to sound bank with code {}", filename, code);
			}
			return filename;
		} else {
			return null;
		}
	}

	/**
	 * play the sound with <code>code</code>
	 * 
	 * @param code internal code of the sound to be played.
	 */
	public void play(String code) {
		if (!mute) {
			if (soundBank.containsKey(code)) {
				SoundClip sc = soundBank.get(code);
				sc.play();
				logger.debug("Play sound {}", code);
			} else {
				logger.error("unable to find the sound {} in the SoundBank !", code);
			}
		} else {
			logger.debug("Mute mode activated, {} not played", code);
		}
	}

	/**
	 * play the sound with <code>code</code>
	 * 
	 * @param code   internal code of the sound to be played.
	 * @param volume volume level to be played.
	 */
	public void play(String code, float volume) {
		if (!mute) {
			if (soundBank.containsKey(code)) {
				SoundClip sc = soundBank.get(code);
				sc.play(0.5f, volume);
				logger.debug("Play sound {} with volume {}", code, volume);
			} else {
				logger.error("unable to find the sound {} in the SoundBank !", code);
			}
		} else {
			logger.debug("Mute mode activated, {} not played", code);
		}
	}

	/**
	 * play the sound with <code>code</code>
	 * 
	 * @param code   internal code of the sound to be played.
	 * @param volume volume level to be played.
	 * @param pan    the pan for the sound to be played.
	 */

	public void play(String code, float volume, float pan) {
		if (!mute) {

			if (soundBank.containsKey(code)) {
				SoundClip sc = soundBank.get(code);
				sc.play(0.5f, volume);
				logger.debug("Play sound {} with volume {} and pan {}", code, volume, pan);
			} else {
				logger.error("unable to find the sound {} in the SoundBank !", code);
			}
		} else {
			logger.debug("Mute mode activated, {} not played", code);
		}
	}

	/**
	 * Is the sound code playing right now ?
	 * 
	 * @param code code of the sound to test.
	 * @return
	 */
	public boolean isPlaying(String code) {
		if (soundBank.containsKey(code)) {
			return soundBank.get(code).isPlaying();
		} else {
			return false;
		}
	}

	/**
	 * The unique sound control instance.
	 * 
	 * @return
	 */
	public static SoundControl getInstance() {
		return instance;
	}

	public boolean isMute() {
		return mute;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}
}