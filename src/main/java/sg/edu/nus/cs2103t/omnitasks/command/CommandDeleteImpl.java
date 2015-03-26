package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class CommandDeleteImpl extends Command {

	public static String[] COMMAND_ALIASES_DELETE = new String[] { "delete",
			"remove" };

	public CommandDeleteImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public void processCommand(Data data, CommandResultListener listener) {
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
			listener.onSuccess("Task \"" + commandInput.getId()
					+ "\" deleted successfully!");
		} else {
			listener.onFailure("Unable to delete Task \""
					+ commandInput.getId() + "\". Please choose a valid id!");
		}
		return;
	}
}
