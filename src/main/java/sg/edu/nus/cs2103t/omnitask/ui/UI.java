package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.model.Task;

public abstract class UI {

	public static interface CommandReceivedListener {
		ArrayList<String> doAutoComplete(String userInput);

		boolean onCommandReceived(String userInput);

		void showMiniHelpIfAvailable(String userInput);

		boolean updateTask(Task task);
	}

	protected CommandReceivedListener commandReceivedListener;

	public abstract void closeHelp();

	public void closeMiniHelp() {
	}

	public abstract void exit();

	public void invokeShowMiniHelpIfAvailable(String input) {
		if (commandReceivedListener != null) {
			commandReceivedListener.showMiniHelpIfAvailable(input);
		}
	}

	public boolean invokeUpdateTask(Task task) {
		if (commandReceivedListener != null) {
			return commandReceivedListener.updateTask(task);
		}

		return false;
	}

	public void redraw() {
	}

	public void scrollDown() {
	}

	public void scrollUp() {
	}
	
	public void showSection(String section) {
	}
	
	public void showSection(DateTime endDate) {
	}
	
	public void showSection(DateTime startDate, DateTime endDate) {
	}

	public void setCommandReceivedListener(
			CommandReceivedListener commandReceivedListener) {
		this.commandReceivedListener = commandReceivedListener;
	}

	public abstract void showAllTasks();;

	public abstract void showError(String msg);;

	public abstract void showHelp(String msg);

	public abstract void showMessage(String msg);

	public void showMiniHelp(String msg) {
	};

	public abstract void showSearchResults(String keyword, ArrayList<Task> tasks);;

	public abstract void start();;

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

}
