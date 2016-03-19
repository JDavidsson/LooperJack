package gui;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import lj.LooperJackControl;
import lj.LooperJackException;

public class MixerList extends JList implements ListSelectionListener {

	private LooperJackControl c;

	public MixerList(LooperJackControl c) {

		super(c.getAvailableMixers().toArray());
		this.c = c;

		this.addListSelectionListener(this);

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		// TODO do something when it's not possible to select the mixer

		try {
			c.setMixer(getSelectedIndex());
		} catch (LooperJackException l) {
			System.err.println(l.getMessage());
			// l.printStackTrace();

			JOptionPane.showMessageDialog(this, l.getMessage(), "Warning",
					JOptionPane.PLAIN_MESSAGE);

		}

	}

}
