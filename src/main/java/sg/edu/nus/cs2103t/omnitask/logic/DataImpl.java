package sg.edu.nus.cs2103t.omnitask.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.storage.IO;
import sg.edu.nus.cs2103t.omnitask.storage.IOJSONImpl;

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

	// Get new unique id which can be used for a new task
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
		task.setId(getNewId());
		task.setName(commandInput.getName());
		task.setStartDate(commandInput.getStartDate());
		task.setEndDate(commandInput.getEndDate());

		// Add the task to our "local cache"
		tasks.add(task);

		// Commit it to storage
		try {
			io.saveToFile(tasks);
		} catch (IOException ex) {
			// TODO: Handle error
			ex.printStackTrace();

			// Reverse change
			tasks.remove(tasks.size() - 1);

			return null;
		}

		return task;
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

				// if input is not null, update new name
				if (!commandInput.getName().equals("")) {
					tasks.get(i).setName(commandInput.getName());
				}
				if(commandInput.getStartDate()!=null) {
					tasks.get(i).setStartDate(commandInput.getStartDate());
				}
				if(commandInput.getEndDate()!=null) {
					tasks.get(i).setEndDate(commandInput.getEndDate());
				}

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

}
