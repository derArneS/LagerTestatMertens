package lager.view.deliverywindow;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

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
		this.part = (int) part;
		setText("Momentane Buchung: " + part + "   Noch zu verteilende G�ter: " + amount);
	}

	public void setAmount(int newAmount) {
		amount = newAmount;
		setText("Momentane Buchung: " + part + "   Noch zu verteilende G�ter: " + amount);
	}
}
