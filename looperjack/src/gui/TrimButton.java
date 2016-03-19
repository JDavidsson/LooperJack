package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import lj.LooperJackControl;
import lj.LooperJackException;

public class TrimButton extends JButton {

	private int msStart;
	private int msEnd;

	public TrimButton(LooperJackControl c) {
		super("Trim");

		msStart = 0;
		msEnd = 0;

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("Pushed the Trim-button");
				try {
					c.trimLoop(msStart, msEnd);
				} catch (LooperJackException l) {
					JOptionPane.showMessageDialog(getMe(), l.getMessage(),
							"Warning", JOptionPane.PLAIN_MESSAGE);
				}

			}

		});

	}

	public void setStart(int msStart) {
		this.msStart = msStart;

	}

	public void setEnd(int msEnd) {

		this.msEnd = msEnd;
	}

	private JButton getMe() {
		return this;
	}
}
