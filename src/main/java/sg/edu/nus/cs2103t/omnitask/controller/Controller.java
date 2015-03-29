package sg.edu.nus.cs2103t.omnitask.controller;

import java.io.File;
import java.io.IOException;

import sg.edu.nus.cs2103t.omnitask.Main;
import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;
import sg.edu.nus.cs2103t.omnitask.storage.IOJSONImpl;
import sg.edu.nus.cs2103t.omnitask.ui.UI;
import sg.edu.nus.cs2103t.omnitask.ui.UI.CommandReceivedListener;
import sg.edu.nus.cs2103t.omnitasks.command.Command;

public class Controller {
	private static Controller controller;

	protected Parser parser;

	protected Data data;
	
	protected UI ui;
	
	private Controller() {
	}
	
	public static Controller GetSingleton() {
		if (controller == null) controller = new Controller();
		
		return controller;
	}
	
	public void setUi(UI ui) {
		this.ui = ui;
		this.ui.setCommandReceivedListener(new CommandReceivedListener() {

			@Override
			public void onCommandReceived(String userInput) {
				processUserInput(userInput);
			}

			@Override
			public String doAutoComplete(String userInput) {
				// TODO Implement this properly
				// Basic example: If "del" is in userInput, return "delete"
				// Implementation could be as sophisticated as needed
				
				// Sample (Bad!) Implementation:
				if (userInput.trim().equals("d") || userInput.trim().equals("de") || userInput.trim().equals("del")) {
					return "delete ";
				}
				
				if (userInput.trim().equals("ex") || userInput.trim().equals("exi")) {
					return "exit";
				}
				
				return userInput;
			}
			
		});
	}

	public void start(String[] args) {
		// Initialize components
		parser = new ParserMainImpl();

		// Get file from argument
		File storageFile = null;
		
		// Use default filename if no argument specified
		if (args.length == 0) {
			storageFile = new File("storage.txt");
		} else {
			storageFile = new File(args[0]);
		}

		// Initialize data logic (which would create the storage file if needed)
		// Exit application if fails
		try {
			data = DataImpl.GetSingleton().init(new IOJSONImpl(storageFile));
		} catch (IOException ex) {
			System.err.println("No permission to access file.");
			Main.Exit();
			return;
		}
	}
	
	private void processUserInput(String input) {
		Command command = parser.parseUserInput(input);

		if (command == null) {
			ui.showError("Invalid command entered. Please try again.");
		} else {
			command.processCommand(data, ui);
		}
	}

}
