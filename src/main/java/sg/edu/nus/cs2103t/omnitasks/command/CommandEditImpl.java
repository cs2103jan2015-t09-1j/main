package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandEditImpl extends Command {

	public static String[] COMMAND_ALIASES_EDIT = new String[] { "edit",
			"change", "update", "modify" };

	public CommandEditImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
		Task task = data.getTask((int) commandInput.getId() - 1);
		// edit name, priority, start&end date from commandInput
		Utils.makeTaskToEdit(commandInput, task);

		try {
			if (data.editTask(task)) {
				ui.showMessage("Task \"" + commandInput.getId()
						+ "\" updated successfully!");
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ui.showError("Unable to update Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");

		}

		return false;
	}
}
