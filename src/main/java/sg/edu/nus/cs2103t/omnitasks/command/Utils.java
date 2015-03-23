package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class Utils {
	
	public static void addAttributes(CommandInput commandInput, Task task) {
		task.setName(commandInput.getName());
		task.setPriority(commandInput.getPriority());
		task.setStartDate(commandInput.getStartDate());
		task.setEndDate(commandInput.getEndDate());
	}
	
	public static void editAttributes(CommandInput commandInput, int i, ArrayList<Task> tasks) {
		if (!commandInput.getName().equals("")) {
			tasks.get(i).setName(commandInput.getName());
		}
		if (commandInput.getPriority() != 0) {
			tasks.get(i).setPriority(commandInput.getPriority());
		}
		if (commandInput.getStartDate() != null) {
			tasks.get(i).setStartDate(commandInput.getStartDate());
		}
		if (commandInput.getEndDate() != null) {
			tasks.get(i).setEndDate(commandInput.getEndDate());
		}
	}
	
	public static CommandInput.CommandType getCommandTypeForEveryClass(String str, String[] commandAliases, CommandInput.CommandType defaultType){
		
		for (String command : commandAliases){
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return defaultType;
			}
		}
	
		return CommandInput.CommandType.INVALID;
	}
	
}
