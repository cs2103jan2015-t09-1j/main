package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

/**
 * This class contains the logic for RemoveDate command. When RemoveDate command is
 * triggered, there will be a check if the target Task is in the tasks list. If
 * it is found, the Task's date will be removed
 * <p>
 */
//@author A0119742
public class CommandRemoveDate extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "remove-date" };

	public CommandRemoveDate(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {
		Task taskToRemoveDate = data.getTask((int) commandInput.getId() - 1);
		
		taskToRemoveDate.setStartDate(null);
		taskToRemoveDate.setEndDate(null);

		try {
			if (data.removeTaskDate(taskToRemoveDate)) {
				ui.showMessage("Task \"" + taskToRemoveDate.getName()
						+ "\" date has been removed!");
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ui.showError("Unable to edit Task \"" + taskToRemoveDate.getName()
					+ "\". Please choose a valid id!");

		}

		return false;
	}
}