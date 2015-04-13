package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

//@author A0111795A
/**
 * This class contains the logic for Archive command. When Archive command is triggered,
 * it will check the parameter being passed, when "done" is detected it will call the data class
 * to mark all done tasks as archived and hide from displaying. And when an index is detected, 
 * it will archive the selected task if the index in within correct range.
 * <p>
 */
public class CommandArchive extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "archive" };

	public CommandArchive(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {
		if (commandInput.getName() != null
				&& commandInput.getName().toLowerCase().equals("done")) {
			ArrayList<Task> tasks = data.getTasks();

			for (Task task : tasks) {
				if (task.isCompleted()) {
					task.setArchived(true);

					if (!data.editTask(task)) {
						ui.showError("Unable to archive all done tasks.");
						return false;
					}
				}
			}

			ui.showMessage("All done tasks successfully archived!");

			return true;
		} else {
			Task task = data.getTask((int) commandInput.getId() - 1);
			task.setArchived(true);

			if (data.editTask(task) && task != null) {
				ui.showMessage("Task \"" + task.getName()
						+ "\" is successfully archived!");
				return true;
			} else {
				ui.showError("Unable to archive Task \"" + task.getName()
						+ "\". Please choose a valid id!");
			}
		}

		return false;
	}
}
