package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.Task;

public class Utils {

	// @author A0119742A
	public static void addAttributes(CommandInput commandInput, Task task) {
		task.setName(commandInput.getName());

		// there exits 2 enums of priority in Task and CommandInput passing
		// between this 2 resulted in conflict
		// using int function instead to set priority
		// cant be used -> task.setPriority(commandInput.getPriority());
		if (commandInput.getPriority() != null) {
			task.setPriority(commandInput.getPriority());
		}

		task.setStartDate(commandInput.getStartDate());
		task.setEndDate(commandInput.getEndDate());
	}

	/**
	 * This method is a utility method that is used by editTask method in
	 * StorageBackedData It creates a Task object that contains attributes of
	 * what the user wants to edit. It then transfer the attributes to the
	 * target Task
	 * <p>
	 * 
	 * @param foundTask
	 *            Target Task
	 * @param mutatorTask
	 *            Task containing user's request
	 */
	public static void editAttributes(Task foundTask, Task mutatorTask) {
		if (!mutatorTask.getName().equals("")) {
			foundTask.setName(mutatorTask.getName());
		}
		if (mutatorTask.getPriority() != null) {
			foundTask.setPriority(mutatorTask.getPriority());
		}
		if (mutatorTask.getStartDate() != null || mutatorTask.getEndDate() != null) {
			foundTask.setStartDate(mutatorTask.getStartDate());
			foundTask.setEndDate(mutatorTask.getEndDate());
		}
		foundTask.setArchived(mutatorTask.isArchived());
		foundTask.setCompleted(mutatorTask.isCompleted());
	}

	// @author A0119742A

	public static CommandInput.CommandType getCommandTypeFromString(String str) {

		for (String command : CommandAdd.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.ADD;
			}
		}

		for (String command : CommandDelete.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.DELETE;
			}
		}

		for (String command : CommandDisplay.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.SHOW;
			}
		}

		for (String command : CommandEdit.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.EDIT;
			}
		}

		for (String command : CommandExit.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.EXIT;
			}
		}

		for (String command : CommandSearch.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.SEARCH;
			}
		}

		for (String command : CommandHelp.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.HELP;
			}
		}

		for (String command : CommandUndo.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.UNDO;
			}
		}

		for (String command : CommandRedo.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.REDO;
			}
		}

		for (String command : CommandMark.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.MARK;
			}
		}

		for (String command : CommandRemoveDate.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.REMOVEDATE;
			}
		}

		for (String command : CommandNext.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.NEXT;
			}
		}

		for (String command : CommandPrev.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.PREV;
			}
		}

		for (String command : CommandStorage.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.STORAGE;
			}
		}

		for (String command : CommandArchive.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.ARCHIVE;
			}
		}

		for (String command : CommandUnarchive.COMMAND_ALIASES) {
			if (command.toLowerCase().equals(str.toLowerCase())) {
				return CommandInput.CommandType.UNARCHIVE;
			}
		}

		return CommandInput.CommandType.INVALID;
	}

	// @author A0119742A
	/**
	 * This method is used by CommandEditImpl to create the mutatorTask which
	 * contains all the attributes that the user wants to edit
	 * <p>
	 * 
	 * @param commandInput
	 *            User's input
	 * @param mutatorTask
	 *            A task object that will take the user's input attributes
	 */
	public static void makeTaskToEdit(CommandInput commandInput,
			Task mutatorTask) {
		if (!commandInput.getName().equals("")) {
			mutatorTask.setName(commandInput.getName());
		}
		if (commandInput.getPriority() != null) {
			mutatorTask.setPriority(commandInput.getPriority());
		}
		if (commandInput.getStartDate() != null || commandInput.getEndDate() != null) {
			mutatorTask.setStartDate(commandInput.getStartDate());
			mutatorTask.setEndDate(commandInput.getEndDate());
		}
	}

}
