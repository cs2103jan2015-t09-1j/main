package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

//@author A0111795A
public abstract class Command {

	protected CommandInput commandInput;

	public Command(CommandInput commandInput) {
		this.commandInput = commandInput;
	}

	public CommandInput getCommandInput() {
		return commandInput;
	}

	public abstract boolean processCommand(Data data, Ui ui);

	public void setCommandInput(CommandInput commandInput) {
		this.commandInput = commandInput;
	}

}
