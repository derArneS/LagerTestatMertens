package lager.view.deliveryWindow;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

import lager.model.ObservableStack;
import lager.view.lagerWindow.ObservableKeyListener;

@SuppressWarnings("serial")
public class ObserverButton extends JButton implements Observer {
	boolean name = false;
	boolean capacity = false;

	public ObserverButton(String title) {
		super(title);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ObservableKeyListener) {
			if (((ObservableKeyListener) o).getName().equals("MENGE")) {
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
		} else if (o instanceof ObservableStack) {
			this.setEnabled(!((ObservableStack) o).isEmpty());
		}
	}

}
