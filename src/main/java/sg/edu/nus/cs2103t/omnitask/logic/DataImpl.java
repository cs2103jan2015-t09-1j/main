package sg.edu.nus.cs2103t.omnitask.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.Logger;
import sg.edu.nus.cs2103t.omnitask.logic.Data.DataUpdatedListener;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.storage.IO;
import sg.edu.nus.cs2103t.omnitask.storage.IOJSONImpl;
import sg.edu.nus.cs2103t.omnitasks.command.Utils;

public class DataImpl extends Data {

	private static DataImpl data;

	private ObservableList<Task> tasks;

	private SortedList<Task> sortedTasks;

	private Stack<ArrayList<Task>> previousState;

	private Stack<ArrayList<Task>> redoStack;

	private ArrayList<Task> redoList;

	private ArrayList<Task> currentList;

	private ArrayList<Task> previousList;

	protected IO io;

	private boolean inited;

	private ListChangeListener<Task> tasksChangeListener = new ListChangeListener<Task>() {

		@Override
		public void onChanged(
				javafx.collections.ListChangeListener.Change<? extends Task> changes) {
			for (DataUpdatedListener listener : dataUpdatedListeners) {
				listener.dataUpdated(getTasks(), changes);
			}
		}

	};

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
		tasks = FXCollections.observableArrayList();
		sortedTasks = tasks.sorted(Task.taskSorterComparator);
		sortedTasks.addListener(tasksChangeListener);
		tasks.addAll(io.readFromFile());
		redoStack = new Stack<ArrayList<Task>>();
		previousState = new Stack<ArrayList<Task>>();
		inited = true;

		return this;
	}

	private void assertInited() {
		assert inited;
	}

	@Override
	public ArrayList<Task> getTasks() {
		assertInited();

		ArrayList<Task> clonedTasks = new ArrayList<Task>();
		for (Task task : sortedTasks) {
			clonedTasks.add(task.clone());
		}

		return clonedTasks;
	}
	
	@Override
	public Task getTask(int index) {
		return sortedTasks.get(index).clone();
	}

	private Stack<ArrayList<Task>> getPreviousState() {
		previousList = getTasks();
		previousState.push((ArrayList<Task>) previousList.clone());

		return previousState;
	}

	// Get new "viewing" taskId which can be used for a new task
	private long getNewId() {
		assertInited();

		long taskId = 0;
		for (Task task : tasks) {
			if (task.getId() > taskId) {
				taskId = task.getId();
			}
		}

		return ++taskId;
	}

	// Not thread-safe
	@Override
	public boolean addTask(Task task) throws TaskNoNameException, IOException {
		assertInited();
		getPreviousState();
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

		// Add the task to our "local cache"
		tasks.add(task);

		// Commit it to storage
		try {
			io.saveToFile(tasks.subList(0, tasks.size()));
			updateTaskId();
		} catch (IOException ex) {
			// Reverse change
			tasks.remove(tasks.size() - 1);

			throw ex;
		}
		
		// Clear redoStack because no undo was made.
		redoStack.clear();

		return true;
	}

	@Override
	public boolean deleteTask(Task taskToRemove) {
		assertInited();
		getPreviousState();

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
		
		// Clear redoStack because no undo was made.
		redoStack.clear();

		return true;
	}

	// Reassign taskId to the tasks arraylist
	private void updateTaskId() {
		assertInited();

		ArrayList<Task> tmpTasks = new ArrayList<Task>();
		for (int i = 0; i < sortedTasks.size(); i++) {
			Task task = sortedTasks.get(i);
			task.setId(i + 1);
			tmpTasks.add(task);
		}

		tasks.setAll(tmpTasks);
		try {
			io.saveToFile(tasks);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean editTask(Task mutatorTask) {
		assertInited();
		getPreviousState();

		int taskIdToUpdate = -1;
		String tmpTaskName = "";
		Task foundTask = new Task();

		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getId() == (mutatorTask.getId())) {
				
				foundTask = tasks.get(i);
				Utils.editAttributes(foundTask, mutatorTask);
				// store the task name from the file in a variable incase need
				// to revert below
				tmpTaskName = tasks.get(i).getName();
				taskIdToUpdate = i;
			}
		}

		// Replace the task object in arraylist with the new object
		tasks.set(taskIdToUpdate, foundTask);

		if (taskIdToUpdate != -1) {
			// Commit it to storage
			try {
				io.saveToFile(tasks);
				updateTaskId();
			} catch (IOException ex) {
				// TODO: Handle error
				ex.printStackTrace();
				printError("IO Exception");

				// Reverse change
				// do a reverse of update
				tasks.get(taskIdToUpdate).setName(tmpTaskName);

				return false;
			}
		}

		// Clear redoStack because no undo was made.
		redoStack.clear();

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
		ArrayList<Task> tmpTasks = new ArrayList<Task>();
		for (int i = 0; i < sortedTasks.size(); i++) {
			Task task = sortedTasks.get(i);
			tmpTasks.add(task);
		}
		tasks.setAll(tmpTasks);
	}

	@Override
	public String getHelpDescriptors(String helpType,boolean miniMenu) throws IOException {
		return io.readFromHelpFile(helpType,miniMenu);
	}
	
	public boolean undo() {
		if (previousState.empty()) {
			return false;
		} else {

			// Saving current Tasks list into redoList
			redoList = getTasks();
			// Pushing to redoStack ("future state") to save current state
			redoStack.push(redoList);

			// Getting previous state
			previousList = previousState.peek();
			// Overwrite current state with previous state
			tasks.setAll(previousState.pop());

			try {
				io.saveToFile(tasks);
			} catch (IOException ex) {
				// TODO: Handle error
				ex.printStackTrace();
				printError("IO Exception");
				return false;
			}

			return true;

		}
	}

	@Override
	public boolean redo() {
		if (redoStack.empty()) {
			return false;
		} else {
			previousList = getTasks();
			// Pushing to previous state to save previous state
			previousState.push(previousList);
			// Overwrite current state with "future" state
			tasks.setAll(redoStack.pop());

			try {
				io.saveToFile(tasks);
			} catch (IOException ex) {
				// TODO: Handle error
				ex.printStackTrace();
				printError("IO Exception");
				return false;
			}

			return true;
		}

	}
}
