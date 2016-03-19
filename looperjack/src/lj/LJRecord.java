package lj;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

/**
 * 
 * This class records from a given input (DataLine) and saves to
 * ButeArrayOutputStream and alternatively saves to a file.
 * 
 * @author Johan Davidsson
 * @version 1.0
 */
public class LJRecord implements Runnable {

	private int bufferSize;
	private byte buffer[];
	private ByteArrayOutputStream out;
	private TargetDataLine line;
	private boolean recording;
	private String filename;

	/**
	 * 
	 * @param audioFormat
	 * @param filename
	 * @param mixerNumber
	 * @throws LineUnavailableException
	 */
	public LJRecord(AudioFormat audioFormat, String filename, int mixerNumber)
			throws LineUnavailableException, IllegalArgumentException,
			SecurityException {

		this.filename = filename;
		recording = true;

		// sorting out which target line..
		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

		Mixer mixer = null;

		mixer = AudioSystem.getMixer(mixerInfo[mixerNumber]); // selects the
																// mixer

		DataLine.Info info = new DataLine.Info(TargetDataLine.class,
				audioFormat);

		line = (TargetDataLine) mixer.getLine(info);
		line.open(audioFormat);

		line.start(); // line is now ready

		// bufferSize = (int) audioFormat.getSampleRate() *
		// audioFormat.getFrameSize();

		bufferSize = audioFormat.getFrameSize();
		buffer = new byte[bufferSize];

	}

	/**
	 * 
	 * @param out
	 * @return
	 */
	private boolean saveStreamToFile(ByteArrayOutputStream out) {

		OutputStream outputStream = null;
		try {
			File file = new File(filename + ".lj");
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

	/**
	 * 
	 */
	@Override
	public void run() {

		System.out.println("Run-method-time: " + System.currentTimeMillis());
		out = new ByteArrayOutputStream();
		System.out.println("Recording");

		int totalBytes = 0;

		while (recording) {

			int count = line.read(buffer, 0, buffer.length);

			totalBytes += count;

			if (count > 0) {
				out.write(buffer, 0, count);
			}

		}

		System.out
				.println("end-run-method-time: " + System.currentTimeMillis());
		if (filename != null) {
			saveStreamToFile(out);
		}
		System.out.println("Total amount of recorded bytes = " + totalBytes);

	}

	/**
	 * 
	 * @return
	 * @throws LooperJackException
	 */
	public ByteArrayOutputStream getStream() throws LooperJackException {

		return out;

	}

	/**
	 * 
	 * @return
	 */
	public boolean stopRecording() {

		if (recording) {
			recording = !recording;
			System.out.println("Recording stopped");
			return true;
		} else {
			System.out.println("Not recording in the first place.");
			return false;

		}
	}

	public boolean isRecording() {
		return recording;
	}

}
