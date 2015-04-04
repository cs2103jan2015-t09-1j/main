package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class Utils {

	public static void addAttributes(CommandInput commandInput, Task task) {
		task.setName(commandInput.getName());

		// there exits 2 enums of priority in Task and CommandInput passing
		// between this 2 resulted in conflict
		// using int function instead to set priority
		// cant be used -> task.setPriority(commandInput.getPriority());
		if (commandInput.getPriority() != null) {
			int prioNum = commandInput.convertPriorityToNum((commandInput
					.getPriority()));
			task.setPriorityByNum(prioNum);
		}

		task.setStartDate(commandInput.getStartDate());
		task.setEndDate(commandInput.getEndDate());
	}

	public static void makeTaskToEdit(CommandInput commandInput, Task mutatorTask) {
		if (!commandInput.getName().equals("")) {
			mutatorTask.setName(commandInput.getName());
		}
		if (commandInput.getPriority() != null) {
			int prioNum = commandInput.convertPriorityToNum((commandInput
					.getPriority()));
			mutatorTask.setPriorityByNum(prioNum);
			// tasks.get(i).setPriority(commandInput.getPriority());
		}
		if (commandInput.getStartDate() != null) {
			mutatorTask.setStartDate(commandInput.getStartDate());
		}
		if (commandInput.getEndDate() != null) {
			mutatorTask.setEndDate(commandInput.getEndDate());
		}
	}
	
	public static void editAttributes(Task foundTask, Task mutatorTask) {
		if (!mutatorTask.getName().equals("")) {
			foundTask.setName(mutatorTask.getName());
		}
		if (mutatorTask.getPriority() != null) {
			foundTask.setPriority(mutatorTask.getPriority());
		}
		if (mutatorTask.getStartDate() != null) {
			foundTask.setStartDate(mutatorTask.getStartDate());
		}
		if (mutatorTask.getEndDate() != null) {
			foundTask.setEndDate(mutatorTask.getEndDate());
		}
		foundTask.setCompleted(mutatorTask.isCompleted());
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

		for (String command : CommandHelpImpl.COMMAND_ALIASES_HELP) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.HELP;
			}
		}

		for (String command : CommandUndoImpl.COMMAND_ALIASES_UNDO) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.UNDO;
			}
		}

		for (String command : CommandRedoImpl.COMMAND_ALIASES_REDO) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.REDO;
			}
		}
		
		for (String command : CommandMarkImpl.COMMAND_ALIASES_MARK) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.MARK;
			}
		}

		return CommandInput.CommandType.INVALID;
	}

}
