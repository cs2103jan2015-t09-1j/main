package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandDeleteImpl extends Command {

	public static String[] COMMAND_ALIASES_DELETE = new String[]{"delete", "remove"};
	
	public CommandDeleteImpl(CommandInput commandInput) {
		super(commandInput);
		// TODO Auto-generated constructor stub
	}
	
	// TODO: This class shouldn't be calling UI directly
	@Override
	public boolean processCommand(UI ui, Data data) {
		ArrayList<Task> tasks = data.getTasks();
		Task taskToRemove = null;
				
		for (int i = 0; i < tasks.size(); i++) {	
			if (tasks.get(i).getId() == commandInput.getId()) {
				//taskToRemove.setId(commandInput.getId());
				taskToRemove = tasks.remove(i);	
			}
		}

		if (data.deleteTask(taskToRemove) && taskToRemove!=null) {
			ui.showMessage("Task \"" + commandInput.getId()
					+ "\" deleted successfully!");
		} else {
			ui.showMessage("Unable to delete Task \"" + commandInput.getId()
					+ "\". Please choose a valid id!");
		}
		return true;
	}
	
	public static CommandInput.CommandType GetCommandTypeFromString(String str) {
		return Utils.getCommandTypeForEveryClass(str, COMMAND_ALIASES_DELETE, CommandInput.CommandType.ADD);
	}
}
