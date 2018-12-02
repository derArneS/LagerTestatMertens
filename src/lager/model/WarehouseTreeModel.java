package lager.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class WarehouseTreeModel implements TreeModel {
	private List<Warehouse> connections = new ArrayList<Warehouse>();
	private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	public WarehouseTreeModel() {
		connections.add(new Warehouse("root", 0));
	}

	public void addWarehouse(Warehouse warehouse, int index) {
		connections.get(0).addChild(warehouse);
		connections.add(warehouse);
	}

	public void addWarehouse(Warehouse warehouse) {
		connections.add(warehouse);
	}

	@Override
	public Object getRoot() {
		return connections.get(0);
	}

	@Override
	public boolean isLeaf(Object node) {
		return getChildCount(node) == 0;
	}

	@Override
	public int getChildCount(Object parent) {
		connections.get(connections.indexOf(parent)).getChildCount();
		return connections.get(connections.indexOf(parent)).getChildCount();
	}

	@Override
	public Object getChild(Object parent, int index) {
		return connections.get(connections.indexOf(parent)).getChild(index);
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return connections.get(connections.indexOf(parent)).getIndexOfChild((Warehouse) child);
	}

	@Override
	public void addTreeModelListener(TreeModelListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// TODO Auto-generated method stub

	}
}