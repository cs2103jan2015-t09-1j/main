package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

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
