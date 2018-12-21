package lager.view.deliverywindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;

/**
 * Implementierung eines ComboBoxListener und Erweiterung eines Observables
 */
public class ObservableComboBoxListener extends Observable implements ActionListener {

	private JComboBox<Object> box;

	public ObservableComboBoxListener(JComboBox<Object> box) {
		super();
		this.box = box;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setChanged();
		notifyObservers(box.getSelectedItem());
	}

	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
		setChanged();
		notifyObservers(box.getSelectedItem());
	}

}
