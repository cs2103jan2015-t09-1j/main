package sg.edu.nus.cs2103t.omnitask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import sg.edu.nus.cs2103t.omnitask.logic.Data;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.parser.ParserMainImpl;
import sg.edu.nus.cs2103t.omnitask.storage.IO;
import sg.edu.nus.cs2103t.omnitask.storage.IOJSONImpl;
import sg.edu.nus.cs2103t.omnitask.ui.UI;
import sg.edu.nus.cs2103t.omnitask.ui.UI.ControllerCallback;
import sg.edu.nus.cs2103t.omnitask.ui.UIMainImpl;
import sg.edu.nus.cs2103t.omnitasks.command.Command;

/**
 * @author TLX
 *
 */

public class Controller extends Application implements ControllerCallback {

	private static UI ui;

	public static void Exit() {
		ui.exit();
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

	protected Data data;

	protected Parser parser;

	public ArrayList<String> doAutoComplete(String userInput) {
		return generatePossibleAutoComplete(userInput);
	}

	public boolean onCommandReceived(String userInput) {
		return processUserInput(userInput);
	}
	
	/**This function display the mini pop up of help specific to each valid command use types in to aid
	 * the user in getting the correct commands format
	 * <p>
	 * 
	 * @author tlx
	 * 
	 * @param userInput String by of text user key in
	 *           
	 * @return void
	 */
	@Override
	public void showMiniHelpIfAvailable(String userInput) {
		// TODO To be fixed
		// with proper
		// implementation
		// adn SLAP similar
		// to
		// doAutoComplete?

		// Sample
		// implementation:
		ArrayList<String> possibleCommands = generatePossibleAutoComplete(userInput);
		if (possibleCommands.size() > 0) {
			try {
				String help = data.getHelpDescriptors(
						possibleCommands.get(0).toUpperCase(), true);
				if (help != null && !help.isEmpty()) {
					ui.showMiniHelp(help);
				} else {
					ui.closeMiniHelp();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				ui.closeMiniHelp();
			}
		} else {
			ui.closeMiniHelp();
		}
	}

	@Override
	public boolean updateTask(Task task) {
		return data.editTask(task);
	}

	/**This function is called on the first instance the program starts up to  
	 * initialize and display the user interface.
	 * <p>
	 * 
	 * @author tlx
	 * 
	 * @param primaryStage which is a Stage object used by UI component
	 *           
	 * @return void
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		// Initialize UI
		ui = new UIMainImpl(primaryStage);
		ui.setCommandCallback(this);

		// Initialize other components
		parser = new ParserMainImpl();

		// Get file from argument
		File storageFile = null;

		// Use default filename if no argument specified
		/*
		 * String[] args = getParameters().getRaw().toArray(new String[] {}); if
		 * (args.length == 0) { storageFile = new File("storage.txt"); } else {
		 * storageFile = new File(args[0]); }
		 */

		// Initialize data logic (which would create the storage file if needed)
		// Exit application if fails
		// if the user has set a location for storage read from that directory

		String userSetDir = "";
		try {
			userSetDir = IO.readFromConfFile();
		} catch (FileNotFoundException io) {

		}
		storageFile = new File("storage.txt");// default

		if (userSetDir.length() > 0) {

			// check that the path is still valid (eg. user deletes the folder
			// then the path is invalid)
			// else it is set to the default storage path
			Path path = Paths.get(userSetDir);
			if (Files.exists(path)) {
				storageFile = new File(userSetDir + "\\storage.txt");
			}

		}

		try {
			data = DataImpl.GetSingleton().init(new IOJSONImpl(storageFile));
		} catch (IOException ex) {
			System.err.println("No permission to access file.");
			Controller.Exit();
			return;
		}

		// Start UI
		ui.start();
	}
	
	/**Detects and generates list of possible comamands as user types in the textbox
	 * <p>
	 * 
	 * @author tlx
	 * 
	 * @param userInput string which contains text that users types in the textbox
	 *           
	 * @return ArrayList<String> generated list of possible commands
	 */
	private ArrayList<String> generatePossibleAutoComplete(String userInput) {
		ArrayList<String> possibleAutoComplete = new ArrayList<String>();

		// TODO Implement this properly
		// Basic example: If "del" is in userInput, return "delete"
		// Implementation could be as sophisticated as needed
		String[] possibleCommands = new String[] { "add", "archive", "delete",
				"display", "edit", "help", "mark", "next", "prev", "redo",
				"undo", "remove-date", "search", "storage", "unarchive" };

		for (int i = 0; i < possibleCommands.length; i++) {
			if (!userInput.equals("")
					&& possibleCommands[i].startsWith(userInput.trim())) {
				possibleAutoComplete.add(possibleCommands[i]);
			}

		}

		/*
		 * / Sample (Bad!) Implementation: if (userInput.trim().equals("a") ||
		 * userInput.trim().equals("ad") || userInput.trim().equals("add")) {
		 * possibleAutoComplete.add("add"); }
		 */

		return possibleAutoComplete;
	}
	
	/**process the entire string of text key in by user
	 * <p>
	 * 
	 * @author tlx
	 * 
	 * @param input the string input of text key in by user 
	 *           
	 * @return boolean indicating if the commands key in by user is valid
	 */
	private boolean processUserInput(String input) {
		Command command = parser.parseUserInput(input);

		if (command == null) {
			ui.showError("Invalid command entered. Please try again.");
			return false;
		} else {
			command.processCommand(data, ui);
			return true;
		}
	}

}
