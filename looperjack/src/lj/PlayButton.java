package lj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class PlayButton extends JButton implements Observer {

	private boolean playing;
	private LooperJackControl c;

	public PlayButton(LooperJackControl c) {

		super("Play");
		playing = false;
		this.c = c;
		c.addObserver(this);

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!playing) {
					try {
						c.startPlaying();
					} catch (LooperJackException l) {
						JOptionPane.showMessageDialog(getMe(), l.getMessage(),
								"Warning", JOptionPane.PLAIN_MESSAGE);

					}
				} else {
					try {
						c.stopPlaying();
					} catch (LooperJackException l) {
						JOptionPane.showMessageDialog(getMe(), l.getMessage(),
								"Warning", JOptionPane.PLAIN_MESSAGE);
					}
				}

			}

		});
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg.equals(c.PLAY_BUTTON_PRESSED)) {
			playing = !playing;
			this.setText(updateText());
		}
	}

	private JButton getMe() {
		return this;
	}

	private String updateText() {

		if (playing) {
			return "Stop playing";
		} else {
			return "Play";
		}

	}
}
