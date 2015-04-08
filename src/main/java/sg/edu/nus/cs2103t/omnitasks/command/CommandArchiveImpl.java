package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandArchiveImpl extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "archive" };

	public CommandArchiveImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
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
				ui.showMessage("Task \"" + commandInput.getId()
						+ "\" is successfully archived!");
				return true;
			} else {
				ui.showError("Unable to archive Task \"" + commandInput.getId()
						+ "\". Please choose a valid id!");
			}
		}

		return false;
	}
}
