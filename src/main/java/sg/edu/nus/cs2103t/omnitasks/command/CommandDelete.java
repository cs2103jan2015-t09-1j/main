package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

/**
 * This class contains the logic for Delete command. When Delete command is
 * triggered, there will be a check if the target Task is in the tasks list. If
 * it is found, make a replicate of the task object. The replicate then will be
 * passed to data class which will then remove the task object from the tasks
 * list
 * <p>
 */
//@author A0119742A
public class CommandDelete extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "delete", "remove",
			"cancel" };

	public CommandDelete(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {
		ArrayList<Task> tasks = data.getTasks();
		Task taskToRemove = null;

		// Search for a task with user-specified taskId
		for (int i = 0; i < tasks.size(); i++) {
			// Return true if taskId is the same as user-specified taskId
			if (tasks.get(i).getId() == commandInput.getId()) {
				// Copy the task into taskToRemove for Data to process
				taskToRemove = tasks.get(i);
			}
		}

		if (data.deleteTask(taskToRemove) && taskToRemove != null) {
			ui.showMessage("Task \"" + commandInput.getId() + "\" " + "\""
					+ taskToRemove.getName() + "\" deleted successfully!");
			return true;
		} else {
			ui.showError("Unable to delete Task \"" + commandInput.getId()
					+ "\". " + "Please choose a valid id!");
		}

		return false;
	}
}