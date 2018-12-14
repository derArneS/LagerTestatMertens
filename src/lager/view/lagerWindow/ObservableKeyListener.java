package lager.view.lagerWindow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextField;

public class ObservableKeyListener extends Observable implements KeyListener {
	private JTextField field;
	private String name;

	public ObservableKeyListener(JTextField field, String name) {
		super();
		this.field = field;
		this.name = name;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
		super.setChanged();
		super.notifyObservers();
	}

	public JTextField getTextField() {
		return field;
	}

	public String getName() {
		return name;
	}

}
