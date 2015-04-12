package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandUnarchiveImpl extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "unarchive" };

	public CommandUnarchiveImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
		Task task = data.getTask((int) commandInput.getId() - 1);
		task.setArchived(false);

		if (data.editTask(task) && task != null) {
			ui.showMessage("Task \"" + commandInput.getId()
					+ "\" is successfully unarchived!");
			return true;
		} else {
			ui.showError("Unable to unarchive Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");
		}

		return false;
	}
}
