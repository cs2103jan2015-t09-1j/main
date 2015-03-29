package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.Task;


public abstract class UI {
	
	protected CommandReceivedListener commandReceivedListener;
	
	public static interface CommandReceivedListener {
		void onCommandReceived(String userInput);
		
		String doAutoComplete(String userInput);
	}

	public void setCommandReceivedListener(CommandReceivedListener commandReceivedListener) {
		this.commandReceivedListener = commandReceivedListener;
	}
	
	protected void invokeCommandReceivedListener(String userInput) {
		if (commandReceivedListener != null) {
			commandReceivedListener.onCommandReceived(userInput);
		}
	}
	
	protected String invokeDoAutocompleteListener(String userInput) {
		if (commandReceivedListener != null) {
			return commandReceivedListener.doAutoComplete(userInput);
		}
		
		return userInput;
	}
	
	public abstract void showError(String msg);

	public abstract void showMessage(String msg);
	
	public abstract void showHelp(String msg);
	
	public abstract void closeHelp();
	
	public abstract void showSearchResults(ArrayList<Task> tasks);

	public abstract void start();
	
	public abstract void exit();
	
}
