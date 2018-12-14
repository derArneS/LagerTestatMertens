package lager.view.deliveryWindow;

import java.util.Observable;
import java.util.Observer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ObservableChangeListener extends Observable implements ChangeListener {
	private ObserverJSlider slider;

	public ObservableChangeListener(ObserverJSlider slider) {
		super();
		this.slider = slider;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		this.setChanged();
		this.notifyObservers(slider.getCurrentAmount());

	}

	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
		super.setChanged();
		super.notifyObservers(slider.getCurrentAmount());
	}

}
