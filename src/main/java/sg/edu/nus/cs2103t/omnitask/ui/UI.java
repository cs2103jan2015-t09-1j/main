package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.MainViewController.ViewMode;

//@author A0111795A
/**
 * This class handles all user interaction and rendering of task lists.
 * 
 */
public abstract class Ui {

	/**
	 * This interface is used by Ui class to communicate with Controller class in the form of callback methods
	 */
	public static interface ControllerCallback {
		/**
		 * Returns the possible auto complete. The userInput argument is used to derive the possibilities.
		 *
		 * @param  userInput User's input  
		 * @return 			 List of possible auto complete
		 */
		ArrayList<String> doAutoComplete(String userInput);

		/**
		 * Process user's input and execute the evaluated command.
		 *
		 * @param  userInput user's input  
		 * @return true If command is successful
		 */
		boolean onCommandReceived(String userInput);

		/**
		 * Populate Ui's mini help window if needed.
		 *
		 * @param  userInput User's input
		 */
		void showMiniHelpIfAvailable(String userInput);

		/**
		 * Show all tasks (similar to issuing "show all" as user input).
		 */
		void showAll();

		/**
		 * Mark the specified task as done and return the result.
		 *
		 * @param  id Task id
		 * @return true If command is successful
		 */
		boolean markTaskAsDone(long id);

		/**
		 * Mark the specified task as not done and return the result.
		 *
		 * @param  id Task id
		 * @return true If command is successful
		 */
		boolean markTaskAsNotDone(long id);
	}

	protected ControllerCallback controllerCallback;

	/**
	 * Closes the help window.
	 */
	public abstract void closeHelp();

	/**
	 * Closes the mini help window.
	 */
	public void closeMiniHelp() {
	}

	/**
	 * Exits the UI. Should be called before exiting the program to allow clean ups.
	 */
	public abstract void exit();

	/**
	 * Helper method to call {@link Ui.ControllerCallback#showMiniHelpIfAvailable}.
	 * Calling this ensure that the method is not called on a null controllerCallback.
	 * 
	 * @param  userInput User's input
	 */
	public void invokeShowMiniHelpIfAvailable(String userInput) {
		if (controllerCallback != null) {
			controllerCallback.showMiniHelpIfAvailable(userInput);
		}
	}

	/**
	 * Helper method to call {@link Ui.ControllerCallback#showAll}.
	 * Calling this ensure that the method is not called on a null controllerCallback.
	 */
	public void invokeShowAll() {
		if (controllerCallback != null) {
			controllerCallback.showAll();
		}
	}

	/**
	 * Helper method to call {@link Ui.ControllerCallback#markTaskAsDone}.
	 * Calling this ensure that the method is not called on a null controllerCallback.
	 *
	 * @param  id Task id
	 * @return true If command is successful
	 */
	public boolean invokeMarkTaskAsDone(long id) {
		if (controllerCallback != null) {
			return controllerCallback.markTaskAsDone(id);
		}

		return false;
	}

	/**
	 * Helper method to call {@link Ui.ControllerCallback#markTaskAsNotDone}.
	 * Calling this ensure that the method is not called on a null controllerCallback.
	 * 
	 * @param  id Task id
	 * @return true If command is successful
	 */
	public boolean invokeMarkTaskAsNotDone(long id) {
		if (controllerCallback != null) {
			return controllerCallback.markTaskAsNotDone(id);
		}

		return false;
	}

	/**
	 * Redraws WebView. Use when graphical glitch in JavaFx's WebView occur.
	 */
	public void redraw() {
	}

	/**
	 * Scroll the task list down.
	 */
	public void scrollDown() {
	}

	/**
	 * Scroll the task list up.
	 */
	public void scrollUp() {
	}

	/**
	 * Scroll to specified section. However, usually you would call {@link Ui#showSection(DateTime)} or {@link Ui#showSection(DateTime, DateTime)} instead.
	 * 
	 * @param section Section to scroll to
	 */
	public void showSection(String section) {
	}

	/**
	 * Scroll to specified date.
	 * 
	 * @param endDate Date to scroll to
	 */
	public void showSection(DateTime endDate) {
	}

	/**
	 * Scroll to specified date.
	 * 
	 * @param startDate Date to scroll to
	 * @param endDate Date to scroll to
	 */
	public void showSection(DateTime startDate, DateTime endDate) {
	}

	/**
	 * Sets the controller callback.
	 * 
	 * @param controllerCallback Usually the controller class this Ui is associated with
	 */
	public void setControllerCallback(ControllerCallback controllerCallback) {
		this.controllerCallback = controllerCallback;
	}

	/**
	 * Show all tasks.
	 */
	public abstract void showAllTasks();

	/**
	 * Show error message to user.
	 * 
	 * @param msg Error message
	 */
	public abstract void showError(String msg);

	/**
	 * Show help window.
	 * 
	 * @param msg Body for help window.
	 */
	public abstract void showHelp(String msg);

	/**
	 * Show message to user.
	 * 
	 * @param msg Message
	 */
	public abstract void showMessage(String msg);

	/**
	 * Show mini help window.
	 * 
	 * @param msg Body for help window.
	 */
	public void showMiniHelp(String msg) {
	};

	/**
	 * Show alternate task list. Used to switch between search mode (&amp; category mode) and archive mode.
	 * 
	 * @param viewMode ViewMode to set to. See {@link MainViewController.ViewMode}.
	 * @param title Title to show on the text field at the top.
	 * @param tasks Tasks to populate the list with.
	 */
	public abstract void showAlternateList(ViewMode viewMode, String title,
			ArrayList<Task> tasks);

	/**
	 * Start the Ui lifecycle.
	 */
	public abstract void start();

	/**
	 * Helper method to call {@link Ui.ControllerCallback#CommandReceivedListener}.
	 * Calling this ensure that the method is not called on a null controllerCallback.
	 * 
	 * @param  userInput user's input  
	 * @return true If command is successful
	 */
	protected boolean invokeCommandReceived(String userInput) {
		if (controllerCallback != null) {
			return controllerCallback.onCommandReceived(userInput);
		}

		return false;
	}

	/**
	 * Helper method to call {@link Ui.ControllerCallback#doAutocompleteListener}.
	 * Calling this ensure that the method is not called on a null controllerCallback.
	 */
	protected ArrayList<String> invokeDoAutocomplete(String userInput) {
		if (controllerCallback != null) {
			return controllerCallback.doAutoComplete(userInput);
		}

		return new ArrayList<String>();
	}

}
