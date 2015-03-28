package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandEditImpl extends Command {
	
	public static String[] COMMAND_ALIASES_EDIT = new String[] { "edit", "change",
	"update" };

	public CommandEditImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
		ArrayList<Task> tasks = data.getTasks();
		int indexOfRetrieved=-1;
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getId() == commandInput.getId()) {
				// edit name, priority, start&end date
				Utils.editAttributes(commandInput, i, tasks);
				indexOfRetrieved = i;

			}
		}

		try {
			if (data.editTask(tasks.get(indexOfRetrieved))) {
				ui.showMessage("Task \"" + commandInput.getId()
						+ "\" updated successfully!");
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ui.showError("Unable to update Task \""
					+ commandInput.getId()
					+ "\". Please choose a valid id!");

		}

		return false;
	}
}
