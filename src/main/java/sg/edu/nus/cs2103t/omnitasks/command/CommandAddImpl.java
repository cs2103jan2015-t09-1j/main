package sg.edu.nus.cs2103t.omnitasks.command;

import java.io.IOException;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.Data.TaskNoNameException;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandAddImpl implements Command {
	
	private static String[] commandAliases = new String[]{"add", "new"};
	
	private static CommandAddImpl command;
	
	private CommandAddImpl() {
		
	}

	public static CommandAddImpl GetSingleton() {
		if (command == null) command = new CommandAddImpl();
		
		return command;
	}
	
	@Override
	public CommandInput.CommandType getCommandTypeFromString(String str) {
		for (String command : commandAliases) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.ADD;
			}
		}
		
		return CommandInput.CommandType.INVALID;
	}
	
	// TODO: This class shouldn't be calling UI directly
	@Override
	public boolean processCommand(UI ui, Data data, CommandInput commandInput) {
		Task task = new Task();
		Utils.addAttributes(commandInput, task);
		
		// TODO: Fix magic string
		try {
			if (data.addTask(task)) {
				ui.showMessage("Task \"" + task.getName() + "\" added successfully!");
				
				return true;
			}
		} catch (TaskNoNameException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ui.showMessage("Failed to add task \"" + commandInput.getName() + "\".");
		
		return false;
	}
	
}
