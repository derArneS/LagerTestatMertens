package lager.view;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

public class ObserverButton extends JButton implements Observer {
	boolean name = false;
	boolean capacity = false;

	public ObserverButton(String title) {
		super(title);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (((ObservableKeyListener) o).getName().equals("NAME")
				|| ((ObservableKeyListener) o).getName().equals("CAPACITY")) {

			if (((ObservableKeyListener) o).getName().equals("NAME")) {
				if (((ObservableKeyListener) o).getTextField().getText().isEmpty()) {
					name = false;
				} else {
					name = true;
				}
			} else if (((ObservableKeyListener) o).getName().equals("CAPACITY")) {
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

			if (capacity && name) {
				this.setEnabled(true);
			} else {
				this.setEnabled(false);
			}
		} else if (((ObservableKeyListener) o).getName().equals("MENGE")) {
			if (((ObservableKeyListener) o).getTextField().getText().isEmpty()) {
				this.setEnabled(false);
			} else {
				if (((ObservableKeyListener) o).getTextField().getText().matches("[1-9]\\d*")) {
					this.setEnabled(true);
				} else {
					this.setEnabled(false);
				}
			}
		}
	}

}
