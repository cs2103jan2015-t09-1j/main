package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandPrevImpl extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "prev", "p" };

	public CommandPrevImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
		ui.scrollUp();
		return true;
	}
}
