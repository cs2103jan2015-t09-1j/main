package sg.edu.nus.cs2103t.omnitask.controller;

import java.util.ArrayList;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class CommandsMainImpl extends Commands {
	
	private UI ui;
	
	private Data data;

	public CommandsMainImpl(UI ui, Data data) {
		super();
		this.ui = ui;
		this.data = data;
	}

	@Override
	public void processCommand(CommandInput commandInput) {
		// TODO: switch only support constants, maybe bad idea to use it here as it cause magic string
		switch (commandInput.getCommandName()) {
			case "add":
				processAddCommand(commandInput);
				break;
				
			case "display":
				processDisplayCommand(commandInput);
				break;
				
			default:
				new Exception("Not implemented").printStackTrace();
		}
	}

	private void processAddCommand(CommandInput commandInput) {
		long taskId = 1;
		ArrayList<Task> tasks = data.getTasks();
		if (tasks.size() > 0) {
			taskId = tasks.get(tasks.size() - 1).getId() + 1;
		}
		
		Task task = new Task();
		task.setId(taskId);
		task.setName(commandInput.getName());
		
		data.addTask(task);
	}
	
	private void processDisplayCommand(CommandInput commandInput) {
		ArrayList<Task> tasks = data.getTasks();
		ui.updateTaskListings(tasks);
	}
}
