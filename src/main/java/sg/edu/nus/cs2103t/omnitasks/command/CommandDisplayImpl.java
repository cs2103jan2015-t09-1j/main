package sg.edu.nus.cs2103t.omnitasks.command;

import java.io.IOException;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.Data.TaskNoNameException;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandDisplayImpl implements Command {
	
	public static String[] COMMAND_ALIASES = new String[]{"display", "show"};
	
	private static CommandDisplayImpl command;
	
	private CommandDisplayImpl() {
		
	}

	public static CommandDisplayImpl GetSingleton() {
		if (command == null) command = new CommandDisplayImpl();
		
		return command;
	}
	
	@Override
	public CommandInput.CommandType getCommandTypeFromString(String str) {
		for (String command : COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.DISPLAY;
			}
		}
		
		return CommandInput.CommandType.INVALID;
	}
	
	// TODO: This class shouldn't be calling UI directly
	@Override
	public boolean processCommand(UI ui, Data data, CommandInput commandInput) {
		return true;
	}
	
}
