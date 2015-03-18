package sg.edu.nus.cs2103t.omnitask.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.Logger;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.storage.IO;
import sg.edu.nus.cs2103t.omnitask.storage.IOJSONImpl;

import java.util.UUID;

import org.joda.time.DateTime;

public class DataImpl extends Data {

	private File storageFile;

	private ArrayList<Task> tasks;

	protected IO io;

	public DataImpl(File storageFile) throws IOException {
		this.storageFile = storageFile;

		this.io = new IOJSONImpl(this.storageFile);
		this.tasks = io.readFromFile();
	}

	@Override
	public ArrayList<Task> getTasks() {
		return tasks;
	}

	// Get new "viewing" taskId which can be used for a new task
	private long getNewId() {
		long taskId = 1;
		ArrayList<Task> tasks = getTasks();
		if (tasks.size() > 0) {
			taskId = tasks.get(tasks.size() - 1).getId() + 1;
		}

		return taskId;
	}

	// Not thread-safe
	@Override
	public Task addTask(CommandInput commandInput) {
		// Create new task object
		Task task = new Task();
		if (commandInput.getName().isEmpty()) {
			return null;
		}
		
		// code waiting for Recurring task implementation
		// if(commandInput.isRecurrence()){
		// task.setRecurrence(1);
		// addAttributes(commandInput, task);
		// } else

		// add id, uuid, name, priority, start&end date to task
		addAttributes(commandInput, task);

		// Add the task to our "local cache"
		tasks.add(task);

		// Commit it to storage
		try {
			io.saveToFile(tasks);
		} catch (IOException ex) {
			// TODO: Handle error
			ex.printStackTrace();
			printError("IO Exception");

			// Reverse change
			tasks.remove(tasks.size() - 1);

			return null;
		}

		return task;
	}

	private void addAttributes(CommandInput commandInput, Task task) {
		UUID uuid = UUID.randomUUID();
		task.setUuid(uuid);
		task.setId(getNewId());
		task.setName(commandInput.getName());
		task.setPriority(commandInput.getPriority());
		task.setStartDate(commandInput.getStartDate());
		task.setEndDate(commandInput.getEndDate());
	}

	@Override
	public boolean deleteTask(CommandInput commandInput) {
		Task taskToRemove = null;
		int indexToRemove = -1;

		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getId() == commandInput.getId()) {
				taskToRemove = tasks.remove(i);
				indexToRemove = i;
			}
		}

		updateTaskId(); // Reassign taskId to the tasks Arraylist

		if (taskToRemove != null) {
			// Commit it to storage
			try {
				io.saveToFile(tasks);
			} catch (IOException ex) {
				// TODO: Handle error
				ex.printStackTrace();
				printError("IO Exception");

				// Reverse change
				tasks.add(indexToRemove, taskToRemove);

				return false;
			}
		} else {
			return false;
		}

		return true;
	}

	// Reassign taskId to the tasks arraylist
	private void updateTaskId() {
		for (int i = 0; i < tasks.size(); i++) {
			tasks.get(i).setId(i + 1);
		}
	}

	@Override
	public boolean editTask(CommandInput commandInput) {
		// lacking startDate, endDate, startTime, endTime
		int taskIdToUpdate = -1;
		String tmpTaskName = "";

		for (int i = 0; i < tasks.size(); i++) {

			if (tasks.get(i).getId() == commandInput.getId()) {
				// store the task name from the file in a variable incase need

				// to revert below
				tmpTaskName = tasks.get(i).getName();

				// edit name, priority, start&end date
				editAttributes(commandInput, i);

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

		return true;
	}

	// edit name, priority, start&end date
	private void editAttributes(CommandInput commandInput, int i) {
		if (!commandInput.getName().equals("")) {
			tasks.get(i).setName(commandInput.getName());
		}
		if (commandInput.getPriority() != 0) {
			tasks.get(i).setPriority(commandInput.getPriority());
		}
		if (commandInput.getStartDate() != null) {
			tasks.get(i).setStartDate(commandInput.getStartDate());
		}
		if (commandInput.getEndDate() != null) {
			tasks.get(i).setEndDate(commandInput.getEndDate());
		}
	}

	private void printError(String msg) {
		System.err.println(DateTime.now() + ": " + msg);
		Logger.writeError(msg);
	}

}
