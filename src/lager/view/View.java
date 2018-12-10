package lager.view;

import java.awt.Dimension;
import java.awt.Window;
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
import javax.swing.SwingUtilities;
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
	private JButton newBooking = new JButton("Neue Lieferung");
	private JButton clearSelectionWarehouse = new JButton("Auswahl loeschen");
	private JButton changeName = new JButton("Name ändern");
	private JButton stock = new JButton("Bestand anzeigen");
	private JPanel controls = new JPanel();
	private JPanel visualization = new JPanel();
	private JScrollPane warehousesPane = new JScrollPane();
	private JScrollPane bookingsPane = new JScrollPane();
	private JTree warehouses = new JTree();
	private JTable bookings = new JTable();

	private String buttonCheck = "DEFAULT";

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
		controls.add(newBooking);
		controls.add(changeName);
		controls.add(stock);

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

		newWarehouse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (warehouses.getSelectionCount() == 0) {
					JOptionPane.showMessageDialog(null, "Kein Lager ausgewählt", "Kein Lager",
							JOptionPane.INFORMATION_MESSAGE);
				} else {

					buttonCheck = "DEFAULT";
					ObserverButton weiter = new ObserverButton("Weiter");
					weiter.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							Window w = SwingUtilities.getWindowAncestor(weiter);
							w.dispose();
							buttonCheck = "WEITER";
						}
					});

					ObserverButton back = new ObserverButton("Zurück");
					back.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							Window w = SwingUtilities.getWindowAncestor(back);
							w.dispose();
						}
					});

					ObserverButton[] options = { weiter, back };

					JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

					JPanel panelName = new JPanel();
					JLabel labelName = new JLabel("Name: ");
					labelName.setPreferredSize(new Dimension(75, 20));
					JTextField textFieldName = new JTextField(10);

					ObservableKeyListener oKLName = new ObservableKeyListener(textFieldName, "NAME");
					oKLName.addObserver(options[0]);
					textFieldName.addKeyListener(oKLName);

					panelName.add(labelName);
					panelName.add(textFieldName);
					panel.add(panelName);

					JPanel panelCapacity = new JPanel();
					JLabel labelCapacity = new JLabel("Kapazität: ");
					labelCapacity.setPreferredSize(new Dimension(75, 20));
					JTextField textFieldCapacity = new JTextField(10);

					ObservableKeyListener oKLCapacity = new ObservableKeyListener(textFieldCapacity, "CAPACITY");
					oKLCapacity.addObserver(options[0]);
					textFieldCapacity.addKeyListener(oKLCapacity);

					panelCapacity.add(labelCapacity);
					panelCapacity.add(textFieldCapacity);
					panel.add(panelCapacity);

					JOptionPane.showOptionDialog(View.this, panel, "Enter a Number", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, null);

					if (buttonCheck.equals("WEITER")) {
						controller.addWarehouse(
								((WarehouseNode) warehouses.getSelectionPath().getLastPathComponent()).getWarehouse(),
								textFieldName.getText(), Integer.valueOf(textFieldCapacity.getText()));
						warehouses.updateUI();
					}

				}
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
				controller.newBooking();
			}
		});

		clearSelectionWarehouse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});

		changeName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (warehouses.getSelectionCount() != 0) {
					String name = JOptionPane.showInputDialog(View.this, "Neuen Namen für das Lager eingeben",
							"Namen ändern", JOptionPane.QUESTION_MESSAGE);
					((WarehouseNode) warehouses.getSelectionPath().getLastPathComponent()).getWarehouse().setName(name);
				} else {
					JOptionPane.showMessageDialog(null, "Kein Lager ausgewählt", "Kein Lager",
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
					JOptionPane.showMessageDialog(null, "Kein Lager ausgewählt", "Kein Lager",
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
	}

	public void setBookingsModel(TableModel model) {
		bookings.setModel(model);
	}

	public void updateUI() {
		warehouses.updateUI();
	}
}
