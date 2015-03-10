package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.List;

import sg.edu.nus.cs2103t.omnitask.model.Task;

public interface UI {
	public void showMessage(String msg);
	
	public void showError(String msg);
	
	public abstract void start();
	
	public void exit();
	
	public void updateTaskListings(List<Task> tasks);
}
