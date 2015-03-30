package sg.edu.nus.cs2103t.omnitask.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.Logger;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.storage.IO;
import sg.edu.nus.cs2103t.omnitask.storage.IOJSONImpl;

public class DataImpl extends Data {

	private static DataImpl data;

	private ArrayList<Task> tasks;
	
	private Stack<ArrayList<Task>> saveState;
	
	protected IO io;

	private boolean inited;

	public static DataImpl GetSingleton() {
		if (data == null) {
			data = new DataImpl();
		}

		return data;
	}

	private DataImpl() {
		super();
	}

	public DataImpl init(IO io) throws IOException {
		if (inited) {
			return this;
		}

		this.io = io;
		tasks = io.readFromFile();

		inited = true;

		return this;
	}

	private void assertInited() {
		assert inited;
	}

	@Override
	public ArrayList<Task> getTasks() {
		assertInited();

		return tasks;
	}
	
	public Stack<ArrayList<Task>> getSaveState() {
		return saveState;
	}

	// Get new "viewing" taskId which can be used for a new task
	private long getNewId() {
		assertInited();

		long taskId = 1;
		ArrayList<Task> tasks = getTasks();
		if (tasks.size() > 0) {
			taskId = tasks.get(tasks.size() - 1).getId() + 1;
		}

		return taskId;
	}

	// Not thread-safe
	@Override
	public boolean addTask(Task task) throws TaskNoNameException, IOException {
		assertInited();

		// Create new task object
		if (task.getName().trim().isEmpty()) {
			throw new TaskNoNameException();
		}

		// When adding task, always get new id
		task.setId(getNewId());

		// Assign randomUUID to task if it is null
		if (task.getUuid() == null) {
			task.setUuid(UUID.randomUUID());
		}

		//saveState.push(getTasks());
		// Add the task to our "local cache"
		tasks.add(task);

		// Commit it to storage
		try {
			io.saveToFile(tasks);
		} catch (IOException ex) {
			// Reverse change
			tasks.remove(tasks.size() - 1);

			throw ex;
		}

		notifyDataChanged();

		return true;
	}

	@Override
	public boolean deleteTask(Task taskToRemove) {
		assertInited();
		//saveState.push(getTasks());

		int indexToRemove = -1;

		if (tasks.contains(taskToRemove)) {
			indexToRemove = tasks.indexOf(taskToRemove);
			tasks.remove(taskToRemove);
		}

		// Commit it to storage
		try {
			io.saveToFile(tasks);
			updateTaskId();
		} catch (IOException ex) {
			// TODO: Handle error
			ex.printStackTrace();
			printError("IO Exception");

			// Reverse change
			tasks.add(indexToRemove, taskToRemove);

			return false;
		}

		notifyDataChanged();

		return true;
	}

	// Reassign taskId to the tasks arraylist
	private void updateTaskId() {
		assertInited();

		for (int i = 0; i < tasks.size(); i++) {
			tasks.get(i).setId(i + 1);
		}
	}

	@Override
	public boolean editTask(Task task) {
		assertInited();
		//saveState.push(getTasks());

		int taskIdToUpdate = -1;
		String tmpTaskName = "";

		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getId() == task.getId()) {
				// store the task name from the file in a variable incase need
				// to revert below
				tmpTaskName = tasks.get(i).getName();
				taskIdToUpdate = i;
			}
		}

		if (taskIdToUpdate != -1) {
			// Commit it to storage
			try {
				io.saveToFile(tasks);
			} catch (IOException ex) {
				// TODO: Handle error
				ex.printStackTrace();
				printError("IO Exception");

				// Reverse change
				// do a reverse of update
				tasks.get(taskIdToUpdate).setName(tmpTaskName);

				return false;
			}
		} else {
			return false;
		}

		notifyDataChanged();

		return true;
	}

	private void printError(String msg) {
		System.err.println(DateTime.now() + ": " + msg);
		Logger.writeError(msg);
	}

	// Only in-charge of fetching full task list from the storage and pass it to
	// CommandSearchImpl for processing
	@Override
	public ArrayList<Task> searchTask() {
		assertInited();

		ArrayList<Task> fullTaskList = new ArrayList<Task>();

		try {
			fullTaskList = io.readFromFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			printError("Unable to read from file @ dataImpl search function! ");
		}
		// make sure the search key is not empty if its empty searchTaskResult
		// will have size of 0

		return fullTaskList;
	}

	@Override
	public void notifyDataChanged() {
		for (DataUpdatedListener listener : dataUpdatedListeners) {
			listener.dataUpdated(tasks);
		}
	}

}
