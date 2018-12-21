package lager.model;

public class EntryCommand implements Command {
	private DeliveriesTableModel model;
	private int iteration, amount;
	private double percentage;
	private Warehouse warehouse;

	public EntryCommand(DeliveriesTableModel model, double percentage, int iteration, Warehouse warehouse, int amount) {
		this.model = model;
		this.iteration = iteration;
		this.percentage = percentage;
		this.warehouse = warehouse;
		this.amount = amount;
	}

	@Override
	public void exec() {
		model.setEntry(percentage, warehouse, iteration, amount);
	}

	@Override
	public Object[] undo() {
		model.setEntry(null, warehouse, iteration, -amount);
		return new Object[] { percentage, warehouse };
	}

}
