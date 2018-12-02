package lager.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lager.model.BookingsTableModel;
import lager.model.Warehouse;
import lager.model.WarehouseTreeModel;
import lager.view.View;

public class Controller {
	private View view;
	private WarehouseTreeModel warehouseTreeModel;
	private BookingsTableModel bookingsTableModel;

	public Controller() {
		view = new View(this, "Lagersimulator");

		warehouseTreeModel = new WarehouseTreeModel();
		rewire(warehouseTreeModel, new BookingsTableModel());
		prestart();
	}

	private void prestart() {
		Warehouse deutschland = new Warehouse("Deutschland", 0);
		Warehouse niedersachsen = new Warehouse("Niedersachsen", 0);
		Warehouse hannoverMisburg = new Warehouse("Hannover-Misburg", 0);
		Warehouse nienburg = new Warehouse("Nienburg", 0);
		Warehouse nrw = new Warehouse("NRW", 0);
		Warehouse bremen = new Warehouse("Bremen", 0);
		Warehouse hessen = new Warehouse("Hessen", 0);
		Warehouse sachsen = new Warehouse("Sachsen", 0);
		Warehouse brandenburg = new Warehouse("Brandenburg", 0);
		Warehouse mv = new Warehouse("MV", 0);

		deutschland.addChild(niedersachsen);
		niedersachsen.addChild(hannoverMisburg);
		niedersachsen.addChild(nienburg);
		deutschland.addChild(nrw);
		deutschland.addChild(bremen);
		deutschland.addChild(hessen);
		deutschland.addChild(sachsen);
		deutschland.addChild(brandenburg);
		deutschland.addChild(mv);

		warehouseTreeModel.addWarehouse(deutschland, 0);
		warehouseTreeModel.addWarehouse(niedersachsen);
		warehouseTreeModel.addWarehouse(hannoverMisburg);
		warehouseTreeModel.addWarehouse(nienburg);
		warehouseTreeModel.addWarehouse(nrw);
		warehouseTreeModel.addWarehouse(bremen);
		warehouseTreeModel.addWarehouse(hessen);
		warehouseTreeModel.addWarehouse(sachsen);
		warehouseTreeModel.addWarehouse(brandenburg);
		warehouseTreeModel.addWarehouse(mv);

	}

	private void rewire(WarehouseTreeModel warehouseTreeModel, BookingsTableModel bookingsTableModel) {
		this.warehouseTreeModel = warehouseTreeModel;
		this.bookingsTableModel = bookingsTableModel;

		view.setWarehouseModel(warehouseTreeModel);
		view.setBookingsModel(bookingsTableModel);
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
		BookingsTableModel bookingsTableModel = ((BookingsTableModel) ois.readObject());

		rewire(warehouseTableModel, bookingsTableModel);
		ois.close();
	}

}
