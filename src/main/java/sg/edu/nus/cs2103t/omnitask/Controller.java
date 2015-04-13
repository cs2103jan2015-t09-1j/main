package sg.edu.nus.cs2103t.omnitask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;
import javafx.application.Application;
import javafx.stage.Stage;
import sg.edu.nus.cs2103t.omnitask.data.Data;
import sg.edu.nus.cs2103t.omnitask.data.StorageBackedData;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput;
import sg.edu.nus.cs2103t.omnitask.item.CommandInput.CommandType;
import sg.edu.nus.cs2103t.omnitask.parser.Parser;
import sg.edu.nus.cs2103t.omnitask.storage.JsonStorage;
import sg.edu.nus.cs2103t.omnitask.storage.Storage;
import sg.edu.nus.cs2103t.omnitask.ui.JavaFxUi;
import sg.edu.nus.cs2103t.omnitask.ui.Ui;
import sg.edu.nus.cs2103t.omnitask.ui.Ui.ControllerCallback;
import sg.edu.nus.cs2103t.omnitasks.command.Command;
import sg.edu.nus.cs2103t.omnitasks.command.CommandAdd;
import sg.edu.nus.cs2103t.omnitasks.command.CommandArchive;
import sg.edu.nus.cs2103t.omnitasks.command.CommandDelete;
import sg.edu.nus.cs2103t.omnitasks.command.CommandDisplay;
import sg.edu.nus.cs2103t.omnitasks.command.CommandEdit;
import sg.edu.nus.cs2103t.omnitasks.command.CommandExit;
import sg.edu.nus.cs2103t.omnitasks.command.CommandHelp;
import sg.edu.nus.cs2103t.omnitasks.command.CommandMark;
import sg.edu.nus.cs2103t.omnitasks.command.CommandNext;
import sg.edu.nus.cs2103t.omnitasks.command.CommandPrev;
import sg.edu.nus.cs2103t.omnitasks.command.CommandRedo;
import sg.edu.nus.cs2103t.omnitasks.command.CommandRemoveDate;
import sg.edu.nus.cs2103t.omnitasks.command.CommandSearch;
import sg.edu.nus.cs2103t.omnitasks.command.CommandStorage;
import sg.edu.nus.cs2103t.omnitasks.command.CommandUnarchive;
import sg.edu.nus.cs2103t.omnitasks.command.CommandUndo;

public class Controller extends Application implements ControllerCallback {

	private static Ui ui;

	// @author A0111795A
	public static void Exit() {
		ui.exit();
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

	protected Data data;

	protected Parser parser;

	// @author A0119643A
	public ArrayList<String> doAutoComplete(String userInput) {
		return generatePossibleAutoComplete(userInput);
	}

	public boolean onCommandReceived(String userInput) {
		return processUserInput(userInput);
	}

	/**
	 * This function display the mini pop up of help specific to each valid
	 * command use types in to aid the user in getting the correct commands
	 * format
	 * <p>
	 * 
	 * 
	 * 
	 * @param userInput
	 *            String by of text user key in
	 * 
	 * @return void
	 */
	@Override
	public void showMiniHelpIfAvailable(String userInput) {
		ArrayList<String> possibleCommands = generatePossibleAutoComplete(userInput);
		if (possibleCommands.size() > 0) {
			try {
				String help = data.getHelpDescriptors(possibleCommands.get(0)
						.toUpperCase(), true);
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

	/**
	 * This function is called on the first instance the program starts up to
	 * initialize and display the user interface.
	 * <p>
	 * 
	 * 
	 * 
	 * @param primaryStage
	 *            which is a Stage object used by UI component
	 * 
	 * @return void
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		// Initialize UI
		ui = new JavaFxUi(primaryStage);
		ui.setControllerCallback(this);

		// Initialize other components
		parser = new Parser();

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
			userSetDir = Storage.readFromConfFile();
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
			data = StorageBackedData.GetSingleton().init(
					new JsonStorage(storageFile));
		} catch (IOException ex) {
			System.err.println("No permission to access file.");
			Controller.Exit();
			return;
		}

		// Start UI
		ui.start();
	}

	/**
	 * Detects and generates list of possible comamands as user types in the
	 * textbox
	 * <p>
	 * 
	 * 
	 * 
	 * @param userInput
	 *            string which contains text that users types in the textbox
	 * 
	 * @return ArrayList<String> generated list of possible commands
	 */
	private ArrayList<String> generatePossibleAutoComplete(String userInput) {

		ArrayList<String> possibleAutoComplete = new ArrayList<String>();
		ArrayList<String[]> allCommandAliases = new ArrayList<String[]>();
		ArrayList<String> possibleCommands = new ArrayList<String>();

		allCommandAliases.add(CommandAdd.COMMAND_ALIASES);
		allCommandAliases.add(CommandArchive.COMMAND_ALIASES);
		allCommandAliases.add(CommandDelete.COMMAND_ALIASES);
		allCommandAliases.add(CommandDisplay.COMMAND_ALIASES);
		allCommandAliases.add(CommandEdit.COMMAND_ALIASES);
		allCommandAliases.add(CommandExit.COMMAND_ALIASES);
		allCommandAliases.add(CommandHelp.COMMAND_ALIASES);
		allCommandAliases.add(CommandMark.COMMAND_ALIASES);
		allCommandAliases.add(CommandNext.COMMAND_ALIASES);
		allCommandAliases.add(CommandPrev.COMMAND_ALIASES);
		allCommandAliases.add(CommandRedo.COMMAND_ALIASES);
		allCommandAliases.add(CommandRemoveDate.COMMAND_ALIASES);
		allCommandAliases.add(CommandSearch.COMMAND_ALIASES);
		allCommandAliases.add(CommandStorage.COMMAND_ALIASES);
		allCommandAliases.add(CommandUnarchive.COMMAND_ALIASES);
		allCommandAliases.add(CommandUndo.COMMAND_ALIASES);

		

		for (int i = 0; i < allCommandAliases.size(); i++) {
			for (String s : allCommandAliases.get(i)) {
				if (!s.trim().isEmpty()) {
					possibleCommands.add(s);
				}
			}
		}
		
		Collections.sort(possibleCommands);
		
		
		for (int i = 0; i < possibleCommands.size(); i++) {
			if (!userInput.equals("")
					&& possibleCommands.get(i).startsWith(userInput.toLowerCase().trim())) {
				possibleAutoComplete.add(possibleCommands.get(i));
			}

		}

		return possibleAutoComplete;
	}

	/**
	 * process the entire string of text key in by user
	 * <p>
	 * 
	 * 
	 * 
	 * @param input
	 *            the string input of text key in by user
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

	// @author A0111795A
	@Override
	public void showAll() {
		CommandInput commandInput = new CommandInput(CommandType.SHOW);
		commandInput.setName("all");
		Command command = new CommandDisplay(commandInput);
		command.processCommand(data, ui);
	}

	@Override
	public boolean markTaskAsDone(long id) {
		CommandInput commandInput = new CommandInput(CommandType.MARK);
		commandInput.setId(id);
		commandInput.setCompleted(true);
		Command command = new CommandMark(commandInput);
		return command.processCommand(data, ui);
	}

	@Override
	public boolean markTaskAsNotDone(long id) {
		CommandInput commandInput = new CommandInput(CommandType.MARK);
		commandInput.setId(id);
		commandInput.setCompleted(false);
		Command command = new CommandMark(commandInput);
		return command.processCommand(data, ui);
	}

}
