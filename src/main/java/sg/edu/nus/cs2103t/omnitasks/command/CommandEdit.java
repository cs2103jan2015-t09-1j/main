package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

public class CommandEdit extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "edit",
			"change", "update", "modify" };

	public CommandEdit(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {

		Task task = new Task();
		try {
			task = data.getTask((int) commandInput.getId() - 1);
		} catch (IndexOutOfBoundsException e1) {
			// TODO Auto-generated catch block
			ui.showError("Unable to edit Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");
			e1.printStackTrace();
			return false;
		}
		int wordLengthLimit = 80;

		for (int i = 0, lengthOfWord = 0; i < commandInput.getName().length(); i++) {
			char character = commandInput.getName().charAt(i);
			if (lengthOfWord >= wordLengthLimit) {
				ui.showError("Unable to edit Task. Task name is too long!");
				return false;
			}
			if (character == ' ') {
				lengthOfWord = 0;
			} else {
				lengthOfWord++;
			}

		}
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
			ui.showError("Unable to edit Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");

		}

		return false;
	}
}
