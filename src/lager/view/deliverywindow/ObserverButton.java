package lager.view.deliverywindow;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

import lager.model.ObservableStack;
import lager.view.lagerwindow.ObservableKeyListener;

/**
 * Erweiterung eines JButtons zu einem ObserverButtons
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
		if (o instanceof ObservableKeyListener) {
			/*
			 * Wenn das Observable ein KeyListener ist, wird bei dem Feld Menge überprüft, ob die Eingabe valide
			 * (nur Zahlen) ist. Falls ja, wird dieser Button aktviert, andernfalls deaktiviert
			 */
			ObservableKeyListener l = (ObservableKeyListener) o;
			if (l.getName().equals("MENGE")) {
				if (l.getTextField().getText().isEmpty()) {
					this.setEnabled(false);
				} else {
					setEnabled(l.getTextField().getText().matches("[1-9]\\d*"));
				}
			}
		} else if (o instanceof ObservableStack) {
			/*
			 * Wenn das Observable ein ObservableStack ist, wird der Button aktiviert, solange der Stack gefüllt
			 * ist. Dies ist im Buchungsdialog der Fall, um Zurück drücken zu können, oder nicht.
			 */
			this.setEnabled(!((ObservableStack) o).isEmpty());
		} else if (o instanceof ObservableChangeListener) {
			/**
			 * Wenn das Observable ein ObservableChangeListenr ist, wird der Button aktiviert sobald der 
			 * Buchungsumfang aufgeteilt wurde
			 */
			ObservableChangeListener l = (ObservableChangeListener) o;
			ObserverJSlider slider = l.getSlider();
			if (slider.getIteration() == 5 && slider.getAmountLeft() - slider.getValue() != 0) {
				setEnabled(false);
			} else {
				setEnabled(slider.getCurrentAmount() != 0);
			}

		}
	}

}
