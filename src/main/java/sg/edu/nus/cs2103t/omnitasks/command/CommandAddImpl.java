package sg.edu.nus.cs2103t.omnitasks.command;

import java.io.IOException;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.Data.TaskNoNameException;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class CommandAddImpl extends Command {
	
	public static String[] COMMAND_ALIASES_ADD = new String[]{"add", "new"};
	
	public CommandAddImpl(CommandInput commandInput) {
		super(commandInput);
	}
	
	@Override
	public void processCommand(Data data, CommandResultListener listener) {
		Task task = new Task();
		Utils.addAttributes(commandInput, task);
		
		// TODO: Fix magic string
		try {
			if (data.addTask(task)) {
				listener.onSuccess("Task \"" + task.getName() + "\" added successfully!");
				return;
			}
		} catch (TaskNoNameException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		listener.onFailure("Failed to add task \"" + commandInput.getName() + "\".");
		
		return;
	}
}
