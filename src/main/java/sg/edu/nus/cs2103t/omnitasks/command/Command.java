package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public abstract class Command {
	
	protected CommandInput commandInput;
	
	public Command(CommandInput commandInput) {
		this.commandInput = commandInput;
	}
	
	public CommandInput getCommandInput() {
		return commandInput;
	}

	public void setCommandInput(CommandInput commandInput) {
		this.commandInput = commandInput;
	}

	// TODO: This class shouldn't be calling UI directly
	public abstract boolean processCommand(UI ui, Data data);
	
}
