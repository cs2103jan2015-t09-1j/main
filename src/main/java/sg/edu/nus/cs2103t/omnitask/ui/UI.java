package sg.edu.nus.cs2103t.omnitask.ui;

import java.util.ArrayList;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.MainViewController.ViewMode;

public abstract class Ui {

	public static interface ControllerCallback {
		ArrayList<String> doAutoComplete(String userInput);

		boolean onCommandReceived(String userInput);

		void showMiniHelpIfAvailable(String userInput);

		void showAll();

		boolean markTaskAsDone(long id);

		boolean markTaskAsNotDone(long id);
	}

	protected ControllerCallback controllerCallback;

	public abstract void closeHelp();

	public void closeMiniHelp() {
	}

	public abstract void exit();

	public void invokeShowMiniHelpIfAvailable(String input) {
		if (controllerCallback != null) {
			controllerCallback.showMiniHelpIfAvailable(input);
		}
	}

	public void invokeShowAll() {
		if (controllerCallback != null) {
			controllerCallback.showAll();
		}
	}

	public boolean invokeMarkTaskAsDone(long id) {
		if (controllerCallback != null) {
			return controllerCallback.markTaskAsDone(id);
		}

		return false;
	}

	public boolean invokeMarkTaskAsNotDone(long id) {
		if (controllerCallback != null) {
			return controllerCallback.markTaskAsNotDone(id);
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

	public void setCommandCallback(ControllerCallback commandReceivedListener) {
		this.controllerCallback = commandReceivedListener;
	}

	public abstract void showAllTasks();;

	public abstract void showError(String msg);;

	public abstract void showHelp(String msg);

	public abstract void showMessage(String msg);

	public void showMiniHelp(String msg) {
	};

	public abstract void showAlternateList(ViewMode viewMode, String title,
			ArrayList<Task> tasks);

	public abstract void start();;

	protected boolean invokeCommandReceivedListener(String userInput) {
		if (controllerCallback != null) {
			return controllerCallback.onCommandReceived(userInput);
		}

		return false;
	}

	protected ArrayList<String> invokeDoAutocompleteListener(String userInput) {
		if (controllerCallback != null) {
			return controllerCallback.doAutoComplete(userInput);
		}

		return new ArrayList<String>();
	}

}
