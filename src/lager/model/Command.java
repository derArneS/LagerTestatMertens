package lager.model;

public interface Command {
	public void exec();

	public Object[] undo();

}
