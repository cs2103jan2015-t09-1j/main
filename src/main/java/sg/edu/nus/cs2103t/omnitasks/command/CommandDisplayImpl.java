package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandDisplayImpl extends Command {

	public static String[] COMMAND_ALIASES = new String[]{"display", "show"};
	
	public CommandDisplayImpl(CommandInput commandInput) {
		super(commandInput);
		// TODO Auto-generated constructor stub
	}
	
	// TODO: This class shouldn't be calling UI directly
	@Override
	public boolean processCommand(UI ui, Data data) {
		return true;
	}
	
	public static CommandInput.CommandType GetCommandTypeFromString(String str) {
		for (String command : COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.DISPLAY;
			}
		}
		
		return CommandInput.CommandType.INVALID;
	}
}
