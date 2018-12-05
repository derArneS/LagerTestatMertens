package lager.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;

import lager.controller.Controller;
import lager.model.WarehouseNode;

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
	private JTree warehouses = new JTree();
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

		warehouses.setRootVisible(false);

		warehousesPane.getViewport().add(warehouses);
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
				Object[] options1 = { "Weiter", "Zurück" };

				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

				JPanel panelName = new JPanel();
				JLabel labelName = new JLabel("Name: ");
				labelName.setPreferredSize(new Dimension(75, 20));
				JTextField textFieldName = new JTextField(10);
				ObservableKeyListener oKLName = new ObservableKeyListener();
				oKLName.addObserver(null);
				textFieldName.addKeyListener(oKLName);

				panelName.add(labelName);
				panelName.add(textFieldName);
				panel.add(panelName);

				JPanel panelCapacity = new JPanel();
				JLabel labelCapacity = new JLabel("Kapazität: ");
				labelCapacity.setPreferredSize(new Dimension(75, 20));
				JTextField textFieldCapacity = new JTextField(10);
				panelCapacity.add(labelCapacity);
				panelCapacity.add(textFieldCapacity);
				panel.add(panelCapacity);

				int result = JOptionPane.showOptionDialog(null, panel, "Enter a Number", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options1, null);
				if (result == JOptionPane.OK_OPTION) {
				}

//				controller.addWarehouse(
//						((WarehouseNode) warehouses.getSelectionPath().getLastPathComponent()).getWarehouse());
//				warehouses.updateUI();
			}
		});

		delWarehouse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.removeWarehouse(
						((WarehouseNode) warehouses.getSelectionPath().getLastPathComponent()).getWarehouse());
				warehouses.updateUI();
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
	}

	public void setBookingsModel(TableModel model) {
		bookings.setModel(model);
	}

	public void updateUI() {
		warehouses.updateUI();
	}
}
