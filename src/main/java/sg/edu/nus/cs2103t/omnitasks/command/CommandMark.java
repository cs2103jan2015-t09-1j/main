package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

/**
 * This class contains the logic for Mark command. When Mark command is
 * triggered, there will be a check if the target Task is in the tasks list. If
 * it is found, the Task's isCompleted attribute will be edited to the user
 * specification.
 * <p>
 */
// @author A0119742A
public class CommandMark extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "mark" };

	public CommandMark(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {
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
			ui.showMessage("Task \"" + task.getName()
					+ "\" is successfully marked as Done!");
			return true;
		} else if (commandInput.isCompleted() == false && data.editTask(task)
				&& task != null) {
			ui.showMessage("Task \"" + task.getName()
					+ "\" is successfully marked as Not Done!");
			return true;
		}

		return false;
	}
}