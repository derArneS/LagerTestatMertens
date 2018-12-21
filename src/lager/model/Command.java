package lager.model;

/**
 * Command des Command-Patterns
 */
public interface Command {
	public void exec();

	public Object[] undo();

}
