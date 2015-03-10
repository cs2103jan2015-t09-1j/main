package sg.edu.nus.cs2103t.omnitask.controller;

public abstract class Controller {
	public abstract void start(String[] args);
	
	public abstract void processUserInput(String input);
}
