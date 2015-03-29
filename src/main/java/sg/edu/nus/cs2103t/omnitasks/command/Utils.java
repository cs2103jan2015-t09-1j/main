package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class Utils {
	
	public static void addAttributes(CommandInput commandInput, Task task) {
		task.setName(commandInput.getName());
		
		
		//there exits 2 enums of priority in Task and CommandInput passing between this 2 resulted in conflict
		//using int function instead to set priority
		//cant be used -> task.setPriority(commandInput.getPriority());
		if(commandInput.getPriority()!=null){
		int prioNum=commandInput.convertPriorityToNum((commandInput.getPriority()));
		task.setPriorityByNum(prioNum);
		}
		
		task.setStartDate(commandInput.getStartDate());
		task.setEndDate(commandInput.getEndDate());
	}
	
	public static void editAttributes(CommandInput commandInput, int i, ArrayList<Task> tasks) {
		if (!commandInput.getName().equals("")) {
			tasks.get(i).setName(commandInput.getName());
		}
		if (commandInput.getPriority() != null) {
			int prioNum=commandInput.convertPriorityToNum((commandInput.getPriority()));
			tasks.get(i).setPriorityByNum(prioNum);
			//tasks.get(i).setPriority(commandInput.getPriority());
		}
		if (commandInput.getStartDate() != null) {
			tasks.get(i).setStartDate(commandInput.getStartDate());
		}
		if (commandInput.getEndDate() != null) {
			tasks.get(i).setEndDate(commandInput.getEndDate());
		}
	}
	
	public static CommandInput.CommandType getCommandTypeFromString(String str) {
		
		for (String command : CommandAddImpl.COMMAND_ALIASES_ADD) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.ADD;
			}
		}
		
		for (String command : CommandDeleteImpl.COMMAND_ALIASES_DELETE) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.DELETE;
			}
		}
		
		for (String command : CommandDisplayImpl.COMMAND_ALIASES_DISPLAY) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.DISPLAY;
			}
		}
		
		for (String command : CommandEditImpl.COMMAND_ALIASES_EDIT) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.EDIT;
			}
		}
		
		for (String command : CommandExitImpl.COMMAND_ALIASES_EXIT) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.EXIT;
			}
		}
		
		for (String command : CommandSearchImpl.COMMAND_ALIASES_SEARCH) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.SEARCH;
			}
		}
	
		return CommandInput.CommandType.INVALID;
	}
	
}
