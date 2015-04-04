package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandMarkImpl extends Command {

	public static String[] COMMAND_ALIASES_MARK = new String[] { "mark" };

	public CommandMarkImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
		ArrayList<Task> tasks = data.getTasks();
		Task taskToMark = null;

		// Search for a task with user-specified taskId
		for (int i = 0; i < tasks.size(); i++) {
			// Return true if taskId is the same as user-specified taskId
			if (tasks.get(i).getId() == commandInput.getId()) {
				// Copy the task into taskToRemove for Data to process
				taskToMark = tasks.get(i);
				taskToMark.setCompleted(commandInput.isCompleted());
			}
		}

		if (data.markTask(taskToMark) && taskToMark != null) {
			ui.showMessage("Task \"" + commandInput.getId()
					+ "\" marked successfully!");
			return true;
		} else {
			ui.showError("Unable to mark Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");
		}

		return false;
	}
}
