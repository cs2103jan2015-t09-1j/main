package sg.edu.nus.cs2103t.omnitask.data;

import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.item.Task;

public abstract class Data {

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
	 * 
	 * @author Sim Wei Kang
	 * 
	 * @param task A task object that contains all the user input attributes
	 * @return True if successful, false if overwise
	 */
	public abstract boolean addTask(Task task) throws TaskNoNameException,
			IOException;

	/** This method checks if the Task object is found in
	 * the Tasks list, and removes it if its found
	 * 
	 * @author Sim Wei Kang 
	 *  
	 * @param task The target Task to be removed
	 * @return True if successful
	 */
	public abstract boolean deleteTask(Task task);

	/** This method replicates the attributes of
	 * mutatorTask onto a Task object the Task object will then be saved into
	 * the Task list.
	 *   
	 * @author Sim Wei Kang
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

	/** This method rewrites the Tasks list with the Tasks list saved in the RedoStack.
	 *  
	 * @author Sim Wei Kang
	 * 
	 * @param void
	 * @return True if successful, false if otherwise.
	 */
	public abstract boolean redo();

	public void removeDataUpdatedListener(DataUpdatedListener listener) {
		dataUpdatedListeners.remove(listener);
	}

	public abstract ArrayList<Task> searchTask();

	/**This method rewrites the Tasks list with the Tasks list saved in the UndoState.
	 * 
	 * @author Sim Wei Kang
	 * 	  
	 * @param void
	 * @return True if successful, false if otherwise
	 */
	public abstract boolean undo();

	public abstract void updateTaskId();

	public abstract boolean changeStorageDirectory(String newDir);

	/**This method rewrites the target Task object's date attributes to null
	 * 
	 * @author Sim Wei Kang
	 * 
	 * @param taskToRemove A Task object that contains the id of the target Task in the Tasks list
	 * @return True if successful, false if otherwise
	 */
	public abstract boolean removeTaskDate(Task taskToRemoveDate);

}
