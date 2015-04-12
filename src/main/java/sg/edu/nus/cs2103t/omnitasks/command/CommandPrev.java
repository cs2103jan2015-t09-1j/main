package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

//@author A0111795A
public class CommandPrev extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "prev", "p" };

	public CommandPrev(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {
		ui.scrollUp();
		return true;
	}
}
