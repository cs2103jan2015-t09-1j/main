package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

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
		ArrayList<Task> tasks = data.getTasks();
		Task taskToEdit = new Task();
		int indexOfRetrieved = -1;
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getId() == commandInput.getId()) {
				taskToEdit = tasks.get(i);
				taskToEdit.setId(commandInput.getId());
				// edit name, priority, start&end date
				Utils.editAttributes(commandInput, taskToEdit);
				indexOfRetrieved = i;

			}
		}

		try {
			if (data.editTask(taskToEdit)) {
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
