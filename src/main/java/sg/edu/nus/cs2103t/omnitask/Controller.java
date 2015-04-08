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
import sg.edu.nus.cs2103t.omnitask.ui.UI.CommandReceivedListener;
import sg.edu.nus.cs2103t.omnitask.ui.UIMainImpl;
import sg.edu.nus.cs2103t.omnitasks.command.Command;

public class Controller extends Application {

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

	CommandReceivedListener commandReceivedListener = new CommandReceivedListener() {

		public ArrayList<String> doAutoComplete(String userInput) {
			return generatePossibleAutoComplete(userInput);
		}

		public boolean onCommandReceived(String userInput) {
			return processUserInput(userInput);
		}

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

	};

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Initialize UI
		ui = new UIMainImpl(primaryStage);
		ui.setCommandReceivedListener(commandReceivedListener);

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

			// check that the path is still valid (eg. user deletes the folder then the path is invalid)
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

	private ArrayList<String> generatePossibleAutoComplete(String userInput) {
		ArrayList<String> possibleAutoComplete = new ArrayList<String>();

		// TODO Implement this properly
		// Basic example: If "del" is in userInput, return "delete"
		// Implementation could be as sophisticated as needed

		// Sample (Bad!) Implementation:
		if (userInput.trim().equals("a") || userInput.trim().equals("ad")
				|| userInput.trim().equals("add")) {
			possibleAutoComplete.add("add");
		}

		if (userInput.trim().equals("d") || userInput.trim().equals("de")
				|| userInput.trim().equals("del")
				|| userInput.trim().equals("dele")
				|| userInput.trim().equals("delet")) {
			possibleAutoComplete.add("delete");
		}
		
		if (userInput.trim().equals("d") || userInput.trim().equals("di")
				|| userInput.trim().equals("dis")
				|| userInput.trim().equals("disp")
				|| userInput.trim().equals("displ")
				|| userInput.trim().equals("displa")) {
			possibleAutoComplete.add("display");
		}

		if (userInput.trim().equals("e") || userInput.trim().equals("ed")
				|| userInput.trim().equals("edi")) {
			possibleAutoComplete.add("edit");
		}

		if (userInput.trim().equals("r") || userInput.trim().equals("re")
				|| userInput.trim().equals("red")) {
			possibleAutoComplete.add("redo");
		}

		if (userInput.trim().equals("u") || userInput.trim().equals("un")
				|| userInput.trim().equals("und")) {
			possibleAutoComplete.add("undo");
		}

		if (userInput.trim().equals("s") || userInput.trim().equals("se")
				|| userInput.trim().equals("sea")
				|| userInput.trim().equals("sear")
				|| userInput.trim().equals("searc")) {
			possibleAutoComplete.add("search");
		}

		if (userInput.trim().equals("s") || userInput.trim().equals("st")
				|| userInput.trim().equals("sto")
				|| userInput.trim().equals("stor")
				|| userInput.trim().equals("stora")
				|| userInput.trim().equals("storag")) {
			possibleAutoComplete.add("storage");
		}

		if (userInput.trim().equals("h") || userInput.trim().equals("he")
				|| userInput.trim().equals("hel")) {
			possibleAutoComplete.add("help");
		}

		if (userInput.trim().equals("e") || userInput.trim().equals("ex")
				|| userInput.trim().equals("exi")) {
			possibleAutoComplete.add("exit");
		}

		return possibleAutoComplete;
	}

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
