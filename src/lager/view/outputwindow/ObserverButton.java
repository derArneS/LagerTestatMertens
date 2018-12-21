package lager.view.outputwindow;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import lager.model.Warehouse;

public class ObserverButton extends JButton implements Observer {

	private JTextField input;
	private JComboBox box;

	public ObserverButton(String title, JTextField input, JComboBox box) {
		super(title);
		this.input = input;
		this.box = box;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Warehouse w = (Warehouse) box.getSelectedItem();
		Integer i = null;

		if (input.getText().matches("[1-9]\\d*")) {
			i = Integer.valueOf(input.getText());
		}

		if (i == null) {
			setEnabled(false);
		} else if (w.getStock() < i) {
			setEnabled(false);
		} else {
			setEnabled(true);
		}
	}

}
