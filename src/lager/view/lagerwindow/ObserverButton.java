package lager.view.lagerwindow;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

/**
 * Erweiterung eines JButtons zu einem ObserverJButton
 */
@SuppressWarnings("serial")
public class ObserverButton extends JButton implements Observer {
	boolean name = false;
	boolean capacity = false;

	public ObserverButton(String title) {
		super(title);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (((ObservableKeyListener) o).getName().equals("NAME")) {
			/*
			 * Überprüfung ob ein Name eingegeben wurde
			 */
			if (((ObservableKeyListener) o).getTextField().getText().isEmpty()) {
				name = false;
			} else {
				name = true;
			}
		} else if (((ObservableKeyListener) o).getName().equals("CAPACITY")) {
			/*
			 * Überprüfung ob eine valide Kapazität der Buchung eingegeben wurde
			 */
			if (((ObservableKeyListener) o).getTextField().getText().isEmpty()) {
				capacity = false;
			} else {
				if (((ObservableKeyListener) o).getTextField().getText().matches("[1-9]\\d*")) {
					capacity = true;
				} else {
					capacity = false;
				}
			}
		}

		/*
		 * Aktivierung des Buttons wenn sowohl ein Name eingegeben, als auch eine valide Kapazität
		 * eingegeben wurde
		 */
		if (capacity && name) {
			this.setEnabled(true);
		} else {
			this.setEnabled(false);
		}

	}

}
