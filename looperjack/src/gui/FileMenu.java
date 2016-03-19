package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import lj.About;

public class FileMenu extends JMenu {

	public FileMenu() {
		super("File");
		setBackground(Color.LIGHT_GRAY);

		JMenuItem quit = new JMenuItem("Quit");

		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		JMenuItem about = new JMenuItem("About");

		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(getMe(), About.getText(),
						"Warning", JOptionPane.PLAIN_MESSAGE);
			}
		});
		add(about);
		add(quit);

	}

	private JMenu getMe() {
		return this;
	}

}
