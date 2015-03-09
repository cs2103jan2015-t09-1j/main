package sg.edu.nus.cs2103t.omnitask.data;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.Task;

public abstract class Data {
	public abstract ArrayList<Task> getTasks();
	
	public abstract boolean addTask(Task task);
	
	public abstract boolean deleteTask(Task task, int id);
}
