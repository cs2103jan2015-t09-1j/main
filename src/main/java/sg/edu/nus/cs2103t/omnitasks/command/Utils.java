package sg.edu.nus.cs2103t.omnitasks.command;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class Utils {
	
	public static void addAttributes(CommandInput commandInput, Task task) {
		task.setName(commandInput.getName());
		task.setPriority(commandInput.getPriority());
		task.setStartDate(commandInput.getStartDate());
		task.setEndDate(commandInput.getEndDate());
	}
	
}
