package lj;

import java.util.ArrayList;
import java.util.Observable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

/**
 * This class consists of methods for controlling the main features of
 * LooperJack e.g. recording, playing, choosing inputs and trimming loop.
 * LooperJackControl works as a 'model' and can be interfaced with.
 * 
 * @author Johan Davidsson
 * @version 1.0
 */
public class LooperJackControl extends Observable {

	public final static int HIGH = 3;
	public final static int PCM = 2;
	public final static int LOW = 1;

	public final static int RECORD_BUTTON_PRESSED = 4;
	public final static int PLAY_BUTTON_PRESSED = 5;

	AudioFormat audioFormat;

	private LJRecord r;
	private LJPlayback p;

	private int mixerNumber;
	private int bytesPerMilliSecond;

	private String filename;
	private Thread recordThread = new Thread(r);
	private Thread playThread = new Thread(p);

	private int msStart;
	private int msEnd;

	/**
	 * 
	 * @param quality
	 * @param filename
	 */
	public LooperJackControl(int quality, String filename) {

		msStart = 0;
		msEnd = 0;

		setQuality(quality);
		r = null;
		p = null;

		recordThread = new Thread(r);
		playThread = new Thread(p);

		printAvailableMixers();
	}

	/**
	 * @throws LooperJackException
	 * 
	 */
	public void startRecording() throws LooperJackException {
		if (p == null) {

			try {
				r = new LJRecord(getFormat(), filename, mixerNumber);
			} catch (LineUnavailableException e) {
				throw new LooperJackException("Unable to use mixer");
				// for further info on fault use e.printStackTrace();
			} catch (IllegalArgumentException e2) {
				throw new LooperJackException(
						"Unable to use chosen mixer, please select another one.");
			} catch (SecurityException e3) {
				throw new LooperJackException(
						"Security restrictions causes the line associated with the chosen mixer to not be available.");
			}
			recordThread = new Thread(r);
			recordThread.start();

			setChanged();
			notifyObservers(RECORD_BUTTON_PRESSED);

		} else {
			System.out.println("Recording thread is still alive.");
		}
	}

	/**
	 * 
	 */
	public void stopRecording() {

		if (r.stopRecording()) {
			setChanged();
			notifyObservers(RECORD_BUTTON_PRESSED);
		}
	}

	/**
	 * @throws LooperJackException
	 * 
	 */
	public void startPlaying() throws LooperJackException {

		if (!playThread.isAlive()) {

			if (r == null || r.getStream() == null) {
				throw new LooperJackException("Nothing has been recorded.");

			} else if (r.isRecording()) {

				throw new LooperJackException("Stop recording before playing.");

			}

			else if (msStart == 0 && msEnd == 0) {

				p = new LJPlayback(r.getStream(), getFormat(), filename); // no
																		// triming
																		// done
			} else if (msStart != 0 || msEnd != 0) {

				p = new LJPlayback(r.getStream(), getFormat(), filename, msStart,
						msEnd, bytesPerMilliSecond);// triming made
			} else {
				System.out.println("You really shouldn't end up in here.... ");
			}
			playThread = new Thread(p);
			playThread.start();

			setChanged();
			notifyObservers(PLAY_BUTTON_PRESSED);
		} else {
			System.out.println("Play thread is still alive.");
		}

	}

	public void stopPlaying() throws LooperJackException {
		if (isPlaying()) {
			p.stopPlaying();
			setChanged();
			notifyObservers(PLAY_BUTTON_PRESSED);
			p = null;
			playThread = new Thread(p);
		} else {
			throw new LooperJackException("Playing is already on..");
		}
	}

	/**
	 * 
	 * @param msStart
	 * @param msEnd
	 * @throws LooperJackException
	 */
	public void trimLoop(int msStart, int msEnd) throws LooperJackException {
		this.msStart = msStart;
		this.msEnd = msEnd;
		if (isPlaying()) {
			stopPlaying();
			startPlaying();
		} else {
			startPlaying();
		}
	}

	/**
	 * 
	 * @param mixerNumber
	 * @throws LooperJackException
	 */
	public void setMixer(int mixerNumber) throws LooperJackException {
		this.mixerNumber = mixerNumber;
		try {
			r = new LJRecord(getFormat(), filename, mixerNumber);
		} catch (LineUnavailableException e1) {
			throw new LooperJackException(
					"The line associated with chosen mixer is not available.");
		} catch (IllegalArgumentException e2) {
			throw new LooperJackException(
					"Unable to use chosen mixer, please select another one.");
		} catch (SecurityException e3) {
			throw new LooperJackException(
					"Security restrictions causes the line associated with the chosen mixer to not be available.");
		}
	}

	/**
	 * 
	 */
	public void printAvailableMixers() {

		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		for (int i = 0; i < mixerInfo.length; i++) {
			System.out.println(i + " -> " + mixerInfo[i].getName());
		}
	}

	public ArrayList<String> getAvailableMixers() {

		ArrayList<String> mixerList = new ArrayList<String>();

		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		for (int i = 0; i < mixerInfo.length; i++) {
			mixerList.add(i + " -> " + mixerInfo[i].getName());
		}

		return mixerList;
	}

	/**
	 * 
	 * @param quality
	 */
	public void setQuality(int quality) {

		float sampleRate = 48000;
		int sampleSizeInBits = 16;
		int channels = 1; // 1 = mono, 2 = stereo
		boolean signed = true;
		boolean bigEndian = true;

		if (quality == LOW) {

			sampleRate = 4000;
			sampleSizeInBits = 4;
			channels = 1; // 1 = mono
			signed = true;
			bigEndian = true;
			bytesPerMilliSecond = (int) ((sampleRate * sampleSizeInBits) / 8);
			System.out.println("Setting recording quality to LOW");

		}
		if (quality == PCM) {

			sampleRate = 8000;
			sampleSizeInBits = 8;
			channels = 1; // 1 = mono
			signed = true;
			bigEndian = true;
			System.out.println("Setting recording quality to PCM");

		}
		// default
		if (quality == HIGH) {

			sampleRate = 48000;
			sampleSizeInBits = 16;
			channels = 1; // 1 = mono
			signed = true;
			bigEndian = true;
			System.out.println("Setting recording quality to HIGH");
		}
		audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels,
				signed, bigEndian);

		bytesPerMilliSecond = (int) ((sampleRate * sampleSizeInBits) / 8) / 1000;

		System.out.println("B/ms: " + bytesPerMilliSecond);

	}

	public boolean isPlaying() {

		return p.isPlaying();

	}

	/**
	 * 
	 * @return
	 */
	private AudioFormat getFormat() {

		return audioFormat;
	}

}
