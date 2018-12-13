package lager.view;

import java.awt.Dimension;
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

public class Delivery {
	View view;

	public Delivery(View view) {
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

	public String bookingPane() {
		JPanel panel = new JPanel();
		ObserverButton weiter = new ObserverButton("Weiter");
		ObserverButton back = new ObserverButton("Zurueck");
		JButton cancel = new JButton("Abbrechen");

		weiter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor(weiter);
				w.dispose();
				bookingCheck = "WEITER";
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

		JComboBox<Object> box = new JComboBox<Object>(view.getAllLeafs());
		panel.add(box);

		JOptionPane.showOptionDialog(view, panel, "Neue Lieferung", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		return bookingCheck;
	}

}
