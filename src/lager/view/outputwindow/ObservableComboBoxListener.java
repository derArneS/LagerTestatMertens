package lager.view.outputwindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;

public class ObservableComboBoxListener extends Observable implements ActionListener {

	private JComboBox<Object> box;

	public ObservableComboBoxListener(JComboBox<Object> box) {
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
