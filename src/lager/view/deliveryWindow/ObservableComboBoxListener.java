package lager.view.deliveryWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;

public class ObservableComboBoxListener extends Observable implements ActionListener {

	private JComboBox<Object> box;

	public ObservableComboBoxListener(JComboBox<Object> box) {
		super();
		this.box = box;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.setChanged();
		this.notifyObservers(box.getSelectedItem());
	}

	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
		super.setChanged();
		super.notifyObservers(box.getSelectedItem());
	}

}
