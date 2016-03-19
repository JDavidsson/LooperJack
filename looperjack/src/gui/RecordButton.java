package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import lj.LooperJackControl;
import lj.LooperJackException;

//TODO add image to button.. maybe somthing like below

/*
 JButton button = new JButton();
 try {
 Image img = ImageIO.read(getClass().getResource("resources/picture.jpg"));
 button.setIcon(new ImageIcon(img));
 } catch (IOException ex) {
 }
 */

public class RecordButton extends JButton implements Observer {

	private boolean recording;
	private LooperJackControl c;

	public RecordButton(LooperJackControl c) {

		super("Record");
		recording = false;

		this.c = c;
		c.addObserver(this);

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!recording) {
					System.out.println("start-recordbutton-push-time: "
							+ System.currentTimeMillis());
					try {
						c.startRecording();
					} catch (LooperJackException l) {
						System.err.println(l.getMessage());
						JOptionPane.showMessageDialog(getMe(), l.getMessage(),
								"Warning", JOptionPane.PLAIN_MESSAGE);
					}
				} else {
					System.out.println("stop-recordbutton-push-time: "
							+ System.currentTimeMillis());
					c.stopRecording();
				}

			}

		});
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg.equals(c.RECORD_BUTTON_PRESSED)) {
			recording = !recording;
			this.setText(updateText());
		}
	}

	private String updateText() {

		if (recording) {
			return "Stop recording";
		} else {
			return "Record";
		}

	}

	private JButton getMe() {
		return this;
	}
}
