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
		
		// Add the task to our "local cache"
		tasks.add(task);
		
		// Commit it to storage
		try {
			io.saveToFile(tasks);
		} catch (IOException ex) {
			// TODO: Handle error
			ex.printStackTrace();
			
			// Reverse change
			tasks.remove(tasks.size()-1);
			
			return null;
		}
		
		return task;
	}

	@Override
	public boolean deleteTask(long id) {
		Task taskToRemove = null;
		int indexToRemove = -1;
	
		for (int i=0; i < tasks.size(); i++) {
			if (tasks.get(i).getId() == id) {
				taskToRemove = tasks.remove(i);
				indexToRemove = i;
			}
		}
			
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

	@Override
	public boolean updateTask(long id, String taskName) {
		int taskIdToUpdate=-1;
		String tmpTaskName  = "" ;
			
		for (int i=0; i < tasks.size(); i++) {
			if (tasks.get(i).getId() == id) {
				//store the task name from the file in a variable incase need to revert below
				//
				tasks.get(i).setName(taskName);
				taskIdToUpdate=i;
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
				//do a reverse of update
				
				return false;
			}
		} else {
			return false;
		}
		
		return true;
	}
	
}
