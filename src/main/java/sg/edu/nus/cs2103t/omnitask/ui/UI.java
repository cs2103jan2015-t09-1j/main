package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.Task;


public abstract class UI {
	
	protected CommandReceivedListener commandReceivedListener;
	
	public static interface CommandReceivedListener {
		void onCommandReceived(String userInput);
		
		ArrayList<String> doAutoComplete(String userInput);
		
		boolean updateTask(Task task);
	}

	public void setCommandReceivedListener(CommandReceivedListener commandReceivedListener) {
		this.commandReceivedListener = commandReceivedListener;
	}
	
	protected void invokeCommandReceivedListener(String userInput) {
		if (commandReceivedListener != null) {
			commandReceivedListener.onCommandReceived(userInput);
		}
	}
	
	protected ArrayList<String> invokeDoAutocompleteListener(String userInput) {
		if (commandReceivedListener != null) {
			return commandReceivedListener.doAutoComplete(userInput);
		}
		
		return new ArrayList<String>();
	}
	
	public boolean invokeUpdateTask(Task task) {
		if (commandReceivedListener != null) {
			return commandReceivedListener.updateTask(task);
		}
		
		return false;
	}
	
	public abstract void showError(String msg);

	public abstract void showMessage(String msg);
	
	public abstract void showHelp(String msg);
	
	public abstract void closeHelp();
	
	public abstract void showAllTasks();
	
	public abstract void showSearchResults(String keyword, ArrayList<Task> tasks);
	
	public abstract void redraw();

	public abstract void start();
	
	public abstract void exit();
	
}
