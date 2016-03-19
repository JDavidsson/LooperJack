package lj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * This is a class that can record and playback sound from a given source. The
 * aim of it is to make a "one-button" program.
 * 
 */
public class LooperJackObsoleted extends Observable {

	public final static int HIGH = 3;
	public final static int PCM = 2;
	public final static int LOW = 1;

	private float sampleRate;
	private int sampleSizeInBits;
	private int channels; // 1 = mono, 2 = stereo
	private boolean signed;
	private boolean bigEndian;

	private String filename;

	private boolean recordToFile;
	private boolean recording;
	private ByteArrayOutputStream out;
	private int mixerNumber;
	private boolean loopOnOff;

	public LooperJackObsoleted() {

		sampleRate = 8000;
		sampleSizeInBits = 8;
		channels = 1;
		signed = true;
		bigEndian = true;
		recording = false;
		loopOnOff = false;
		mixerNumber = 0;

		recordToFile = false;

	}

	/**
	 * 
	 * Default sound quality is set to PCM_QUALITY
	 * */
	public LooperJackObsoleted(String filename) {
		super();
		recordToFile = true;
		this.filename = filename;

	}

	/**
	 * If recording is off this methods starts a recording. Otherwise it just
	 * stops the recording.
	 */
	public void pushRecord() {

		if (!recording) {
			recording = !recording;
			this.record();
		} else {
			recording = !recording;
		}

		this.setChanged();
		this.notifyObservers(1);
	}

	public void pushPlay() {

		if (!loopOnOff) {
			loopOnOff = !loopOnOff;
			this.play();
		} else {
			loopOnOff = !loopOnOff;
		}

		this.setChanged();
		this.notifyObservers(2);

	}

	/** */
	public void setQuality(int quality) {

		if (quality == LOW) {

			sampleRate = 4000;
			sampleSizeInBits = 4;
			channels = 1; // 1 = mono
			signed = true;
			bigEndian = true;

		}
		if (quality == PCM) {

			sampleRate = 8000;
			sampleSizeInBits = 8;
			channels = 1; // 1 = mono
			signed = true;
			bigEndian = true;

		}
		if (quality == HIGH) {

			sampleRate = 48000;
			sampleSizeInBits = 16;
			channels = 1; // 1 = mono
			signed = true;
			bigEndian = true;
		}
	}

	public void printAvailableMixers() {

		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		for (int i = 0; i < mixerInfo.length; i++) {
			System.out.println(i + " -> " + mixerInfo[i].getName());
		}
		System.out
				.println("NOTE: These are ALL the mixer-ports and you should probably choose an option with Soundflower..");
	}

	/** */
	public void play() {

		byte audio[] = null;
		if (!recordToFile) { // This is the default (empty constructor), reading
								// from the output stream
			audio = out.toByteArray();
		} else {
			// This is for reading a file to create bytestream to read from Path
			Path path = Paths.get(filename);
			try {
				audio = Files.readAllBytes(path);
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("Could not load file-path..");
			}
		}
		InputStream input = new ByteArrayInputStream(audio);

		AudioFormat audioFormat = getFormat();
		AudioInputStream audioInputStream = new AudioInputStream(input,
				audioFormat, audio.length / audioFormat.getFrameSize());

		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);

		try {
			SourceDataLine sLine = (SourceDataLine) AudioSystem.getLine(info);

			sLine.open(audioFormat);
			sLine.start();

			Runnable playRunner = new Runnable() {

				int bufferSize = (int) audioFormat.getSampleRate()
						* audioFormat.getFrameSize();
				byte buffer[] = new byte[bufferSize];

				@Override
				public void run() {
					System.out.println("Playing");

					try {
						int count = 0;
						int totalBytes = 0;

						// System.out.println("Marked support: " +
						// audioInputStream.markSupported());
						audioInputStream.mark(audioInputStream.available()); // mark

						while ((count != -1) && loopOnOff) {

							count = audioInputStream.read(buffer, 0,
									buffer.length);

							totalBytes += count;
							System.out.print("p");

							if (count > 0) {
								sLine.write(buffer, 0, count);
							}

							// moving back to begining of stream (the loop)
							if (count == -1) {
								System.out.print(">>");
								audioInputStream.reset(); // reset to marked
								count = 0;
							}

						}
						System.out.println("Total bytes = " + totalBytes);
						System.out.println(count);
						audioInputStream.close();
						sLine.drain();
						sLine.close();

					} catch (IOException e) {
						System.out
								.println("Something wrong with the output stream");
						e.printStackTrace();

					}
				}
			}; // Runnable stops here

			Thread playThread = new Thread(playRunner);
			playThread.start();

		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**

	 * 
	 * */
	public void record() {

		// sorting out which target line..
		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

		Mixer mixer = null;

		mixer = AudioSystem.getMixer(mixerInfo[mixerNumber]);

		// Line.Info[] lineInfo = mixer.getTargetLineInfo(); //remove when
		// certain that it's not used

		AudioFormat audioFormat = getFormat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class,
				audioFormat);

		try {

			TargetDataLine line = (TargetDataLine) mixer.getLine(info);

			// TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);

			line.open(audioFormat);
			line.start(); // line is now ready

			Runnable recordRunner = new Runnable() {

				int bufferSize = (int) audioFormat.getSampleRate()
						* audioFormat.getFrameSize();

				// the buffersize should effect the "exactness" of the
				// recording.
				byte buffer[] = new byte[bufferSize];

				@Override
				public void run() {

					out = new ByteArrayOutputStream();
					System.out.print("Recording");

					while (recording) {

						int count = line.read(buffer, 0, buffer.length);
						System.out.print("r");

						if (count > 0) {
							out.write(buffer, 0, count);
						}
					}
					try {
						if (recordToFile) {
							saveStreamToFile(out);
						}
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}; // Runnable stops here

			Thread recordThread = new Thread(recordRunner);
			recordThread.start();

		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** */
	private boolean saveStreamToFile(ByteArrayOutputStream out) {

		OutputStream outputStream = null;
		try {
			File file = new File(filename);
			outputStream = new FileOutputStream(file);
			out.writeTo(outputStream);
			return true;
		} catch (FileNotFoundException e1) {
			System.out.println("File not found");
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/** */
	private AudioFormat getFormat() {

		AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits,
				channels, signed, bigEndian);

		return audioFormat;
	}

	public void setMixer(int mixerNumber) {
		this.mixerNumber = mixerNumber;

	}

}
