package lager.model;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class WarehouseNode implements MutableTreeNode {
	private Warehouse warehouse;
	private Map<Integer, WarehouseNode> map = new HashMap<Integer, WarehouseNode>();
	private WarehouseNode parent;

	public WarehouseNode(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return map.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return map.size();
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		for (Integer i : map.keySet()) {
			if (map.get(i).equals(node)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean getAllowsChildren() {
		return map.size() == 0;
	}

	@Override
	public boolean isLeaf() {
		return map.size() == 0;
	}

	@Override
	public Enumeration<? extends TreeNode> children() {
		return (Enumeration<? extends TreeNode>) map.values();
	}

	public Map<Integer, WarehouseNode> getChildren() {
		return map;
	}

	public void insert(MutableTreeNode child) {
		insert(child, map.size());
	}

	@Override
	public void insert(MutableTreeNode child, int index) {
		child.setParent(this);
		map.put(index, (WarehouseNode) child);
	}

	@Override
	public void remove(int index) {
		map.remove(index);
	}

	@Override
	public void remove(MutableTreeNode node) {
		int index = getIndex(node);
		if (index != -1) {
			map.remove(index);
		}
	}

	@Override
	public void setUserObject(Object object) {

	}

	@Override
	public void removeFromParent() {
		parent.remove(this);
		parent = null;
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		parent = (WarehouseNode) newParent;
	}

	@Override
	public String toString() {
		return warehouse.toString();
	}

}
