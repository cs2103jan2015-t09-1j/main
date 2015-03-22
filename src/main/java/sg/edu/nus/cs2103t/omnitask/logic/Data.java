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
	
	public abstract ArrayList<Task> getTasks();

	public abstract boolean addTask(Task task) throws TaskNoNameException, IOException;

	public abstract boolean deleteTask(CommandInput commandInput);

	public abstract boolean editTask(CommandInput commandInput);
	
	public abstract ArrayList<Task> searchTask(CommandInput commandInput);
}
