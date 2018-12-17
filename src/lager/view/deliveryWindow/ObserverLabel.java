package lager.view.deliveryWindow;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ObserverLabel extends JLabel implements Observer {

	public ObserverLabel(String text) {
		super(text);
	}

	@Override
	public void update(Observable o, Object arg) {
		setText("Momentane Buchung: " + arg);
	}

}
