package lager.view.outputwindow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Implementierung eines KeyListeners und Erweiterung eines Observables
 */
public class ObservableKeyListener extends Observable implements KeyListener {
	public ObservableKeyListener() {
		super();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		setChanged();
		notifyObservers();
	}

	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
		setChanged();
		notifyObservers();
	}

	public void update() {
		setChanged();
		notifyObservers();
	}

}
