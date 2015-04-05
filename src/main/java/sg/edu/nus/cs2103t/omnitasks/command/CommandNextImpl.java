package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandNextImpl extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "next", "n" };

	public CommandNextImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
		ui.scrollDown();
		return true;
	}
}
