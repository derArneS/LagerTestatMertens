package lager.view.deliverywindow;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

/**
 * Erweiterung eines JLabels zu einem ObserverJLabel
 */
@SuppressWarnings("serial")
public class ObserverLabel extends JLabel implements Observer {

	private int amount;
	private int part;

	public ObserverLabel(String text, int amount) {
		super(text);
		this.amount = amount;
	}

	@Override
	public void update(Observable o, Object part) {
		/*
		 * Aktualisierung des Textes auf den aktuellen Status
		 */
		this.part = (int) part;
		setText("Momentane Buchung: " + part + "   Noch zu verteilende Güter: " + amount);
	}

	public void setAmount(int newAmount) {
		amount = newAmount;
		setText("Momentane Buchung: " + part + "   Noch zu verteilende Güter: " + amount);
	}
}
