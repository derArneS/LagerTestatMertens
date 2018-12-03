package lager.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

		bookingsPane.getViewport().add(bookings);
		bookings.setFillsViewportHeight(true);

		visualization.add(warehouses);
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

			}
		});

		delWarehouse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.removeWarehouse(
						((WarehouseNode) warehouses.getSelectionPath().getLastPathComponent()).getWarehouse());
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
