package main;

import lj.LooperJackControl;
import gui.GUI;

public class LooperJackMain {

	public static void main(String[] args) {

		new GUI( new LooperJackControl(LooperJackControl.HIGH,
				null));
	}
}
