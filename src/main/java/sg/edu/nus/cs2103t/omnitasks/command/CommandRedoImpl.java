package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandRedoImpl extends Command {

	public static String[] COMMAND_ALIASES_REDO = new String[] { "redo" };

	public CommandRedoImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
		if (data.redo()) {
			ui.showMessage("Redo completed!");
			return true;
		} else {
			ui.showMessage("You have no Redo entries");
			return false;
		}
	}
}
