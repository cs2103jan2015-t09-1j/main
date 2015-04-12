package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.Controller;
import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandExitImpl extends Command {

	public static String[] COMMAND_ALIASES_EXIT = new String[] { "exit",
			"close", "escape" };

	public CommandExitImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
		Controller.Exit();
		return true;
	}
}
