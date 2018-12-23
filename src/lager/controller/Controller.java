package lager.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JTree;

import lager.model.Command;
import lager.model.DeliveriesTableModel;
import lager.model.EntryCommand;
import lager.model.ObservableStack;
import lager.model.Warehouse;
import lager.model.WarehouseNode;
import lager.model.WarehouseTreeModel;
import lager.view.View;
import lager.view.deliverywindow.NewDeliveryWindow;
import lager.view.lagerwindow.NewLagerWindow;
import lager.view.outputwindow.NewOutputWindow;

/**
 * Der Controller des MVC-Patterns
 */
public class Controller {
	private View view;
	private WarehouseTreeModel warehouseTreeModel;
	private DeliveriesTableModel deliveriesTableModel;
	private Warehouse root = new Warehouse("root", 0);
	private ObservableStack undoStack = new ObservableStack();
	private ObservableStack redoStack = new ObservableStack();

	/**
	 * Erzeugen der View und des Models und Verkn�pfen der Models
	 */
	public Controller() {
		view = new View(this, "Lagersimulator");

		warehouseTreeModel = new WarehouseTreeModel(root.getNode());
		rewire(warehouseTreeModel, new DeliveriesTableModel());
		prestart();
	}

	/**
	 * Initialisierung der Warehouses und Aufbau der Start-Datenstruktur
	 */
	private void prestart() {
		Warehouse deutschland = new Warehouse("Deutschland", 10);

		Warehouse niedersachsen = new Warehouse("Niedersachsen", 10);
		Warehouse hannoverMisburg = new Warehouse("Hannover-Misburg", 10);
		Warehouse nienburg = new Warehouse("Nienburg", 20);

		Warehouse nrw = new Warehouse("NRW", 10);
		Warehouse bremen = new Warehouse("Bremen", 10);
		Warehouse hessen = new Warehouse("Hessen", 10);
		Warehouse sachsen = new Warehouse("Sachsen", 10);
		Warehouse brandenburg = new Warehouse("Brandenburg", 10);
		Warehouse mv = new Warehouse("MV", 10);

		Warehouse europa = new Warehouse("Europa", 10);
		Warehouse frankreich = new Warehouse("Frankreich", 10);
		Warehouse parisnord = new Warehouse("Paris-Nord", 10);
		Warehouse orleans = new Warehouse("Orl�ans", 20);
		Warehouse marseille = new Warehouse("Marseille", 10);
		Warehouse nimes = new Warehouse("N�mes", 10);

		Warehouse italien = new Warehouse("Italien", 10);
		Warehouse mailand = new Warehouse("Mailand", 10);
		Warehouse laquila = new Warehouse("L'Aquila", 10);

		Warehouse spanien = new Warehouse("Spanien", 10);

		Warehouse grossbritannien = new Warehouse("Gro�brittanien", 10);

		root.getNode().insert(deutschland.getNode());
		deutschland.getNode().insert(niedersachsen.getNode());
		niedersachsen.getNode().insert(hannoverMisburg.getNode());
		niedersachsen.getNode().insert(nienburg.getNode());
		deutschland.getNode().insert(nrw.getNode());
		deutschland.getNode().insert(bremen.getNode());
		deutschland.getNode().insert(hessen.getNode());
		deutschland.getNode().insert(sachsen.getNode());
		deutschland.getNode().insert(brandenburg.getNode());
		deutschland.getNode().insert(mv.getNode());

		root.getNode().insert(europa.getNode());
		europa.getNode().insert(frankreich.getNode());
		frankreich.getNode().insert(parisnord.getNode());
		frankreich.getNode().insert(orleans.getNode());
		frankreich.getNode().insert(marseille.getNode());
		frankreich.getNode().insert(nimes.getNode());

		europa.getNode().insert(italien.getNode());
		italien.getNode().insert(mailand.getNode());
		italien.getNode().insert(laquila.getNode());

		europa.getNode().insert(spanien.getNode());

		root.getNode().insert(grossbritannien.getNode());

		warehouseTreeModel.reload();

	}

	/**
	 * Verkn�pfen von 2 Models
	 * 
	 * @param warehouseTreeModel Warenh�user
	 * @param bookingsTableModel Buchungen
	 */
	private void rewire(WarehouseTreeModel warehouseTreeModel, DeliveriesTableModel bookingsTableModel) {
		this.warehouseTreeModel = warehouseTreeModel;
		this.deliveriesTableModel = bookingsTableModel;

		view.setWarehouseModel(warehouseTreeModel);
		view.setDeliveryModel(bookingsTableModel);
	}

	/**
	 * Speichert die aktuelle Lager- und Buchungsstruktur
	 * 
	 * @param file Datei, die gespeichert werden soll
	 * @throws Exception
	 */
	public void save(File file) throws Exception {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(warehouseTreeModel);
		oos.writeObject(deliveriesTableModel);
		oos.close();
	}

	/**
	 * Laden einer Lager- und Buchungsstruktur
	 */
	public void load(File selectedFile) throws Exception {
		FileInputStream fis = new FileInputStream(selectedFile);
		ObjectInputStream ois = new ObjectInputStream(fis);

		WarehouseTreeModel warehouseTableModel = ((WarehouseTreeModel) ois.readObject());
		DeliveriesTableModel bookingsTableModel = ((DeliveriesTableModel) ois.readObject());

		rewire(warehouseTableModel, bookingsTableModel);
		ois.close();
	}

	/**
	 * Entfernen einen Warehouses
	 * 
	 * @param warehouse
	 */
	public void removeWarehouse(Warehouse warehouse) {
		for (WarehouseNode n : warehouse.getNode().getChildren().values()) {
			((WarehouseNode) warehouse.getNode().getParent()).insert(n);
		}

		warehouseTreeModel.removeNodeFromParent(warehouse.getNode());

	}

	/**
	 * Hinzuf�gen der Warehouses aus einem JTree
	 * 
	 * @param warehouses
	 * @param view
	 */
	public void addWarehouse(JTree warehouses, View view) {
		NewLagerWindow newLagerWindow = new NewLagerWindow(view);
		if (newLagerWindow.optionPane()) {
			if (warehouses.getSelectionCount() == 0) {
				addWarehouse(((WarehouseNode) warehouses.getModel().getRoot()).getWarehouse(), newLagerWindow.getName(),
						newLagerWindow.getCapacity());
			} else {

				addWarehouse(((WarehouseNode) warehouses.getSelectionPath().getLastPathComponent()).getWarehouse(),
						newLagerWindow.getName(), newLagerWindow.getCapacity());
			}
		}

	}

	/**
	 * Hinzuf�gen eines neuen Warehouses zur Datenstruktur
	 * 
	 * @param warehouse
	 * @param name
	 * @param capacity
	 */
	public void addWarehouse(Warehouse warehouse, String name, int capacity) {
		Warehouse newWarehouse = new Warehouse(name, capacity);

		if (warehouse.getNode().getChildCount() == 0) {
			newWarehouse.addToCapacity(warehouse.getCapacity());
			newWarehouse.addToStock(warehouse.getStock());
			warehouse.setStock(0);
		}

		warehouse.getNode().insert(newWarehouse.getNode());

	}

	/**
	 * Ausl�sen einer neuen Buchung
	 * 
	 * @param view
	 */
	public void newDelivery() {
		NewDeliveryWindow delivery = new NewDeliveryWindow(view);
		view.updateUI();
		deliveriesTableModel.addNewDelivery();
		int amount = delivery.amountPane();
		if (amount > 0) {
			deliveriesTableModel.addAmount(amount);
			view.updateUI();
			String bookingCheck = delivery.bookingPane(amount, this);
			if (!bookingCheck.equals("WEITER")) {
				resetUndoStack();
				deliveriesTableModel.deleteLastDelivery();
			}
		} else {
			resetUndoStack();
			deliveriesTableModel.deleteLastDelivery();
		}

		view.updateUI();
		undoStack.clear();
		redoStack.clear();

	}

	/**
	 * Hinzuf�gen eines neuen Buchungsteileintrags
	 * 
	 * @param percentage
	 * @param iteration
	 * @param warehouse
	 * @param amount
	 */
	public void addDelieveryEntry(double percentage, int iteration, Warehouse warehouse, int amount) {
		Command command = new EntryCommand(deliveriesTableModel, percentage, iteration, warehouse, amount);
		command.exec();
		undoStack.push(command);
		view.updateUI();
	}

	/**
	 * R�ckg�ngigmachen einer Buchung
	 * 
	 * @return
	 */
	public Object[] undoDeliveryEntry() {
		Command command = undoStack.pop();
		Object array[] = command.undo();
		redoStack.push(command);
		view.updateUI();
		return array;
	}

	/**
	 * R�ckgbe des Undo-Stacks
	 * 
	 * @return Undo-Stack
	 */
	public ObservableStack getUndoStack() {
		return undoStack;
	}

	/**
	 * R�ckgabe des Delivery-Models
	 * 
	 * @return Delivery-Model
	 */
	public DeliveriesTableModel getDeliveryModel() {
		return deliveriesTableModel;
	}

	/**
	 * Zur�cksetzen des Undo-Stacks
	 */
	public void resetUndoStack() {
		while (!undoStack.isEmpty()) {
			Command command = undoStack.pop();
			command.undo();
		}
	}

	/**
	 * Ausl�sen einer neuen Auslieferung.
	 * 
	 * @param view
	 * @param controller
	 */
	public void newOutput() {
		NewOutputWindow w = new NewOutputWindow(view);
		w.outputPane(this);
	}

	/**
	 * Entfernt die gew�nschte Menge aus dem Bestand des mitgegeben Lagers.
	 * 
	 * @param warehouse
	 * @param amount
	 */
	public void newOutput(Object warehouse, int amount) {
		Warehouse w = (Warehouse) warehouse;
		w.addToStock(-amount);
		deliveriesTableModel.addToOutputData(w, amount);
		view.updateUI();
	}

	/**
	 * Gibt f�r das mitgegebene Lager alle Zulieferungen zur�ck
	 * 
	 * @param warehouse
	 * @return
	 */
	public Object[][] getInputDataForWarehouse(Warehouse warehouse) {
		Object[][] data = deliveriesTableModel.getInputData();

		if (data.length > 0) {
			Object[][] temp = new Object[0][data[0].length];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					if (warehouse.getName().equals(data[i][j])) {

						Object[][] temp2 = new Object[temp.length + 1][data[0].length];
						for (int k = 0; k < temp.length; k++) {
							for (int k2 = 0; k2 < temp[0].length; k2++) {
								temp2[k][k2] = temp[k][k2];
							}
						}

						for (int j2 = 0; j2 < data[i].length; j2++) {
							temp2[temp2.length - 1][j2] = data[i][j2];
						}

						temp = temp2;
						j = data[i].length;
					}
				}
			}
			return temp;
		}
		return null;
	}

	/**
	 * Gibt f�r das mitgegebene Lager alle Auslieferungen zur�ck
	 * 
	 * @param w
	 * @return
	 */
	public Object[][] getOutputDataForWarehouse(Warehouse w) {
		Object[][] data = deliveriesTableModel.getOutputData();

		if (data.length > 0) {
			Object[][] temp = new Object[0][data[0].length];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					if (data[i][j].equals(w)) {

						Object[][] temp2 = new Object[temp.length + 1][data[0].length];
						for (int k = 0; k < temp.length; k++) {
							for (int k2 = 0; k2 < temp[0].length; k2++) {
								temp2[k][k2] = temp[k][k2];
							}
						}

						for (int j2 = 0; j2 < data[i].length; j2++) {
							temp2[temp2.length - 1][j2] = data[i][j2];
						}

						temp = temp2;
						j = data[i].length;
					}
				}
			}
			return temp;
		}
		return null;
	}
}
