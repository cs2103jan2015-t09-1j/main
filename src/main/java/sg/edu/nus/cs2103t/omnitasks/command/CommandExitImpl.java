package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.Main;
import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;

public class CommandExitImpl extends Command {
	
	public static String[] COMMAND_ALIASES_EXIT = new String[]{"exit", "close"};
	
	public CommandExitImpl(CommandInput commandInput) {
		super(commandInput);
	}
	
	@Override
	public void processCommand(Data data, CommandResultListener listener) {
		Main.Exit();
		return;
	}
}
