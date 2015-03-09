package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.List;

import sg.edu.nus.cs2103t.omnitask.model.Task;

public interface UI {
	public void showMessage(String msg);
	
	public void showError(String msg);
	
	public void start();
	
//<<<<<<< HEAD
	public abstract void start();//push test sijie
//=======
	public void exit();
//>>>>>>> 539c0fd0cf4488f2f17108ef11c8f4df5c6540db
	
	public void updateTaskListings(List<Task> tasks);
}
