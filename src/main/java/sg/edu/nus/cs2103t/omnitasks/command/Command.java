package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public interface Command {
	
	public CommandType getCommandTypeFromString(String str);
	
	// TODO: This class shouldn't be calling UI directly
	public boolean processCommand(UI ui, Data data, CommandInput commandInput);
	
}
