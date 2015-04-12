package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

//@author A0111795A
public class CommandNext extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "next", "n" };

	public CommandNext(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {
		ui.scrollDown();
		return true;
	}
}
