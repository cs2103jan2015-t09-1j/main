package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandExitImpl extends Command {
	
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
}
