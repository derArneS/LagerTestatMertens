package lager.model;

public class Warehouse {
	private String name;
	private Integer capacity;
	private String number;
	private Warehouse preWarehouse;

	public Warehouse(String name, int capacity) {
		this.name = name;
		this.capacity = capacity;
	}

	public Warehouse(String name, Warehouse warehouse) {
		this.name = name;
		preWarehouse = warehouse;
		capacity = null;
	}

	@Override
	public String toString() {
		return name;
	}

	public Object getValue(String row) {
		switch (row) {
		case "Lager":
			return number;
		case "Name":
			return name;
		case "Kapazitaet":
			return capacity;
		default:
			return null;
		}
	}

}
