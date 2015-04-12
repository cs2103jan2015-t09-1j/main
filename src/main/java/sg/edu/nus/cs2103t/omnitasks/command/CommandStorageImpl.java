package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandStorageImpl extends Command {

	public static String[] COMMAND_ALIASES_STORAGE = new String[] { "storage",
			"dest", "dir" };

	public CommandStorageImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {

		if (commandInput.getName() != null) {
			if (data.changeStorageDirectory(commandInput.getName())) {
				ui.showMessage("Sucessfully changed storage directory to: "
						+ commandInput.getName());
			} else {
				ui.showError(commandInput.getName() + " this directory is invalid! Ensure that the path exist!");
			}
		} else {
			ui.showError("Please fill in the path to the new storage location you wish to set!");
		}

		return false;

	}
}
