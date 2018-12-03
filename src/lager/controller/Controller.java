package lager.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lager.model.BookingsTableModel;
import lager.model.Warehouse;
import lager.model.WarehouseNode;
import lager.model.WarehouseTreeModel;
import lager.view.View;

public class Controller {
	private View view;
	private WarehouseTreeModel warehouseTreeModel;
	private BookingsTableModel bookingsTableModel;
	private WarehouseNode rootNode = new WarehouseNode(new Warehouse("root", 0));

	public Controller() {
		view = new View(this, "Lagersimulator");

		warehouseTreeModel = new WarehouseTreeModel(rootNode);
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

//		warehouseTreeModel.addWarehouse(deutschland, rootNode);
//		warehouseTreeModel.addWarehouse(niedersachsen, deutschland.getNode());
//		warehouseTreeModel.addWarehouse(hannoverMisburg, niedersachsen.getNode());
//		warehouseTreeModel.addWarehouse(nienburg, deutschland.getNode());
//		warehouseTreeModel.addWarehouse(nrw, deutschland.getNode());
//		warehouseTreeModel.addWarehouse(bremen, deutschland.getNode());
//		warehouseTreeModel.addWarehouse(hessen, deutschland.getNode());
//		warehouseTreeModel.addWarehouse(sachsen, deutschland.getNode());
//		warehouseTreeModel.addWarehouse(brandenburg, deutschland.getNode());
//		warehouseTreeModel.addWarehouse(mv, deutschland.getNode());

		warehouseTreeModel.reload();

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

	public void removeWarehouse(Warehouse warehouse) {
		for (WarehouseNode n : warehouse.getNode().getChildren().values()) {
			((WarehouseNode) warehouse.getNode().getParent()).insert(n);
		}

		((WarehouseNode) warehouse.getNode().getParent()).remove(warehouse.getNode());
		warehouseTreeModel.reload();
	}

}
