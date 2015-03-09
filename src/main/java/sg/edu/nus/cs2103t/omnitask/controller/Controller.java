package sg.edu.nus.cs2103t.omnitask.controller;

import java.io.File;

import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public abstract class Controller {
	public abstract void start(String[] args);
	
	public abstract void processUserInput(String input);
}
