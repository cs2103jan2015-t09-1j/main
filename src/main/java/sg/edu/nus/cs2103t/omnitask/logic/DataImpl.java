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
	
}
