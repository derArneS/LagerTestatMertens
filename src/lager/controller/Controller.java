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
import lager.view.deliveryWindow.NewDeliveryWindow;
import lager.view.lagerWindow.NewLagerWindow;

public class Controller {
	private View view;
	private WarehouseTreeModel warehouseTreeModel;
	private DeliveriesTableModel bookingsTableModel;
	private WarehouseNode rootNode = new Warehouse("root", 0).getNode();
	private ObservableStack undoStack = new ObservableStack();
	private ObservableStack redoStack = new ObservableStack();

	public Controller() {
		view = new View(this, "Lagersimulator");

		warehouseTreeModel = new WarehouseTreeModel(rootNode);
		rewire(warehouseTreeModel, new DeliveriesTableModel());
		prestart();
	}

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

	private void rewire(WarehouseTreeModel warehouseTreeModel, DeliveriesTableModel bookingsTableModel) {
		this.warehouseTreeModel = warehouseTreeModel;
		this.bookingsTableModel = bookingsTableModel;

		view.setWarehouseModel(warehouseTreeModel);
		view.setDeliveryModel(bookingsTableModel);
	}

	public void save(File file) throws Exception {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(warehouseTreeModel);
		oos.writeObject(bookingsTableModel);
		oos.close();
	}

	public void load(File selectedFile) throws Exception {
		FileInputStream fis = new FileInputStream(selectedFile);
		ObjectInputStream ois = new ObjectInputStream(fis);

		WarehouseTreeModel warehouseTableModel = ((WarehouseTreeModel) ois.readObject());
		DeliveriesTableModel bookingsTableModel = ((DeliveriesTableModel) ois.readObject());

		rewire(warehouseTableModel, bookingsTableModel);
		ois.close();
	}

	public void removeWarehouse(Warehouse warehouse) {
		for (WarehouseNode n : warehouse.getNode().getChildren().values()) {
			((WarehouseNode) warehouse.getNode().getParent()).insert(n);
		}

		warehouseTreeModel.removeNodeFromParent(warehouse.getNode());

	}

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

	public void addWarehouse(Warehouse warehouse, String name, int capacity) {
		Warehouse newWarehouse = new Warehouse(name, capacity);

		if (warehouse.getNode().getChildCount() == 0) {
			newWarehouse.addToCapacity(warehouse.getCapacity());
			newWarehouse.addToStock(warehouse.getStock());
			warehouse.setStock(0);
		}

		warehouse.getNode().insert(newWarehouse.getNode());

	}

	public void newDelivery(View view) {
		NewDeliveryWindow delivery = new NewDeliveryWindow(view);
		bookingsTableModel.addNewDelivery();
		int amount = delivery.amountPane();
		if (amount > 0) {
			bookingsTableModel.addAmount(amount);
			String bookingCheck = delivery.bookingPane(amount, this);
			if (bookingCheck.equals("CANCEL")) {
				bookingsTableModel.deleteLastDelivery();
			}
		} else {
			bookingsTableModel.deleteLastDelivery();
		}

	}

	public void addDelieveryEntry(int percentage, int iteration, Warehouse warehouse) {
		Command command = new EntryCommand(bookingsTableModel, percentage, iteration, warehouse);
		command.exec();
		undoStack.push(command);
		view.updateUI();
	}

	public Object[] undoDeliveryEntry() {
		Command command = undoStack.pop();
		Object array[] = command.undo();
		redoStack.push(command);
		view.updateUI();
		return array;
	}

	public ObservableStack getUndoStack() {
		return undoStack;
	}

}
