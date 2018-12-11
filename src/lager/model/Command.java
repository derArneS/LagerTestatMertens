package lager.model;

public interface Command {
	public void exec();

	public void undo();

}
