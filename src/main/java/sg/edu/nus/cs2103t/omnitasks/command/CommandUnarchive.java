package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

//@author A0111795A
/**
 * This class contains the logic for unarchive command. When unarchive command is triggered, 
 * it will call the data class to mark the respective archived task unarchived again, thus
 * unhide it from displaying.
 */
public class CommandUnarchive extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "unarchive" };

	public CommandUnarchive(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {
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
