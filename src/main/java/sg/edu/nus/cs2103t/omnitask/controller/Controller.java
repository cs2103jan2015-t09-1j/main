package sg.edu.nus.cs2103t.omnitask.controller;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;

public abstract class Controller {
	public abstract void start(String[] args);
	
	public abstract void processCommand(CommandInput commandInput);
	
	public abstract void processAddCommand(CommandInput commandInput);
	
	public abstract void processDisplayCommand(CommandInput commandInput);
	
	public abstract void processDeleteCommand(CommandInput commandInput);
	
	public abstract void processUpdateCommand(CommandInput commandInput);
	
	public abstract void processUserInput(String input);
}
