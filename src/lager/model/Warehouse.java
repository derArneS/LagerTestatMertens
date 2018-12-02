package lager.model;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
	private String name;
	private int capacity;
	private List<Warehouse> children = new ArrayList<>();

	public Warehouse(String name, int capacity) {
		this.name = name;
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return name;
	}

	public void addChild(Warehouse child) {
		children.add(child);
	}

	public int getChildCount() {
		return children.size();
	}

	public Warehouse getChild(int index) {
		return children.get(index);
	}

	public int getIndexOfChild(Warehouse child) {
		return children.indexOf(child);
	}

}
