package lager.view.deliveryWindow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import lager.view.View;
import lager.view.lagerWindow.ObservableKeyListener;

public class NewDeliveryWindow {
	View view;

	public NewDeliveryWindow(View view) {
		this.view = view;
	}

	private String amountCheck = "DEFAULT";

	public int amountPane() {
		JPanel panel = new JPanel();
		ObserverButton weiter = new ObserverButton("Weiter");
		JButton back = new JButton("Zurueck");
		back.setEnabled(false);
		JButton cancel = new JButton("Abbrechen");
		JLabel amount = new JLabel("Menge:");
		JTextField userInput = new JTextField("");
		ObservableKeyListener oKLUserInput = new ObservableKeyListener(userInput, "MENGE");

		weiter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor(weiter);
				w.dispose();
				amountCheck = "WEITER";
			}
		});

		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Window w = SwingUtilities.getWindowAncestor(weiter);
				w.dispose();
			}
		});

		Object[] options = { weiter, back, cancel };

		oKLUserInput.addObserver(weiter);
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

	public String bookingPane(int amount) {
		JPanel panel = new JPanel();
		JPanel wrapper = new JPanel();
		JPanel innerPanel = new JPanel();
		JComboBox<Object> box = new JComboBox<Object>(view.getAllLeafs());
		ObserverButton weiter = new ObserverButton("Weiter");
		ObserverButton back = new ObserverButton("Zurueck");
		box.setSelectedIndex(0);
		ObservableComboBoxListener boxListener = new ObservableComboBoxListener(box);
		ObserverJSlider slider = new ObserverJSlider(0, 100, 100, amount);
		ObservableChangeListener sliderListener = new ObservableChangeListener(slider);
		JButton cancel = new JButton("Abbrechen");
		ObserverLabel currentAmount = new ObserverLabel("");

		weiter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (iteration == 5) {
					Window w = SwingUtilities.getWindowAncestor(weiter);
					w.dispose();
					bookingCheck = "WEITER";
				} else {
					iteration++;
				}
			}
		});

		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Window w = SwingUtilities.getWindowAncestor(weiter);
				w.dispose();
				bookingCheck = "CANCEL";
			}
		});

		Object[] options = { weiter, back, cancel };

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.setPreferredSize(new Dimension(400, 100));
		wrapper.setPreferredSize(new Dimension(250, 50));

		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		slider.setFont(new Font("Arial", Font.ITALIC, 15));
		boxListener.addObserver(slider);
		box.addActionListener(boxListener);
		sliderListener.addObserver(currentAmount);
		slider.addChangeListener(sliderListener);
		wrapper.add(box);
		innerPanel.add(currentAmount);
		panel.add(wrapper);
		panel.add(slider);
		panel.add(innerPanel);

		JOptionPane.showOptionDialog(view, panel, "Neue Lieferung", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		return bookingCheck;
	}

}
