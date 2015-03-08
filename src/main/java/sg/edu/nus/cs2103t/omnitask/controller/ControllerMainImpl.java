package sg.edu.nus.cs2103t.omnitask.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.data.DataJSONImpl;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;
import sg.edu.nus.cs2103t.omnitask.ui.UIMainImpl;
import sg.edu.nus.cs2103t.omnitask.ui.UIPrototypeImpl;
import sg.edu.nus.cs2103t.omnitask.ui.UI;

public class ControllerMainImpl extends Controller {
	
	protected UI ui;
	
	protected Parser parser;
	
	protected Commands commands;
	
	protected Data data;
	
	@Override
	public void start(String[] args) {
		// Initialize components
		ui = new UIMainImpl(this);
		parser = new ParserMainImpl();
		
		// Get file from argument
		File storageFile = checkForAndInitArgument(args);
		
		// Check if we have the filename from the argument, quit if not
		if (storageFile == null) {
			ui.exit();
			return;
		}
		
		// Prompt to create file if it does not exist.
		// Exit program if file does not exist and user does not want to create it
		try {
			DataJSONImpl.CheckIfFileExistAndCreateIfDoesNot(storageFile);
		} catch (IOException ex) {
			ui.showError("No permission to access file.");
			ui.exit();
			return;
		}
		
		// Continue initializing components
		data = new DataJSONImpl(storageFile);
		commands = new CommandsMainImpl(ui, data);
		
		// Pass control to UI to receive user input
		ui.start();
	}
	
	@Override
	public void processUserInput(String input) {
		CommandInput commandInput = parser.parseUserInput(input);
		
		if (commandInput == null) {
			ui.showError("Invalid command entered. Please try again.");
		} else {
			commands.processCommand(commandInput);
		}
	}

	private File checkForAndInitArgument(String[] args) {
		// Print error if argument is not given
		if (args.length == 0) {
			ui.showError("Please specify a text file as an argument.");
			return null;
		}
		
		return new File(args[0]);
	}

}
