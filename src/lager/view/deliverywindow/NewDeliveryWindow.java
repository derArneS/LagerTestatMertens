package lager.view.deliverywindow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import lager.controller.Controller;
import lager.model.ObservableStack;
import lager.model.Warehouse;
import lager.view.View;
import lager.view.lagerwindow.ObservableKeyListener;

/**
 * Der JOptionPane Dialog um einen neue Buchung zum System hinzuzufügen
 */
public class NewDeliveryWindow {
	View view;

	public NewDeliveryWindow(View view) {
		this.view = view;
	}

	private String amountCheck = "DEFAULT";

	/**
	 * Öffnet die JOptionPane zur Eingabe des Umfangs der Buchung
	 * 
	 * @return Umfang der Buchung
	 */
	public int amountPane() {
		JPanel panel = new JPanel();
		ObserverButton go = new ObserverButton("Weiter");
		JButton back = new JButton("Zurueck");
		back.setEnabled(false);
		JButton cancel = new JButton("Abbrechen");
		JLabel amount = new JLabel("Menge:");
		JTextField userInput = new JTextField("");
		ObservableKeyListener oKLUserInput = new ObservableKeyListener(userInput, "MENGE");

		go.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor(go);
				w.dispose();
				amountCheck = "WEITER";
			}
		});

		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Window w = SwingUtilities.getWindowAncestor(go);
				w.dispose();
			}
		});

		Object[] options = { go, back, cancel };

		oKLUserInput.addObserver(go);
		userInput.addKeyListener(oKLUserInput);

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(amount);
		panel.add(userInput);
		userInput.setPreferredSize(new Dimension(75, 20));

		JOptionPane.showOptionDialog(view, panel, "Neue Lieferung", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (amountCheck.equals("WEITER")) {
			return Integer.valueOf(userInput.getText());
		}

		return -1;
	}

	private String bookingCheck = "DEFAULT";
	private int iteration = 1;
	private int amountLeft;
	private Stack<Integer> amountStack = new Stack<Integer>();

	/**
	 * Öffnet die JOptionPane zur Eingabe einer Teilbuchung
	 * 
	 * @param amount     Umfang der Kompletten Buchung
	 * @param controller
	 * @return auszuführende Aktion
	 */
	public String bookingPane(int amount, Controller controller) {
		JPanel panel = new JPanel();
		JPanel wrapper = new JPanel();
		JPanel innerPanel = new JPanel();
		JComboBox<Object> box = new JComboBox<Object>(view.getAllFreeSpaceLeafs());
		ObserverButton go = new ObserverButton("Weiter");
		ObserverButton back = new ObserverButton("Zurueck");
		ObservableComboBoxListener boxListener = new ObservableComboBoxListener(box);
		ObserverJSlider slider = new ObserverJSlider(0, 100, 100, amount);
		ObservableChangeListener sliderListener = new ObservableChangeListener(slider);
		JButton cancel = new JButton("Abbrechen");
		ObserverLabel currentAmount = new ObserverLabel("", amount);

		amountLeft = amount;
		slider.setIteration(iteration);
		box.setSelectedIndex(0);

		go.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				controller.addDelieveryEntry(slider.getCurrentPercentage(), iteration,
						(Warehouse) box.getSelectedItem(), slider.getCurrentAmount());

				amountStack.push(slider.getCurrentAmount());
				amountLeft -= slider.getCurrentAmount();
				currentAmount.setAmount(amountLeft);
				slider.setAmountLeft(amountLeft);
				box.updateUI();
				box.setSelectedIndex(0);

				iteration++;
				slider.setIteration(iteration);

				if (iteration == 6 || amountLeft == 0) {
					Window w = SwingUtilities.getWindowAncestor(go);
					w.dispose();
					bookingCheck = "WEITER";
				}
			}
		});

		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] array = controller.undoDeliveryEntry();
				slider.setValue((int) (double) array[0]);
				box.setSelectedItem(array[1]);
				amountLeft += amountStack.pop();
				currentAmount.setAmount(amountLeft);
				slider.setAmountLeft(amountLeft);
				box.updateUI();
				iteration--;
				slider.setIteration(iteration);
			}
		});

		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Window w = SwingUtilities.getWindowAncestor(go);
				w.dispose();
				bookingCheck = "CANCEL";
			}
		});

		Object[] options = { go, back, cancel };

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		slider.setFont(new Font("Arial", Font.ITALIC, 15));
		boxListener.addObserver(slider);
		box.addActionListener(boxListener);
		sliderListener.addObserver(currentAmount);
		sliderListener.addObserver(go);

		ObservableStack undoStack = controller.getUndoStack();
		undoStack.addObserver(back);
		slider.addChangeListener(sliderListener);
		wrapper.add(box);
		innerPanel.add(currentAmount);
		panel.add(wrapper);
		panel.add(slider);
		panel.add(innerPanel);
		panel.validate();

		JOptionPane.showOptionDialog(view, panel, "Neue Lieferung", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		return bookingCheck;
	}

}
