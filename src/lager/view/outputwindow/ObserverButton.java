package lager.view.outputwindow;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import lager.model.Warehouse;

/**
 * Erweiterung eines JButtons zu einem ObserverButtons
 */
@SuppressWarnings("serial")
public class ObserverButton extends JButton implements Observer {

	private JTextField input;
	private JComboBox<Object> box;

	public ObserverButton(String title, JTextField input, JComboBox<Object> box) {
		super(title);
		this.input = input;
		this.box = box;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Warehouse w = (Warehouse) box.getSelectedItem();
		Integer i = null;

		if (input.getText().matches("[1-9]\\d*")) {
			// Wenn eine valide Zahl eingegeben wird, wird diese als Auslieferungsumfang
			// gesetzt
			i = Integer.valueOf(input.getText());
		}

		/*
		 * Eine Auslieferung kann nur gebucht werden, wenn ein Umfang angegeben wurde
		 * und dieser im Lager vorhanden ist
		 */
		if (i == null) {
			setEnabled(false);
		} else if (w.getStock() < i) {
			setEnabled(false);
		} else {
			setEnabled(true);
		}
	}

}
