package lager.view.outputwindow;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import lager.controller.Controller;
import lager.view.View;

/**
 * Der JOptionPane Dialog um einen neue Auslieferung zum System hinzuzufügen
 */
public class NewOutputWindow {
	private View view;

	public NewOutputWindow(View view) {
		this.view = view;
	}

	/**
	 * Öffnet die JOptionPane zur Eingabe des Umfangs der Auslieferung
	 */
	public void outputPane(Controller controller) {
		JButton finished = new JButton("Fertig");
		JPanel panel = new JPanel();
		JPanel innerPanel = new JPanel();
		JComboBox<Object> box = new JComboBox<Object>(view.getAllLeafsWithStock());
		JLabel label = new JLabel("Auslieferungsmenge");
		JTextField input = new JTextField();
		ObservableComboBoxListener boxListener = new ObservableComboBoxListener(box);
		ObservableKeyListener keyListener = new ObservableKeyListener();
		ObserverButton go = new ObserverButton("Weiter", input, box);

		Object[] options = { go, finished };
		box.addActionListener(boxListener);
		input.addKeyListener(keyListener);
		boxListener.addObserver(go);
		keyListener.addObserver(go);

		box.setSelectedIndex(0);

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));

		panel.add(box);
		innerPanel.add(label);
		innerPanel.add(input);
		panel.add(innerPanel);

		go.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.newOutput(box.getSelectedItem(), Integer.parseInt(input.getText()));
				input.setText("");
				KeyListener[] kL = input.getKeyListeners();
				ObservableKeyListener listener = (ObservableKeyListener) kL[0];
				listener.update();
				box.updateUI();
			}
		});

		finished.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor(finished);
				w.dispose();
			}
		});

		go.addActionListener(new ObservableComboBoxListener(box));

		JOptionPane.showOptionDialog(view, panel, "Neue Auslieferung", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	}
}
