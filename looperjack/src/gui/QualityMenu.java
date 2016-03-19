package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import lj.LooperJackControl;

public class QualityMenu extends JMenu {

	public QualityMenu(LooperJackControl c) {

		super("Quality");
		setBackground(Color.LIGHT_GRAY);
		JMenuItem pcmQ = new JMenuItem("PCM");
		pcmQ.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				c.setQuality(c.PCM);

			}

		});
		JMenuItem highQ = new JMenuItem("High (default)");
		highQ.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				c.setQuality(c.HIGH);

			}

		});

		add(highQ);
		add(pcmQ);

	}

}
