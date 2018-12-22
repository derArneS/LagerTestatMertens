package lager.view.outputwindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Implementierung eines ComboBoxListener und Erweiterung eines Observables
 */
public class ObservableComboBoxListener extends Observable implements ActionListener {

	public ObservableComboBoxListener() {
		super();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setChanged();
		notifyObservers();
	}

	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
		setChanged();
		notifyObservers();
	}

}
