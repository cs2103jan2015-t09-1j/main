package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandSearchImpl extends Command {
	
	public CommandSearchImpl(CommandInput commandInput) {
		super(commandInput);
		// TODO Auto-generated constructor stub
	}
	
	// TODO: This class shouldn't be calling UI directly
	@Override
	public boolean processCommand(UI ui, Data data) {
		//process search functions done here
		ArrayList<Task> searchTaskResult = new ArrayList<Task>();
		ArrayList<Task> fullTaskList = new ArrayList<Task>();
		
		fullTaskList = data.searchTask(commandInput);
		String searchKey = commandInput.getName();
		
		if (commandInput.getName()!=null) {
			for (int i = 0; i < fullTaskList.size(); i++) {
				if (fullTaskList.get(i).getName().toLowerCase().contains(searchKey)) {
					searchTaskResult.add(fullTaskList.get(i));
				}
			}
			ui.updateTaskListings(searchTaskResult);
		}else{
			ui.showMessage("Please fill in the search key!");
		}
		
		
		//return false to update the task listing with search result and not default 
		return false;
	}
}
