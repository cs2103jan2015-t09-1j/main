package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.MainViewController;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandDisplayImpl extends Command {

	public static String[] COMMAND_ALIASES_DISPLAY = new String[] { "display",
			"show" };

	public CommandDisplayImpl(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
		if (commandInput.getName() == null || (commandInput.getName().isEmpty() && commandInput.getStartDate() == null && commandInput.getEndDate() == null)) {
			ui.showError("Invalid parameters for \"" + commandInput.getCommandType().toString().toLowerCase() + "\" command!");
			return false;
		}
		
		if (commandInput.getName().toLowerCase().equals("all")) {
			ui.showAllTasks();
			data.updateTaskId();
		} else if (commandInput.getName().toLowerCase().equals("overdue")) {
			ui.showSection(MainViewController.SECTION_OVERDUE);
		} else if (commandInput.getName().toLowerCase().equals("no due") || commandInput.getName().toLowerCase().equals("floating")) {
			ui.showSection(MainViewController.SECTION_FLOATING);
		} else if (commandInput.getName().startsWith("#")) {
			
			ArrayList<Task> showTaskResult = new ArrayList<Task>();
			ArrayList<Task> fullTaskList = new ArrayList<Task>();

			fullTaskList = data.searchTask();
			String searchKey = commandInput.getName().toLowerCase();
			for (int i = 0; i < fullTaskList.size(); i++) {
				if (fullTaskList.get(i).getName().toLowerCase()
						.contains(searchKey)) {
					showTaskResult.add(fullTaskList.get(i));
				}
			}

			ui.showSearchResults(searchKey, showTaskResult);
		} else if (commandInput.getStartDate() != null || commandInput.getEndDate() != null) {
			ui.showSection(commandInput.getStartDate(), commandInput.getEndDate());
		} else {
			ui.showError("Invalid parameters for \"" + commandInput.getCommandType().toString().toLowerCase() + "\" command!");
		}
		
		return true;
	}
}
