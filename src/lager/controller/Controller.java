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
import lager.view.outputwindow.newOutputWindow;

/**
 * Der Controller des MVC-Patterns
 */
public class Controller {
	private View view;
	private WarehouseTreeModel warehouseTreeModel;
	private DeliveriesTableModel deliveriesTableModel;
	private WarehouseNode rootNode = new Warehouse("root", 0).getNode();
	private ObservableStack undoStack = new ObservableStack();
	private ObservableStack redoStack = new ObservableStack();

	/**
	 * Erzeugen der View und des Models und Verknüpfen der Models
	 */
	public Controller() {
		view = new View(this, "Lagersimulator");

		warehouseTreeModel = new WarehouseTreeModel(rootNode);
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

		rootNode.insert(deutschland.getNode());
		deutschland.getNode().insert(niedersachsen.getNode());
		niedersachsen.getNode().insert(hannoverMisburg.getNode());
		niedersachsen.getNode().insert(nienburg.getNode());
		deutschland.getNode().insert(nrw.getNode());
		deutschland.getNode().insert(bremen.getNode());
		deutschland.getNode().insert(hessen.getNode());
		deutschland.getNode().insert(sachsen.getNode());
		deutschland.getNode().insert(brandenburg.getNode());
		deutschland.getNode().insert(mv.getNode());

		warehouseTreeModel.reload();

	}

	/**
	 * Verknüpfen von 2 Models
	 * 
	 * @param warehouseTreeModel Warenhäuser
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
	 * Hinzufügen der Warehouses aus einem JTree
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
	 * Hinzufügen eines neuen Warehouses zur Datenstruktur
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
	 * Auslösen einer neuen Buchung
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
	 * Hinzufügen eines neuen Buchungsteileintrags
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
	 * Rückgängigmachen einer Buchung
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
	 * Rückgbe des Undo-Stacks
	 * 
	 * @return Undo-Stack
	 */
	public ObservableStack getUndoStack() {
		return undoStack;
	}

	/**
	 * Rückgabe des Delivery-Models
	 * 
	 * @return Delivery-Model
	 */
	public DeliveriesTableModel getDeliveryModel() {
		return deliveriesTableModel;
	}

	/**
	 * Zurücksetzen des Undo-Stacks
	 */
	public void resetUndoStack() {
		while (!undoStack.isEmpty()) {
			Command command = undoStack.pop();
			command.undo();
		}
	}

	public void newOutput() {
		newOutputWindow w = new newOutputWindow(view);
		w.outoutPane(this);
	}

	public void newOutput(Object selectedItem, int parseInt) {
		Warehouse w = (Warehouse) selectedItem;
		w.addToStock(-parseInt);
		deliveriesTableModel.addToOutputData(w, parseInt);
		view.updateUI();
	}

}
