/**
 * SnapGames
 * 
 * @since 2018
 * @see https://github.com//SnapGames/basic-game-framework/wiki
 */
package fr.snapgames.bgf.core.audio;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to play and manage a sound clip from file.
 * 
 * @author Frédéric Delorme
 *
 */
public class SoundClip {

	private static final Logger logger = LoggerFactory.getLogger(SoundClip.class);
	/**
	 * Java Sound clip to be read.
	 */
	private Clip clip;
	/**
	 * Volume control.
	 */
	private FloatControl gainControl;
	/**
	 * Pan Control.
	 */
	private FloatControl panControl;
	/**
	 * Balance Control.
	 */
	private FloatControl balanceControl;

	/**
	 * Initialize the sound clip ready to play from the file at <code>path</code>.
	 * 
	 * @param path Path to the sound clip to be read.
	 */
	public SoundClip(String path) {
		try {
			InputStream audioSrc = SoundClip.class.getResourceAsStream("/" + path);
			if (audioSrc == null) {
				logger.error("unable to read the sound file {}", path);

			} else {
				InputStream bufferedIn = new BufferedInputStream(audioSrc);
				AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
				AudioFormat baseFormat = ais.getFormat();

				AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
						16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
				AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
				clip = AudioSystem.getClip();
				clip.open(dais);

				if (clip.isControlSupported(FloatControl.Type.BALANCE)) {
					balanceControl = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
				} else {
					logger.debug("BALANCE control is not supported");
				}

				if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
					gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				} else {
					logger.debug("MASTER_GAIN control is not supported");
				}
				if (clip.isControlSupported(FloatControl.Type.PAN)) {
					panControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
				} else {
					logger.debug("PAN control is not supported");
				}
			}
		} catch (Exception e) {
			logger.error("unable to play the sound file {}", path, e);
		}

	}

	/**
	 * Start playing the clip.
	 */
	public void play() {
		if (clip == null) {
			return;
		} else {
			stop();
			clip.setFramePosition(0);
			while (!clip.isRunning()) {
				clip.start();
			}
		}

	}

	public void play(float pan, float volume) {
		if (clip == null) {
			return;
		} else {
			stop();
			clip.setFramePosition(0);
			while (!clip.isRunning()) {
				clip.start();
			}
		}
		setPan(pan);
		setVolume(volume);
	}

	public void play(float pan, float volume, float balance) {
		if (clip == null) {
			return;
		} else {
			stop();
			clip.setFramePosition(0);
			while (!clip.isRunning()) {
				clip.start();
			}
		}
		setPan(pan);
		setBalance(balance);
		setVolume(volume);
	}

	/**
	 * Set balance for this sound clip.
	 * 
	 * @param balance
	 */
	private void setBalance(float balance) {
		balanceControl.setValue(balance);
	}

	/**
	 * Set Panning for this sound clip.
	 * 
	 * @param pan
	 */
	public void setPan(float pan) {
		panControl.setValue(pan);
	}

	/**
	 * Set Volume for this sound clip.
	 * 
	 * @param volume
	 */
	public void setVolume(float volume) {
		float min = gainControl.getMinimum() / 4;
		if (volume != 1) {
			gainControl.setValue(min * (1 - volume));
		}
	}

	/**
	 * Stop playing the clip.
	 */
	public void stop() {
		if (clip == null) {
			return;
		} else if (clip.isRunning()) {
			clip.stop();
		}
	}

	/**
	 * Loop the clip continuously
	 */
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		while (!clip.isRunning()) {
			clip.start();
		}
	}

	/**
	 * Close the clip.
	 */
	public void close() {
		stop();
		clip.drain();
		clip.close();
	}

	/**
	 * is the clip is playing ?
	 * 
	 * @return
	 */
	public boolean isPlaying() {
		return clip.isRunning();
	}

}
