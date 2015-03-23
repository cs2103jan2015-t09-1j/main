package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;

public abstract class Command {
	
	protected CommandInput commandInput;
	
	public static interface CommandResultListener {
		void onSuccess(String msg);
		
		void onFailure(String msg);
	}
	
	public Command(CommandInput commandInput) {
		this.commandInput = commandInput;
	}
	
	public CommandInput getCommandInput() {
		return commandInput;
	}

	public void setCommandInput(CommandInput commandInput) {
		this.commandInput = commandInput;
	}

	public abstract void processCommand(Data data, CommandResultListener listener);
	
}
