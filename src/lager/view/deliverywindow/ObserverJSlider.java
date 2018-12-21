package lager.view.deliverywindow;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JSlider;

import lager.model.Warehouse;

@SuppressWarnings("serial")
public class ObserverJSlider extends JSlider implements Observer {
	private int amount;
	private int originalAmount;
	private int iteration;

	public ObserverJSlider(int min, int max, int init, int amount) {
		super(min, max, init);
		this.amount = amount;
		iteration = 0;
		originalAmount = amount;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Warehouse warehouse = (Warehouse) arg1;

		int free = warehouse.getCapacity() - warehouse.getStock();

		if (amount > free) {
			setMaximum(free);
		} else {
			setMaximum(amount);
		}
	}

	public int getCurrentAmount() {
		return getValue();
	}

	public double getCurrentPercentage() {
		return getValue() * 100.0 / originalAmount;
	}

	public void setAmountLeft(int amountLeft) {
		this.amount = amountLeft;
	}

	public int getAmountLeft() {
		return amount;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public int getIteration() {
		return iteration;
	}

}
