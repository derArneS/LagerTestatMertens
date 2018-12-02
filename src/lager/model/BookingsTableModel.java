package lager.model;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class BookingsTableModel extends AbstractTableModel {
	private String[] columnNames = { "Nr.", "Menge", "Antei 1", "Lager 1", "Anteil 2", "Lager 2", "Anteil 3", "Lager 3",
			"Anteil 4", "Lager 4", "Anteil 5", "Lager 5", "Summe Anteile" };
	private Object[][] data = { { "1", "1000", "50%", "Bremen", "20%", "MV", "10%", "Mailand", "10%", "Spanien", "10%",
			"Grossbritannien", "100%" } };

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
		return data.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}
}
