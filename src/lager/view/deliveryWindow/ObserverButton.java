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
			ObservableKeyListener l = (ObservableKeyListener) o;
			if (l.getName().equals("MENGE")) {
				if (l.getTextField().getText().isEmpty()) {
					this.setEnabled(false);
				} else {
					setEnabled(l.getTextField().getText().matches("[1-9]\\d*"));
				}
			}
		} else if (o instanceof ObservableStack) {
			this.setEnabled(!((ObservableStack) o).isEmpty());
		} else if (o instanceof ObservableChangeListener) {
			ObservableChangeListener l = (ObservableChangeListener) o;
			setEnabled(!(l.getSlider().getCurrentAmount() == 0));
		}
	}

}
