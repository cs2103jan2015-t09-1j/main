package sg.edu.nus.cs2103t.omnitask.controller;

import sg.edu.nus.cs2103t.omnitask.model.CommandInput;

public abstract class Commands {
	public static String COMMAND_ADD = "add";
	
	public static String COMMAND_DISPLAY = "display";
	
	
	
	public abstract void processCommand(CommandInput commandInput);
}
