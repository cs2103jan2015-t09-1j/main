package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class Utils {
	
	public static String[] COMMAND_ALIASES_ADD = new String[]{"add", "new"};
	public static String[] COMMAND_ALIASES_DISPLAY = new String[]{"display", "show"};
	public static String[] COMMAND_ALIASES_EDIT = new String[] { "edit", "change",
	"update" };
	public static String[] COMMAND_ALIASES_EXIT = new String[]{"exit", "close"};
	public static String[] COMMAND_ALIASES_SEARCH = new String[]{"search", "find"};

	
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
	
	public static CommandInput.CommandType GetCommandTypeFromString(String str) {
		for (String command : COMMAND_ALIASES_ADD) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.ADD;
			}
		}
		for (String command : COMMAND_ALIASES_DISPLAY) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.DISPLAY;
			}
		}
		for (String command : COMMAND_ALIASES_EDIT) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.EDIT;
			}
		}
		for (String command : COMMAND_ALIASES_EXIT) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.EXIT;
			}
		}
		for (String command : COMMAND_ALIASES_SEARCH) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.SEARCH;
			}
		}
		return CommandInput.CommandType.INVALID;
	}
	
}
