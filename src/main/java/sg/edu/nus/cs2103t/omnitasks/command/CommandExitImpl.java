package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandExitImpl extends Command {
	
	public static String[] COMMAND_ALIASES_EXIT = new String[]{"exit", "close"};
	
	public CommandExitImpl(CommandInput commandInput) {
		super(commandInput);
		// TODO Auto-generated constructor stub
	}
	
	// TODO: This class shouldn't be calling UI directly
	@Override
	public boolean processCommand(UI ui, Data data) {
		ui.exit();
		return true;
	}
	
	public static CommandInput.CommandType GetCommandTypeFromString(String str) {
		return Utils.getCommandTypeForEveryClass(str, COMMAND_ALIASES_EXIT, CommandInput.CommandType.ADD);
	}
}
