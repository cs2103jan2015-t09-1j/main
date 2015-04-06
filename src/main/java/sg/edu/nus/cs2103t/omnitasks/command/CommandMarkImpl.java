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
		Task task = data.getTask((int) commandInput.getId() - 1);
		task.setCompleted(commandInput.isCompleted());

		if (data.editTask(task) && task != null) {
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
