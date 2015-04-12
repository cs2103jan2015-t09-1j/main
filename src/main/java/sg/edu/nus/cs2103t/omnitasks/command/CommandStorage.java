package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

//@author A0119643A

/**
 * Logic for change storage location, Validate if the user input directory exist
 * and change omnitask working directory to the one user specified.
 * <p>
 *
 */
public class CommandStorage extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "storage", "dest",
			"dir" };

	public CommandStorage(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {

		if (commandInput.getName() != null) {
			if (data.changeStorageDirectory(commandInput.getName())) {
				ui.showMessage("Sucessfully changed storage directory to: "
						+ commandInput.getName());
			} else {
				ui.showError(commandInput.getName()
						+ " this directory is invalid! Ensure that the path exist!");
			}
		} else {
			ui.showError("Please fill in the path to the new storage location you wish to set!");
		}

		return false;

	}
}
