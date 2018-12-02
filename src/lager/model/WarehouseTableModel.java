package lager.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class WarehouseTableModel extends AbstractTableModel {
	private String[] columnNames = { "Lager", "Name", "Kapazitaet" };
	private List<Warehouse> listWarehouse = new ArrayList<Warehouse>();

	public WarehouseTableModel() {
		super();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return listWarehouse.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Warehouse warehouse = listWarehouse.get(rowIndex);
		return warehouse.getValue(columnNames[columnIndex]);
	}

	public void addWarehouse(Warehouse warehouse) {
		listWarehouse.add(warehouse);
		fireTableDataChanged();
	}

	public Warehouse getWarehouseAtRow(int index) {
		return listWarehouse.get(index);
	}

}