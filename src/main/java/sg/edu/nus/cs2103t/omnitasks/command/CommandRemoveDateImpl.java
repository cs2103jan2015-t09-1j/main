package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandRemoveDateImpl extends Command {

	public static String[] COMMAND_ALIASES_REMOVEDATE = new String[] { "remove-date" };

	public CommandRemoveDateImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
		Task taskToRemoveDate = data.getTask((int) commandInput.getId() - 1);
		
		taskToRemoveDate.setStartDate(null);
		taskToRemoveDate.setEndDate(null);

		try {
			if (data.removeTaskDate(taskToRemoveDate)) {
				ui.showMessage("Task \"" + commandInput.getId()
						+ "\" date has been removed!");
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ui.showError("Unable to edit Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");

		}

		return false;
	}
}
