package sg.edu.nus.cs2103t.omnitasks.command;

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
		Task task = new Task();
		try {
			task = data.getTask((int) commandInput.getId() - 1);
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			ui.showError("Unable to mark Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");
			e.printStackTrace();
			return false;
		}
		task.setCompleted(commandInput.isCompleted());

		if (commandInput.isCompleted() == true && data.editTask(task)
				&& task != null) {
			ui.showMessage("Task \"" + commandInput.getId()
					+ "\" is successfully marked as Done!");
			return true;
		} else if (commandInput.isCompleted() == false && data.editTask(task)
				&& task != null) {
			ui.showMessage("Task \"" + commandInput.getId()
					+ "\" is successfully marked as Not Done!");
		}

		return false;
	}
}
