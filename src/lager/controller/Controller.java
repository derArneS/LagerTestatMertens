package lager.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lager.model.BookingsTableModel;
import lager.model.Warehouse;
import lager.model.WarehouseTableModel;
import lager.view.View;

public class Controller {
	private View view;
	private WarehouseTableModel warehouseTableModel;
	private BookingsTableModel bookingsTableModel;

	public Controller() {
		view = new View(this, "Lagersimulator");
		rewire(new WarehouseTableModel(), new BookingsTableModel());
	}

	private void rewire(WarehouseTableModel warehouseTableModel, BookingsTableModel bookingsTableModel) {
		this.warehouseTableModel = warehouseTableModel;
		this.bookingsTableModel = bookingsTableModel;

		view.setWarehouseModel(warehouseTableModel);
		view.setBookingsModel(bookingsTableModel);
	}

	public void save(File file) throws Exception {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(warehouseTableModel);
		oos.writeObject(bookingsTableModel);
		oos.close();
	}

	public void load(File selectedFile) throws Exception {
		FileInputStream fis = new FileInputStream(selectedFile);
		ObjectInputStream ois = new ObjectInputStream(fis);

		WarehouseTableModel warehouseTableModel = ((WarehouseTableModel) ois.readObject());
		BookingsTableModel bookingsTableModel = ((BookingsTableModel) ois.readObject());

		rewire(warehouseTableModel, bookingsTableModel);
		ois.close();
	}

	public void newWarehouse(String name, Warehouse warehouse) {
		warehouseTableModel.addWarehouse(new Warehouse(name, warehouse));
	}

	public void newWarehouse(String name, int capacity) {
		warehouseTableModel.addWarehouse(new Warehouse(name, capacity));
	}

}
