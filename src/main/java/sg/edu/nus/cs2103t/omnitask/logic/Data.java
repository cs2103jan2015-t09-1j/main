package sg.edu.nus.cs2103t.omnitask.logic;

import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public abstract class Data {
	
	public static class TaskNoNameException extends Exception {
		public TaskNoNameException() {
			super("Task must have a name.");
		}
	}
	
	public static interface DataUpdatedListener {
		void dataUpdated(ArrayList<Task> tasks);
	}
	
	protected ArrayList<DataUpdatedListener> dataUpdatedListeners;
	
	protected Data() {
		dataUpdatedListeners = new ArrayList<DataUpdatedListener>();
	}
	
	public void addDataUpdatedListener(DataUpdatedListener listener) {
		dataUpdatedListeners.add(listener);
	}
	
	public void removeDataUpdatedListener(DataUpdatedListener listener) {
		dataUpdatedListeners.remove(listener);
	}
	
	public abstract void notifyDataChanged();
	
	public abstract ArrayList<Task> getTasks();

	public abstract boolean addTask(Task task) throws TaskNoNameException, IOException;

	public abstract boolean deleteTask(Task task);

	public abstract boolean editTask(Task task);
	
	public abstract ArrayList<Task> searchTask();

	public abstract String getHelpDescriptors(String helpType) throws IOException;
	
}
