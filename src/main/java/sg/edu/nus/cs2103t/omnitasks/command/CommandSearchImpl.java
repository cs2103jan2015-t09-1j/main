package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandSearchImpl extends Command {

	public static String[] COMMAND_ALIASES_SEARCH = new String[] { "search",
			"find" };

	public CommandSearchImpl(CommandInput commandInput) {
		super(commandInput);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean processCommand(Data data, UI ui) {
		// process search functions done here
		ArrayList<Task> searchTaskResult = new ArrayList<Task>();
		ArrayList<Task> fullTaskList = new ArrayList<Task>();

		fullTaskList = data.searchTask();
		String searchKey = commandInput.getName();

		if (commandInput.getName() != null) {
			for (int i = 0; i < fullTaskList.size(); i++) {
				if (fullTaskList.get(i).getName().toLowerCase()
						.contains(searchKey)) {
					searchTaskResult.add(fullTaskList.get(i));
				}
			}

			ui.showSearchResults(searchKey, searchTaskResult);
			ui.showMessage("Type \"show all\" to show all tasks.");
			return true;
		} else {
			ui.showError("Please fill in the search key!");
		}

		// return false to update the task listing with search result and not
		// default
		return false;
	}
}
