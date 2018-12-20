package lager.view.deliveryWindow;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JSlider;

import lager.model.Warehouse;

@SuppressWarnings("serial")
public class ObserverJSlider extends JSlider implements Observer {
	int amount;

	public ObserverJSlider(int min, int max, int init, int amount) {
		super(min, max, init);
		this.amount = amount;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Warehouse warehouse = (Warehouse) arg1;

		float percent = ((float) (warehouse.getCapacity() - warehouse.getStock())) / ((float) amount) * 100;
		if (amount > warehouse.getCapacity() - warehouse.getStock()) {
			setMaximum((int) percent);
		} else {
			setMaximum(100);
		}
	}

	public int getCurrentAmount() {
		return (int) ((float) getValue() / (float) 100 * amount);
	}

	public int getCurrentPercentage() {
		return getCurrentAmount() * 100 / amount;
	}

}
