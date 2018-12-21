package lager.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;

import lager.controller.Controller;
import lager.model.Warehouse;
import lager.model.WarehouseNode;

@SuppressWarnings("serial")
public class View extends JFrame {
	private Controller controller;
	private JButton save = new JButton("Speichern");
	private JButton open = new JButton("Oeffnen");
	private JButton newWarehouse = new JButton("Neues Lager");
	private JButton delWarehouse = new JButton("Lager loeschen");
	private JButton newDelivery = new JButton("Neue Lieferung");
	private JButton newOutput = new JButton("Neue Auslieferung");
	private JButton clearSelectionWarehouse = new JButton("Auswahl loeschen");
	private JButton changeName = new JButton("Name aendern");
	private JButton stock = new JButton("Bestand anzeigen");
	private JPanel controls = new JPanel();
	private JPanel visualization = new JPanel();
	private JScrollPane warehousesPane = new JScrollPane();
	private JScrollPane deliveryPane = new JScrollPane();
	private JTree warehouses = new JTree();
	private JTable deliveries = new JTable();

	public View(Controller controller, String titel) {
		super(titel);
		this.controller = controller;
		viewAufbauen();
	}

	private void viewAufbauen() {
		controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
		visualization.setLayout(new BoxLayout(visualization, BoxLayout.Y_AXIS));

		controls.add(save);
		controls.add(open);
		controls.add(newWarehouse);
		controls.add(delWarehouse);
		controls.add(newDelivery);
		controls.add(newOutput);
		controls.add(changeName);
		controls.add(stock);

		warehouses.setRootVisible(false);

		warehousesPane.getViewport().add(warehouses);
		deliveryPane.getViewport().add(deliveries);
		deliveries.setFillsViewportHeight(true);

		visualization.add(warehousesPane);
		visualization.add(clearSelectionWarehouse);
		visualization.add(deliveryPane);

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Lagersystemdatei", ".lsd");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(View.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("Sie haben diese Datei gespeichert:" + chooser.getSelectedFile().getName());
					try {
						controller.save(new File(chooser.getSelectedFile().toString() + ".lsd"));
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Fehler beim Speichern: " + e1.getMessage());
					}
				}
			}
		});

		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Lagersystemdatei", ".lsd");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(View.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("Sie haben diese Datei goeffnet:" + chooser.getSelectedFile().getName());
					try {
						controller.load(chooser.getSelectedFile());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Fehler beim Laden: " + e1.getMessage());
					}
				}

			}
		});

		newWarehouse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.addWarehouse(warehouses, View.this);
				warehouses.updateUI();
			}
		});

		delWarehouse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (warehouses.getSelectionCount() != 0) {
					if (((WarehouseNode) warehouses.getLastSelectedPathComponent()).getWarehouse().getStock() == 0) {
						controller.removeWarehouse(
								((WarehouseNode) warehouses.getSelectionPath().getLastPathComponent()).getWarehouse());
						warehouses.updateUI();
					} else {
						JOptionPane.showMessageDialog(View.this, "Lager muss zum entfernen leer sein",
								"Lager mit Inhalt", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(View.this, "Kein Lager ausgewaehlt", "Kein Lager",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		newDelivery.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.newDelivery();
				newOutput.setEnabled(checkForStock());
			}

		});

		newOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.newOutput();
				newOutput.setEnabled(checkForStock());
			}
		});

		clearSelectionWarehouse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				warehouses.clearSelection();
			}
		});

		changeName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (warehouses.getSelectionCount() != 0) {
					String name = JOptionPane.showInputDialog(View.this, "Neuen Namen fuer das Lager eingeben",
							"Namen aendern", JOptionPane.QUESTION_MESSAGE);
					if (name != null) {
						((WarehouseNode) warehouses.getSelectionPath().getLastPathComponent()).getWarehouse()
								.setName(name);
					}
				} else {
					JOptionPane.showMessageDialog(View.this, "Kein Lager ausgewaehlt", "Kein Lager",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		stock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (warehouses.getSelectionCount() != 0) {
					Warehouse w = ((WarehouseNode) warehouses.getSelectionPath().getLastPathComponent()).getWarehouse();
					JOptionPane.showMessageDialog(View.this, w.getChildStock(), "Bestand",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(View.this, "Kein Lager ausgewaehlt", "Kein Lager",
							JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(controls);
		this.add(visualization);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void setWarehouseModel(TreeModel model) {
		warehouses.setModel(model);
		newOutput.setEnabled(checkForStock());
	}

	public void setDeliveryModel(TableModel model) {
		deliveries.setModel(model);
	}

	public void updateUI() {
		warehouses.updateUI();
		deliveries.updateUI();
	}

	public Object[] getAllFreeSpaceLeafs() {
		List<Warehouse> l = new ArrayList<Warehouse>();
		Warehouse w = ((WarehouseNode) warehouses.getModel().getRoot()).getWarehouse();
		getFreeSpaceLeafs(w, l);
		return l.toArray();
	}

	private void getFreeSpaceLeafs(Warehouse warehouse, List<Warehouse> allFreeSpaceLeafs) {
		WarehouseNode node = warehouse.getNode();

		if (node.isLeaf()) {
			if (warehouse.getStock() - warehouse.getCapacity() != 0) {
				allFreeSpaceLeafs.add(warehouse);
			}
		} else {
			for (WarehouseNode w : node.getChildren().values()) {
				getFreeSpaceLeafs(w.getWarehouse(), allFreeSpaceLeafs);
			}
		}
	}

	public Object[] getAllLeafsWithStock() {
		List<Warehouse> l = new ArrayList<Warehouse>();
		Warehouse w = ((WarehouseNode) warehouses.getModel().getRoot()).getWarehouse();
		getLeafsWithStock(w, l);
		return l.toArray();
	}

	private void getLeafsWithStock(Warehouse w, List<Warehouse> l) {
		WarehouseNode n = w.getNode();
		if (n.isLeaf()) {
			if (w.getStock() != 0) {
				l.add(w);
			}
		} else {
			for (WarehouseNode child : n.getChildren().values()) {
				getLeafsWithStock(child.getWarehouse(), l);
			}
		}
	}

	private boolean checkForStock() {
		Object[] o = getAllLeafsWithStock();
		if (o.length == 0) {
			return false;
		}
		return true;
	}
}
