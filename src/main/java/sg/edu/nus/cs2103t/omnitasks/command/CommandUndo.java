package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

/**
 * This class contains the logic for undo command. When Undo command is triggered, it will call data class to carry out the reverting to the previous state.
 * <p>
 */
//@author A0119742A
public class CommandUndo extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "undo" };

	public CommandUndo(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {
		if (data.undo()) {
			ui.showMessage("Undo completed!");
			return true;
		} else {
			ui.showMessage("You have no previous entries");
			return false;
		}
	}
}