package lager.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;

import lager.controller.Controller;
import lager.model.Warehouse;
import lager.model.WarehouseNode;

/**
 * View des MVC-Patterns
 */
@SuppressWarnings("serial")
public class View extends JFrame {
	private Controller controller;
	private JButton save = new JButton("Speichern");
	private JButton open = new JButton("�ffnen");
	private JButton newWarehouse = new JButton("Neues Lager");
	private JButton delWarehouse = new JButton("Lager l�schen");
	private JButton newDelivery = new JButton("Neue Lieferung");
	private JButton newOutput = new JButton("Neue Auslieferung");
	private JButton clearSelectionWarehouse = new JButton("Lagerauswahl l�schen");
	private JButton changeName = new JButton("Name �ndern");
	private JButton stock = new JButton("Bestand und Buchungen anzeigen");
	private JButton listDetails = new JButton("Buchungsdetails anzeigen");
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

	/**
	 * Aufbauen und anzeigen der View Hinzuf�gen der ActionListener
	 */
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
		visualization.add(listDetails);

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
					System.out.println("Sie haben diese Datei g�ffnet:" + chooser.getSelectedFile().getName());
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
					JOptionPane.showMessageDialog(View.this, "Kein Lager ausgew�hlt", "Kein Lager",
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
					String name = JOptionPane.showInputDialog(View.this, "Neuen Namen f�r das Lager eingeben",
							"Namen �ndern", JOptionPane.QUESTION_MESSAGE);
					if (name != null) {
						((WarehouseNode) warehouses.getSelectionPath().getLastPathComponent()).getWarehouse()
								.setName(name);
					}
				} else {
					JOptionPane.showMessageDialog(View.this, "Kein Lager ausgew�hlt", "Kein Lager",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		stock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (warehouses.getSelectionCount() != 0) {
					Warehouse w = ((WarehouseNode) warehouses.getSelectionPath().getLastPathComponent()).getWarehouse();
					if (w.getNode().isLeaf()) {
						Object[][] o1 = controller.getInputDataForWarehouse(w);
						Object[][] o2 = controller.getOutputDataForWarehouse(w);

						JPanel panel = new JPanel();
						JScrollPane scrollPane1 = new JScrollPane();
						JScrollPane scrollPane2 = new JScrollPane();
						panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
						panel.add(new JLabel("Aktueller Bestand: " + w.getChildStock()));

						if (o1 != null) {
							String[] columnNames = { "Nr.", "Menge", "Antei 1", "Lager 1", "Anteil 2", "Lager 2",
									"Anteil 3", "Lager 3", "Anteil 4", "Lager 4", "Anteil 5", "Lager 5",
									"Summe Anteile", "Datum" };

							TableModel dataModel = new AbstractTableModel() {

								@Override
								public String getColumnName(int columnIndex) {
									return columnNames[columnIndex];
								}

								@Override
								public Object getValueAt(int rowIndex, int columnIndex) {
									return o1[rowIndex][columnIndex];
								}

								@Override
								public int getRowCount() {
									return o1.length;
								}

								@Override
								public int getColumnCount() {
									return o1[0].length;
								}
							};

							JTable delivery = new JTable(dataModel);
							scrollPane1.getViewport().add(delivery);
							scrollPane1.setPreferredSize(new Dimension(1500, 250));
							panel.add(scrollPane1);
						}

						if (o2 != null) {
							String[] columnNames = { "Warenlager", "Menge", "Datum" };

							TableModel dataModel = new AbstractTableModel() {

								@Override
								public String getColumnName(int columnIndex) {
									return columnNames[columnIndex];
								}

								@Override
								public Object getValueAt(int rowIndex, int columnIndex) {
									return o2[rowIndex][columnIndex];
								}

								@Override
								public int getRowCount() {
									return o2.length;
								}

								@Override
								public int getColumnCount() {
									return o2[0].length;
								}
							};

							JTable output = new JTable(dataModel);
							scrollPane2.getViewport().add(output);
							panel.add(scrollPane2);
						}

						JOptionPane.showMessageDialog(View.this, panel, "Bestand", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(View.this, "Aktueller Bestand:" + w.getChildStock(), "Bestand",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(View.this, "Keine Buchung ausgew�hlt", "Keine Buchung",
							JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});

		listDetails.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (deliveries.getSelectedRow() != -1) {
					JOptionPane.showMessageDialog(View.this,
							deliveries.getModel().getValueAt(deliveries.getSelectedRow(), 13), "Buchungsdetails",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(View.this, "Kein Lager ausgew�hlt", "Kein Lager",
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
		toolTipps();
	}

	public void setWarehouseModel(TreeModel model) {
		warehouses.setModel(model);
		newOutput.setEnabled(checkForStock());
	}

	public void setDeliveryModel(TableModel model) {
		deliveries.setModel(model);
	}

	/**
	 * Aktualisiert die UI der Models
	 */
	public void updateUI() {
		warehouses.updateUI();
		deliveries.updateUI();
	}

	/**
	 * Gibt alle Bl�tter zur�ck, welche noch Platz im Bestand haben
	 * 
	 * @return
	 */
	public Object[] getAllFreeSpaceLeafs() {
		List<Warehouse> l = new ArrayList<Warehouse>();
		Warehouse w = ((WarehouseNode) warehouses.getModel().getRoot()).getWarehouse();
		getFreeSpaceLeafs(w, l);
		return l.toArray();
	}

	/**
	 * Gibt rekursiv alle Bl�tter zur�ck, welche sich unter dem �bergebenen Lager
	 * befinden und Platz im Bestand haben
	 * 
	 * @param warehouse
	 * @param allFreeSpaceLeafs
	 */
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

	/**
	 * Gibt alle Bl�tter zur�ck, welche einen Bestand haben
	 * 
	 * @return
	 */
	public Object[] getAllLeafsWithStock() {
		List<Warehouse> l = new ArrayList<Warehouse>();
		Warehouse w = ((WarehouseNode) warehouses.getModel().getRoot()).getWarehouse();
		getLeafsWithStock(w, l);
		return l.toArray();
	}

	/**
	 * Gibt rekursiv alle Bl�tter zur�ck, welche sich unter einem Lager befinden und
	 * die einen Bestand haben
	 * 
	 * @param w
	 * @param l
	 */
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

	/**
	 * �berpr�ft, ob es mindestens ein Lager mit Bestand gibt
	 * 
	 * @return
	 */
	private boolean checkForStock() {
		Object[] o = getAllLeafsWithStock();
		if (o.length == 0) {
			return false;
		}
		return true;
	}

	/**
	 * F�gt ToolTips hinzu
	 */
	private void toolTipps() {
		ToolTipManager.sharedInstance().setDismissDelay(10000);

		save.setToolTipText("�ffnet den Dateibrowser zum Speichern der aktuellen Daten.");
		open.setToolTipText("�ffnet den Dateibrowser zum �ffnen einer vorhandenen Lagersystemdatei.");
		newWarehouse.setToolTipText("�ffnet den Dialog zur Erstellung eines Lagers. "
				+ "Wenn kein Lager ausgew�hlt ist, wird das neue Lager als Lager der obersten Kategorie erstellt. "
				+ "Ist ein Lager in der Hierachie ausgew�hlt, so wird das neue Lager als Kind des ausgew�hlten Lagers erstellt");
		delWarehouse
				.setToolTipText("L�scht das ausgew�hlte Lager. ACHTUNG: Es k�nnen nur leere Lager entfernt werden.");
		newDelivery.setToolTipText("�ffnet den Dialog um eine neue Zulieferung zu erstellen.");
		newOutput.setToolTipText("�ffnet den Dialog um eine neue Auslieferung zu erstellen.");
		clearSelectionWarehouse.setToolTipText("L�scht die aktuelle Auswahl, so das kein Lager mehr ausgew�hlt ist.");
		changeName.setToolTipText("�ffnet den Dialog um den Namen des ausgew�hlten Lagers zu �ndern.");
		stock.setToolTipText(
				"�ffnet einen Dialog, welcher den aktuellen Bestand und alle Buchungen, die das ausgew�hlte Lager betreffen, anzeigt. "
						+ "ACHTUNG: Wenn das ausgewh�lte Lager weitere Lager enth�lt, so wird lediglich der aktuelle aussummierte Bestand aller Kinder angezeigt.");
		listDetails.setToolTipText("Zeigt die Buchungsdetails der ausgew�hlten Buchung an.");

	}
}
