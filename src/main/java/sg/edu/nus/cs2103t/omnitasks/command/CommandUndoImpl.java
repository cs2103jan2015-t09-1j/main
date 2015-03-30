package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.Controller;
import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandUndoImpl extends Command {
	
	public static String[] COMMAND_ALIASES_UNDO = new String[]{"undo"};
	
	public CommandUndoImpl(CommandInput commandInput) {
		super(commandInput);
	}
	
	@Override
	public boolean processCommand(Data data, UI ui) {
		if(data.undo()){
			ui.showMessage("Undo completed!");
			return true;
		}
		else {
			ui.showMessage("You have no previous entries");
			return false;
		}
	}
}
