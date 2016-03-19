package gui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpinnerEnd extends JSpinner {

	private TrimButton trimButton;

	public SpinnerEnd(TrimButton trimButton) {

		this.trimButton = trimButton;

		JSpinner end = new JSpinner(new SpinnerNumberModel());
		JComponent fieldEnd = ((JSpinner.DefaultEditor) this.getEditor());
		Dimension prefSizeEnd = fieldEnd.getPreferredSize();
		prefSizeEnd = new Dimension(80, prefSizeEnd.height);
		fieldEnd.setPreferredSize(prefSizeEnd);

		this.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("You changed in the SpinnerEnd");
				trimButton.setEnd((int) getModel().getValue());
			}

		});

	};

}
