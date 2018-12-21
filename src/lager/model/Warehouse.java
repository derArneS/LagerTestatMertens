package lager.model;

import java.io.Serializable;

/**
 * Die zantrale Datenstruktur Warehouse
 */
@SuppressWarnings("serial")
public class Warehouse implements Serializable {
	private String name;
	private int capacity;
	private int stock;
	private WarehouseNode warehouseNode;

	/**
	 * Erstellen eines Warenhauses mit Name und Kapazität
	 * Initialisierung einer WarehouseNode für dieses Warehouse zur Verwendung im JTree
	 * 
	 * @param name
	 * @param capacity
	 */
	public Warehouse(String name, int capacity) {
		this.name = name;
		this.capacity = capacity;
		warehouseNode = new WarehouseNode(this);
		stock = 0;
	}

	@Override
	public String toString() {
		if (getNode().getChildCount() == 0) {
			return name + " " + stock + "/" + capacity;
		}
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

	public int getStock() {
		return stock;
	}

	public int getChildStock() {
		WarehouseNode node = getNode();
		int tempStock = 0;

		if (node.getChildCount() != 0) {
			for (WarehouseNode w : getNode().getChildren().values()) {
				tempStock += w.getWarehouse().getChildStock();
			}
		} else {
			tempStock += stock;
		}

		return tempStock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean addToStock(int stocking) {
		if (stock + stocking > capacity)
			return false;
		stock += stocking;
		return true;

	}

	public void addToCapacity(int addCap) {
		capacity += addCap;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getName() {
		return name;
	}

}
