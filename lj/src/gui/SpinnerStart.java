package gui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpinnerStart extends JSpinner {

	private TrimButton trimButton;

	public SpinnerStart() {

		JSpinner start = new JSpinner(new SpinnerNumberModel());
		JComponent fieldStart = ((JSpinner.DefaultEditor) this.getEditor());
		Dimension prefSizeStart = fieldStart.getPreferredSize();
		prefSizeStart = new Dimension(80, prefSizeStart.height);
		fieldStart.setPreferredSize(prefSizeStart);

		System.out.println(getModel().getValue());

		this.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("You changed in the SpinnerEnd");
				trimButton.setStart((int) getModel().getValue());
			}

		});

	};

	public SpinnerStart(TrimButton trimButton) {
		this();
		this.trimButton = trimButton;

	}
}
