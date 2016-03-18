package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import lj.LooperJackControl;
import lj.PlayButton;



/**
 * Builds a GUI for LooperJack 
 * 
 * @author Johan Davidsson
 * @version 1.0
 */
public class GUI extends JFrame {

	public GUI(LooperJackControl c) {
		this.initialize(c);
	}

	private void initialize(LooperJackControl c) {

		setFont(new Font("Serif", Font.PLAIN, 22));
		setBounds(100, 100, 400, 340);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Menu building

		buildMenu(c);

		JPanel northPanel = new JPanel();
		getContentPane().add(northPanel, BorderLayout.NORTH);

		northPanel.add(buildMixerPanel(c));

		JPanel centerPanel = new JPanel();
		getContentPane().add(centerPanel, BorderLayout.CENTER);

		centerPanel.add(buildRecordPanel(c));

		JPanel southPanel = new JPanel();
		getContentPane().add(southPanel, BorderLayout.SOUTH);

		southPanel.add(buildTrimPanel(c));

		this.setVisible(true);
	}

	private void buildMenu(LooperJackControl c) {
		JMenuBar menuBar = new JMenuBar();

		menuBar.setBackground(Color.LIGHT_GRAY);

		menuBar.add(new FileMenu());
		menuBar.add(new QualityMenu(c));

		setJMenuBar(menuBar);

	}

	private JPanel buildMixerPanel(LooperJackControl c) {

		JPanel mixerPanel = new JPanel();
		mixerPanel.setLayout(new BorderLayout());

		JLabel mixerLabel = new JLabel("Select an input to record from:");
		mixerPanel.add(mixerLabel, BorderLayout.NORTH);

		MixerList mixerList = new MixerList(c);
		JScrollPane mixerScrollPane = new JScrollPane(mixerList);
		mixerPanel.add(mixerScrollPane, BorderLayout.SOUTH);

		return mixerPanel;
	}

	private JPanel buildRecordPanel(LooperJackControl c) {
		JPanel recordPanel = new JPanel();
		recordPanel.setLayout(new BorderLayout());
		recordPanel.add(new RecordButton(c), BorderLayout.WEST);
		recordPanel.add(new PlayButton(c), BorderLayout.EAST);
		return recordPanel;
	}

	private JPanel buildTrimPanel(LooperJackControl c) {

		JPanel trimPanel = new JPanel();
		trimPanel.setLayout(new BorderLayout());

		JPanel northTrimPanel = new JPanel();
		trimPanel.add(northTrimPanel, BorderLayout.NORTH);

		JLabel trimPanelLabel = new JLabel(
				"Trim start/end of loop in milliseconds:");
		northTrimPanel.add(trimPanelLabel);

		JPanel southTrimPanel = new JPanel();
		trimPanel.add(southTrimPanel, BorderLayout.WEST);

		JPanel startTrimPanel = new JPanel(new BorderLayout());
		southTrimPanel.add(startTrimPanel, BorderLayout.WEST);

		JLabel startLabel = new JLabel("Start");
		startTrimPanel.add(startLabel, BorderLayout.WEST);

		TrimButton trimButton = new TrimButton(c);

		startTrimPanel.add(new SpinnerStart(trimButton), BorderLayout.EAST);

		JPanel endTrimPanel = new JPanel();
		southTrimPanel.add(endTrimPanel, BorderLayout.CENTER);

		JLabel endLabel = new JLabel("End");
		endTrimPanel.add(endLabel, BorderLayout.WEST);

		endTrimPanel.add(new SpinnerEnd(trimButton), BorderLayout.EAST);

		southTrimPanel.add(trimButton, BorderLayout.EAST);

		return trimPanel;

	}
}
