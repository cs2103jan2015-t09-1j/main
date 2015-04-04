package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.Task;


public abstract class UI {
	
	protected CommandReceivedListener commandReceivedListener;
	
	public static interface CommandReceivedListener {
		boolean onCommandReceived(String userInput);
		
		ArrayList<String> doAutoComplete(String userInput);
		
		boolean updateTask(Task task);
		
		void showMiniHelpIfAvailable(String userInput);
	}

	public void setCommandReceivedListener(CommandReceivedListener commandReceivedListener) {
		this.commandReceivedListener = commandReceivedListener;
	}
	
	protected boolean invokeCommandReceivedListener(String userInput) {
		if (commandReceivedListener != null) {
			return commandReceivedListener.onCommandReceived(userInput);
		}
		
		return false;
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
	
	public void invokeShowMiniHelpIfAvailable(String input) {
		if (commandReceivedListener != null) {
			commandReceivedListener.showMiniHelpIfAvailable(input);
		}
	}
	
	public abstract void showError(String msg);

	public abstract void showMessage(String msg);
	
	public abstract void showHelp(String msg);
	
	public abstract void closeHelp();
	
	public abstract void showMiniHelp(String msg);
	
	public abstract void closeMiniHelp();
	
	public abstract void showAllTasks();
	
	public abstract void showSearchResults(String keyword, ArrayList<Task> tasks);
	
	public abstract void redraw();

	public abstract void start();
	
	public abstract void exit();
	
}
