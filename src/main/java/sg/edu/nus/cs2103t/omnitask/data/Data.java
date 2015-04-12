package sg.edu.nus.cs2103t.omnitask.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.storage.IO;

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

	/*
	 * @author Sim Wei Kang
	 * 
	 * This method assigns a task ID and a UUID to a task object and saves it in the Tasks list
	 * 
	 * @param task A task object that contains all the user input attributes
	 * @return True if successful, false if overwise
	 */
	public abstract boolean addTask(Task task) throws TaskNoNameException,
			IOException;

	/*
	 * @author Sim Wei Kang 
	 * 
	 * This method checks if the Task object is found in
	 * the Tasks list, and removes it if its found
	 * 
	 * @param task The target Task to be removed
	 * @return True if successful
	 */
	public abstract boolean deleteTask(Task task);

	/*
	 * @author Sim Wei Kang 
	 * 
	 * This method replicates the attributes of
	 * mutatorTask onto a Task object the Task object will then be saved into
	 * the Task list.
	 * 
	 * @param mutatorTask A Task object that holds the data in which the user
	 * wants to edit
	 * 
	 * @return True if successful, false if otherwise.
	 */
	public abstract boolean editTask(Task mutatorTask);

	public abstract String getHelpDescriptors(String helpType, boolean miniMenu)
			throws IOException;

	public abstract Task getTask(int index);

	public abstract ArrayList<Task> getTasks();

	public abstract void notifyDataChanged();

	/*
	 * @author Sim Wei Kang
	 * 
	 * This method rewrites the Tasks list with the Tasks list saved in the RedoStack.
	 * 
	 * @param void
	 * @return True if successful, false if otherwise.
	 */
	public abstract boolean redo();

	public void removeDataUpdatedListener(DataUpdatedListener listener) {
		dataUpdatedListeners.remove(listener);
	}

	public abstract ArrayList<Task> searchTask();

	/*
	 * @author Sim Wei Kang
	 * 
	 * This method rewrites the Tasks list with the Tasks list saved in the UndoState.
	 * 
	 * @param void
	 * @return True if successful, false if otherwise
	 */
	public abstract boolean undo();

	public abstract void updateTaskId();

	public abstract boolean changeStorageDirectory(String newDir);

	/*
	 * @author Sim Wei Kang
	 * 
	 * This method rewrites the target Task object's date attributes to null
	 * 
	 * @param taskToRemove A Task object that contains the id of the target Task in the Tasks list
	 * @return True if successful, false if otherwise
	 */
	public abstract boolean removeTaskDate(Task taskToRemoveDate);

}
