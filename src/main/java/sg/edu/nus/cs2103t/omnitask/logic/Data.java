package sg.edu.nus.cs2103t.omnitask.logic;

import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.Task;

public abstract class Data {

	public static interface DataUpdatedListener {
		void dataUpdated(
				ArrayList<Task> tasks,
				javafx.collections.ListChangeListener.Change<? extends Task> changes);
	}

	public static class TaskNoNameException extends Exception {
		public TaskNoNameException() {
			super("Task must have a name.");
		}
	}

	protected ArrayList<DataUpdatedListener> dataUpdatedListeners;

	protected Data() {
		dataUpdatedListeners = new ArrayList<DataUpdatedListener>();
	}

	public void addDataUpdatedListener(DataUpdatedListener listener) {
		dataUpdatedListeners.add(listener);
	}

	public abstract boolean addTask(Task task) throws TaskNoNameException,
			IOException;

	public abstract boolean deleteTask(Task task);

	public abstract boolean editTask(Task mutatorTask);

	public abstract String getHelpDescriptors(String helpType, boolean miniMenu)
			throws IOException;

	public abstract Task getTask(int index);

	public abstract ArrayList<Task> getTasks();

	public abstract void notifyDataChanged();

	public abstract boolean redo();

	public void removeDataUpdatedListener(DataUpdatedListener listener) {
		dataUpdatedListeners.remove(listener);
	}

	public abstract ArrayList<Task> searchTask();

	public abstract boolean undo();

}
