package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;

public class CommandDisplayImpl extends Command {
	
	public static String[] COMMAND_ALIASES_DISPLAY = new String[]{"display", "show"};
	
	public CommandDisplayImpl(CommandInput commandInput) {
		super(commandInput);
	}
	
	@Override
	public void processCommand(Data data, CommandResultListener listener) {
		data.notifyDataChanged();
		return;
	}
	
	public static CommandInput.CommandType GetCommandTypeFromString(String str) {
		return Utils.getCommandTypeForEveryClass(str, COMMAND_ALIASES_DISPLAY, CommandInput.CommandType.DISPLAY);
	}
}
