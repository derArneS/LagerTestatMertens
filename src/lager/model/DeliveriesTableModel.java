package lager.model;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class DeliveriesTableModel extends AbstractTableModel {
	private String[] columnNames = { "Nr.", "Menge", "Antei 1", "Lager 1", "Anteil 2", "Lager 2", "Anteil 3", "Lager 3",
			"Anteil 4", "Lager 4", "Anteil 5", "Lager 5", "Summe Anteile" };
	private Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();

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
		return data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex + 1)[columnIndex];
	}

	public int addNewDelivery() {
		data.put(data.size() + 1, new Object[13]);
		data.get(data.size())[0] = data.size();
		return data.size();
	}

	public void addAmount(int amount) {
		data.get(data.size())[1] = amount;
	}

	public void setEntry(Integer percentage, Warehouse warehouse, int iteration) {
		data.get(data.size())[2 * iteration] = percentage;
		data.get(data.size())[2 * iteration + 1] = warehouse;

		int tempPercentage = 0;
		for (int i = 1; i < 5; i++) {
			Integer integer = (Integer) data.get(data.size())[2 * i];
			tempPercentage += integer == null ? 0 : integer;
		}

		data.get(data.size())[12] = tempPercentage;
	}

	public void deleteLastDelivery() {
		data.remove(data.size());
	}

	public int getPercentage() {
		return (int) data.get(data.size())[12];
	}

}
