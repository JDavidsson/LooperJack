package lj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * 
 Plays a recorded sound and loops. It also allows for trimming the loop's
 * start/end.
 * 
 * @author Johan Davidsson
 * @version 1.0
 */
public class LJPlayback implements Runnable {

	private ByteArrayOutputStream out;

	private String filename;
	private AudioFormat audioFormat;
	private boolean playing;

	private AudioInputStream manipulatedAudioInputStream;

	private boolean loopManipulated;

	/**
	 * 
	 * @param out
	 * @param audioFormat
	 * @param filename
	 */
	public LJPlayback(ByteArrayOutputStream out, AudioFormat audioFormat,
			String filename) {

		loopManipulated = false;
		playing = true;
		this.out = out;
		this.filename = filename;
		this.audioFormat = audioFormat;

	}

	/**
	 * 
	 * @param out
	 * @param audioFormat
	 * @param filename
	 * @param msStart
	 * @param msEnd
	 * @param bytesPerMilliSecond
	 * @throws LooperJackException
	 */
	public LJPlayback(ByteArrayOutputStream out, AudioFormat audioFormat,
			String filename, int msStart, int msEnd, int bytesPerMilliSecond)
			throws LooperJackException {

		this(out, audioFormat, filename);
		this.trimLoop(msStart, msEnd, bytesPerMilliSecond);

	}

	/**
	 * 
	 */
	@Override
	public void run() {

		playing = true;

		byte audio[] = null;
		if (filename == null) { // This is the default (empty constructor),
								// reading
								// from the output stream

			audio = out.toByteArray();
		} else {
			// This is for reading a file to create bytestream to read from Path
			Path path = Paths.get(filename + "lj");
			try {
				audio = Files.readAllBytes(path);
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("Could not load file-path..");
			}
		}

		System.out.println("audio-byteArray length: " + audio.length);

		InputStream input = new ByteArrayInputStream(audio);

		AudioInputStream audioInputStream;

		audioInputStream = new AudioInputStream(input, audioFormat,
				audio.length / audioFormat.getFrameSize());

		if (loopManipulated) {
			audioInputStream = manipulatedAudioInputStream;
		}

		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);

		SourceDataLine sLine;
		try {
			sLine = (SourceDataLine) AudioSystem.getLine(info);
			sLine.open(audioFormat);
			sLine.start();

			int bufferSize = audioFormat.getFrameSize();

			byte buffer[] = new byte[bufferSize];

			System.out.println("Play");

			try {
				int count = 0;
				int totalBytes = 0;

				audioInputStream.mark(audioInputStream.available()); // mark

				while ((count != -1) && playing) {

					count = audioInputStream.read(buffer, 0, buffer.length);

					totalBytes += count;

					if (count > 0) {
						sLine.write(buffer, 0, count);
					}

					// moving back to begining of stream (the loop)
					if (count == -1) {
						System.out.print(">");
						audioInputStream.reset(); // reset to marked
						count = 0;

					}

				}
				System.out.println("Total amount of played bytes = "
						+ totalBytes);
				audioInputStream.close();
				sLine.drain();
				sLine.close();

			} catch (IOException e) {
				System.out.println("Something wrong with the output stream.");
				e.printStackTrace();

			}
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean stopPlaying() {
		if (playing) {
			playing = !playing;
			System.out.println("Stop");
			return true;
		} else {
			return false;
		}

	}

	public boolean isPlaying() {
		return playing;
	}

	/**
	 * As of now this method only shrinks the loop with specified times. It
	 * cannot enlarge it.
	 * 
	 * @param msStart
	 *            - milliseconds that will be trimmed at beginning of loop
	 * @param msEnd
	 *            - same as above but at the end of the loop
	 * @param bytesPerMilliSeconds
	 * @throws LooperJackException
	 */
	private void trimLoop(int msStart, int msEnd, int bytesPerMilliSeconds)
			throws LooperJackException {

		byte audio[] = out.toByteArray();

		int newByteArrayLength = audio.length
				- (msStart * bytesPerMilliSeconds)
				- (msEnd * bytesPerMilliSeconds);

		if (newByteArrayLength < 0) {
			System.err
					.println("Can't trim that much, try triming less instead..");
			// resets
			msStart = 0;
			msEnd = 0;
			newByteArrayLength = audio.length;
			throw new LooperJackException(
					"Can't trim that much, try triming less instead..");
		}

		System.out.println("Original length of loop (bytes) : " + audio.length);
		System.out.println("New length of loop (bytes): " + newByteArrayLength);

		// the new byte array
		byte bytes[] = new byte[newByteArrayLength];

		int newStart = msStart * bytesPerMilliSeconds;
		int newEnd = audio.length - (msEnd * bytesPerMilliSeconds);

		int k = newStart;
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = audio[k];
			k++;
		}

		InputStream input = new ByteArrayInputStream(bytes);
		AudioInputStream audioInputStream = new AudioInputStream(input,
				audioFormat, bytes.length / audioFormat.getFrameSize());

		manipulatedAudioInputStream = audioInputStream;
		loopManipulated = true;

	}

	public boolean somethingToPlay() {
		if (out == null) {
			return false;
		} else {
			return true;
		}
	}
}
