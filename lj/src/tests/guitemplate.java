package tests;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.border.EtchedBorder;

public class guitemplate {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					guitemplate window = new guitemplate();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public guitemplate() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setFont(new Font("Times", Font.PLAIN, 12));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel northPanel = new JPanel();
		frame.getContentPane().add(northPanel, BorderLayout.NORTH);

		JButton btnNewButton = new JButton("New button");
		northPanel.add(btnNewButton);
		btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton btnNewButton_1 = new JButton("New button");
		northPanel.add(btnNewButton_1);
		btnNewButton_1.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel centerPanel = new JPanel();
		centerPanel
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(centerPanel, BorderLayout.CENTER);

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(centerPanel, popupMenu);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

		JList list = new JList();
		centerPanel.add(list);

		JList list_1 = new JList();
		centerPanel.add(list_1);

		JPanel westPanel = new JPanel();
		frame.getContentPane().add(westPanel, BorderLayout.WEST);

		JPanel southPanel = new JPanel();
		frame.getContentPane().add(southPanel, BorderLayout.SOUTH);

		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setToolTipText("");
		southPanel.add(lblNewLabel);

		JSpinner spinner = new JSpinner();
		spinner.setToolTipText("");

		JComponent field = ((JSpinner.DefaultEditor) spinner.getEditor());
		Dimension prefSize = field.getPreferredSize();
		prefSize = new Dimension(80, prefSize.height);
		field.setPreferredSize(prefSize);

		southPanel.add(spinner);

		JLabel lblNewLabel_1 = new JLabel("New label");
		southPanel.add(lblNewLabel_1);

		JSpinner spinner_1 = new JSpinner();

		JComponent field1 = ((JSpinner.DefaultEditor) spinner_1.getEditor());
		Dimension prefSize1 = field.getPreferredSize();
		prefSize1 = new Dimension(80, prefSize.height);
		field1.setPreferredSize(prefSize);

		southPanel.add(spinner_1);

		JButton btnNewButton_2 = new JButton("Trim");
		southPanel.add(btnNewButton_2);

		JPanel eastPanel = new JPanel();
		frame.getContentPane().add(eastPanel, BorderLayout.EAST);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
