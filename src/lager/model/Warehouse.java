package lager.model;

public class Warehouse {
	private String name;
	private int capacity;
	private WarehouseNode warehouseNode;

	public Warehouse(String name, int capacity) {
		this.name = name;
		this.capacity = capacity;
		warehouseNode = new WarehouseNode(this);
	}

	@Override
	public String toString() {
		return name;
	}

	public WarehouseNode getNode() {
		return warehouseNode;

	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}
