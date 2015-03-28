package sg.edu.nus.cs2103t.omnitasks.command;

import java.io.IOException;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.Data.TaskNoNameException;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandAddImpl extends Command {
	
	public static String[] COMMAND_ALIASES_ADD = new String[]{"add", "new"};
	
	public CommandAddImpl(CommandInput commandInput) {
		super(commandInput);
	}
	
	@Override
	public boolean processCommand(Data data, UI ui) {
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
		
		ui.showError("Failed to add task \"" + commandInput.getName() + "\".");
		
		return false;
	}
}
