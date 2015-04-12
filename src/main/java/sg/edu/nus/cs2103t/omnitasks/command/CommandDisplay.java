package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.MainViewController;
import sg.edu.nus.cs2103t.omnitask.ui.MainViewController.ViewMode;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;

public class CommandDisplay extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "display",
			"show" };

	public CommandDisplay(CommandInput commandInput) {
		super(commandInput);
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {
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
		} else if (commandInput.getName().toLowerCase().startsWith("archive")) {
			
			ArrayList<Task> showTaskResult = new ArrayList<Task>();
			ArrayList<Task> fullTaskList = data.searchTask();
			
			for (Task task : fullTaskList) {
				if (task.isArchived()) {
					showTaskResult.add(task);
				}
			}

			ui.showAlternateList(ViewMode.ARCHIVED, "Archived Tasks", showTaskResult);
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

			ui.showAlternateList(ViewMode.ALTERNATE, "Category \"" + searchKey + "\"", showTaskResult);
		} else if (commandInput.getStartDate() != null || commandInput.getEndDate() != null) {
			ui.showSection(commandInput.getStartDate(), commandInput.getEndDate());
		} else {
			ui.showError("Invalid parameters for \"" + commandInput.getCommandType().toString().toLowerCase() + "\" command!");
		}
		
		return true;
	}
}
