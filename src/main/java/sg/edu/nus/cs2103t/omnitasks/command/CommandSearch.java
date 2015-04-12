package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.Task;
import sg.edu.nus.cs2103t.omnitask.ui.MainViewController.ViewMode;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;


//@author A0119643A

/**
 * This class contains the logic for search command. When search command is
 * detected by the parser it will pass commandInput object into
 * commandSearchImpl and return a commandSearchImpl object. This class object is
 * used in controller class where processComamnd method is called using
 * commandSearchImpl's object.
 * <p>
 * 
 * 
 * 
 */

public class CommandSearch extends Command {

	public static String[] COMMAND_ALIASES = new String[] { "search", "find" };

	public CommandSearch(CommandInput commandInput) {
		super(commandInput);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean processCommand(Data data, Ui ui) {
		// process search functions done here
		ArrayList<Task> searchTaskResult = new ArrayList<Task>();
		ArrayList<Task> fullTaskList = new ArrayList<Task>();

		fullTaskList = data.searchTask();
		String searchKey = commandInput.getName().toLowerCase();

		if (commandInput.getName() != null) {
			for (int i = 0; i < fullTaskList.size(); i++) {
				if (fullTaskList.get(i).getName().toLowerCase()
						.contains(searchKey)) {
					searchTaskResult.add(fullTaskList.get(i));
				}
			}

			ui.showAlternateList(ViewMode.ALTERNATE, "Search results for \""
					+ searchKey + "\"", searchTaskResult);
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
