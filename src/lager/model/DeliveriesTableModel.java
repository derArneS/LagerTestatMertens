package lager.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

/**
 * Buchungs-Model
 */
@SuppressWarnings("serial")
public class DeliveriesTableModel extends AbstractTableModel {
	private String[] columnNames = { "Nr.", "Menge", "Antei 1", "Lager 1", "Anteil 2", "Lager 2", "Anteil 3", "Lager 3",
			"Anteil 4", "Lager 4", "Anteil 5", "Lager 5", "Summe Anteile" };
	private Map<Integer, Object[]> inputData = new HashMap<Integer, Object[]>();
	private Map<Integer, Object[]> outputData = new HashMap<Integer, Object[]>();

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
		return inputData.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return inputData.get(rowIndex + 1)[columnIndex];
	}

	/**
	 * Start der Eingabe einer neuen Buchung
	 * 
	 * @return Buchungsnummer
	 */
	public int addNewDelivery() {
		inputData.put(inputData.size() + 1, new Object[14]);
		inputData.get(inputData.size())[0] = inputData.size();
		inputData.get(inputData.size())[13] = new Date();
		return inputData.size();
	}

	/**
	 * Setzen des Umfangs der neuen Buchung
	 * 
	 * @param amount
	 */
	public void addAmount(int amount) {
		inputData.get(inputData.size())[1] = amount;
	}

	/**
	 * Setzen eines Eintrags in die neue Buchung
	 * 
	 * @param percentage
	 * @param warehouse
	 * @param iteration
	 * @param amount
	 */
	public void setEntry(Double percentage, Warehouse warehouse, int iteration, int amount) {
		if (percentage != null) {
			inputData.get(inputData.size())[2 * iteration] = ((double) Math.round(percentage * 100)) / 100;
			inputData.get(inputData.size())[2 * iteration + 1] = warehouse.getName();
		} else {
			inputData.get(inputData.size())[2 * iteration] = null;
			inputData.get(inputData.size())[2 * iteration + 1] = null;
		}

		double tempPercentage = 0;
		for (int i = 1; i < 6; i++) {
			Double two = (Double) inputData.get(inputData.size())[2 * i];
			tempPercentage += two == null ? 0 : two;
		}

		warehouse.addToStock(amount);

		inputData.get(inputData.size())[12] = tempPercentage;
	}

	/**
	 * Entfernen der letzten Buchung
	*/
	public void deleteLastDelivery() {
		inputData.remove(inputData.size());
	}

	public void addToOutputData(Warehouse w, int i) {
		Object[] o = { w, i, new Date() };
		outputData.put(outputData.size() + 1, o);
	}

}
