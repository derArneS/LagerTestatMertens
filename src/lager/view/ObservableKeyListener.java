package lager.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

public class ObservableKeyListener extends Observable implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
		super.setChanged();
		super.notifyObservers();
	}

}
