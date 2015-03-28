package sg.edu.nus.cs2103t.omnitasks.command;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class CommandSearchImpl extends Command {
	
	public static String[] COMMAND_ALIASES_SEARCH = new String[]{"search", "find"};
	
	public CommandSearchImpl(CommandInput commandInput) {
		super(commandInput);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void processCommand(Data data, CommandResultListener listener) {
		//process search functions done here
		ArrayList<Task> searchTaskResult = new ArrayList<Task>();
		ArrayList<Task> fullTaskList = new ArrayList<Task>();
		
		fullTaskList = data.searchTask();
		String searchKey = commandInput.getName();
		
		if (commandInput.getName()!=null) {
			for (int i = 0; i < fullTaskList.size(); i++) {
				if (fullTaskList.get(i).getName().toLowerCase().contains(searchKey)) {
					searchTaskResult.add(fullTaskList.get(i));
				}
			}
			// TODO: Faruq: I am breaking search at the moment to implement observer pattern
			//ui.updateTaskListings(searchTaskResult);
		}else{
			listener.onFailure("Please fill in the search key!");
		}
		
		
		//return false to update the task listing with search result and not default 
		return;
	}
}
