package sg.edu.nus.cs2103t.omnitask.data;

import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.item.Task;

public abstract class Data {

	//@author A0111795A
	public static interface DataUpdatedListener {
		void dataUpdated(
				ArrayList<Task> tasks,
				javafx.collections.ListChangeListener.Change<? extends Task> changes);
	}

	@SuppressWarnings("serial")
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

	/** This method assigns a task ID and a UUID to a task object and saves it in the Tasks list
	 * <p>
	 * 
	 * @param task A task object that contains all the user input attributes
	 * @return True if successful, false if overwise
	 */
	public abstract boolean addTask(Task task) throws TaskNoNameException,
			IOException;

	/** This method checks if the Task object is found in
	 * the Tasks list, and removes it if its found
	 * <p>
	 * 
	 *  
	 * @param task The target Task to be removed
	 * @return True if successful
	 */
	public abstract boolean deleteTask(Task task);

	/** This method replicates the attributes of
	 * mutatorTask onto a Task object the Task object will then be saved into
	 * the Task list.
	 * <p>
	 *   
	 * @param mutatorTask A Task object that holds the data in which the user
	 * wants to edit
	 * 
	 * @return True if successful, false if otherwise.
	 */
	public abstract boolean editTask(Task mutatorTask);
	
	public abstract String getHelpDescriptors(String helpType, boolean miniMenu)
			throws IOException;

	/**
	 * This method returns a Task object that exists in index-position of the Tasks list
	 * <p>
	 * 
	 * @param index
	 * @return Task A Task object
	 */
	public abstract Task getTask(int index);

	/**
	 * This method returns the a clone of the Tasks list
	 * <p>
	 * 
	 * @return Tasks A list containing task objects
	 */
	public abstract ArrayList<Task> getTasks();

	/**
	 * This method updates the tasks list
	 */
	public abstract void notifyDataChanged();

	/** This method rewrites the Tasks list with the Tasks list saved in the RedoStack.
	 *  <p>
	 * 
	 * @param void
	 * @return True if successful, false if otherwise.
	 */
	public abstract boolean redo();

	//@author A0111795A
	public void removeDataUpdatedListener(DataUpdatedListener listener) {
		dataUpdatedListeners.remove(listener);
	}

	//@author A0119742A
	public abstract ArrayList<Task> searchTask();

	/**This method rewrites the Tasks list with the Tasks list saved in the UndoState.
	 * <p>
	 * 
	 * @author A0119742A
	 * 	  
	 * @param void
	 * @return True if successful, false if otherwise
	 */
	public abstract boolean undo();

	/**
	 * This method updates the task ID of all the tasks in the tasks list
	 * <p>
	 */
	public abstract void updateTaskId();

	/**
	 * This methods changes the storage directory of OmniTask
	 * <p>
	 * 
	 * @param newDir New directory path
	 * @return True if successful, false if otherwise.
	 */
	public abstract boolean changeStorageDirectory(String newDir);

	/**This method rewrites the target Task object's date attributes to null
	 * <p>
	 *  
	 * @param taskToRemove A Task object that contains the id of the target Task in the Tasks list
	 * @return True if successful, false if otherwise
	 */
	public abstract boolean removeTaskDate(Task taskToRemoveDate);

}
