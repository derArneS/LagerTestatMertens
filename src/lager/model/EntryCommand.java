package lager.model;

public class EntryCommand implements Command {
	private DeliveriesTableModel model;
	private int iteration, percentage;
	private Warehouse warehouse;

	public EntryCommand(DeliveriesTableModel model, int percentage, int iteration, Warehouse warehouse) {
		this.model = model;
		this.iteration = iteration;
		this.percentage = percentage;
		this.warehouse = warehouse;
	}

	@Override
	public void exec() {
		model.setEntry(percentage, warehouse, iteration);
	}

	@Override
	public Object[] undo() {
		model.setEntry(null, null, iteration);
		return new Object[] { percentage, warehouse };
	}

}
