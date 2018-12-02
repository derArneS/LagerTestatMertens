package lager.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import lager.controller.Controller;
import lager.model.WarehouseTableModel;

@SuppressWarnings("serial")
public class View extends JFrame {
	private Controller controller;
	private JButton save = new JButton("Speichern");
	private JButton open = new JButton("Oeffnen");
	private JButton undo = new JButton("Undo");
	private JButton redo = new JButton("Redo");
	private JButton newWarehouse = new JButton("Neues Lager");
	private JButton delWarehouse = new JButton("Lager loeschen");
	private JButton newBooking = new JButton("Neue Lieferung");
	private JButton clearSelectionWarehouse = new JButton("Auswahl loeschen");
	private JPanel controls = new JPanel();
	private JPanel visualization = new JPanel();
	private JScrollPane warehousesPane = new JScrollPane();
	private JScrollPane bookingsPane = new JScrollPane();
	private JTable warehouses = new JTable();
	private JTable bookings = new JTable();

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
		controls.add(undo);
		controls.add(redo);
		controls.add(newWarehouse);
		controls.add(delWarehouse);
		controls.add(newBooking);

		warehousesPane.getViewport().add(warehouses);
		warehouses.setFillsViewportHeight(true);
		warehouses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bookingsPane.getViewport().add(bookings);
		bookings.setFillsViewportHeight(true);

		visualization.add(warehousesPane);
		visualization.add(clearSelectionWarehouse);
		visualization.add(bookingsPane);

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

		undo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		redo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		newWarehouse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (warehouses.getSelectionModel().isSelectionEmpty()) {
					JPanel optionPane = new JPanel();
					JTextField name = new JTextField();
					name.setPreferredSize(new Dimension(100, 50));
					JTextField capacity = new JTextField();
					capacity.setPreferredSize(new Dimension(100, 50));

					optionPane.add(new JLabel("Name:"));
					optionPane.add(name);
					optionPane.add(Box.createHorizontalStrut(15));
					optionPane.add(new JLabel("Kapazitaet:"));
					optionPane.add(capacity);

					try {
						int result = JOptionPane.showConfirmDialog(null, optionPane,
								"Bitte Name und Kapazitaet eingeben", JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							WarehouseTableModel model = (WarehouseTableModel) warehouses.getModel();
							controller.newWarehouse(name.getText(), Integer.valueOf(capacity.getText()));
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}
				} else {
					JPanel optionPane = new JPanel();
					JTextField name = new JTextField();
					name.setPreferredSize(new Dimension(100, 50));

					optionPane.add(new JLabel("Name:"));
					optionPane.add(name);

					try {
						int result = JOptionPane.showConfirmDialog(null, optionPane, "Bitte Name eingeben",
								JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							WarehouseTableModel model = (WarehouseTableModel) warehouses.getModel();
							controller.newWarehouse(name.getText(),
									model.getWarehouseAtRow(warehouses.getSelectedRow()));
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}

			}
		});

		delWarehouse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		newBooking.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		clearSelectionWarehouse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				warehouses.getSelectionModel().clearSelection();
			}
		});

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(controls);
		this.add(visualization);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void setWarehouseModel(TableModel model) {
		warehouses.setModel(model);
	}

	public void setBookingsModel(TableModel model) {
		bookings.setModel(model);
	}
}
